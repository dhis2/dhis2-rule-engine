[![Maven Central](https://img.shields.io/maven-central/v/org.hisp.dhis.rules/rule-engine?label=maven%20central)](https://central.sonatype.com/artifact/org.hisp.dhis.rules/rule-engine)
[![NPM Version](https://img.shields.io/npm/v/@dhis2/rule-engine)](https://www.npmjs.com/package/@dhis2/rule-engine)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dhis2_dhis2-rule-engine&metric=coverage&branch=master)](https://sonarcloud.io/summary/new_code?id=dhis2_dhis2-rule-engine&branch=master)
[![KDoc link](https://img.shields.io/badge/API_reference-KDoc-blue)](https://dhis2.github.io/dhis2-rule-engine/api/)

# DHIS2 Rule Engine

## What is the Rule Engine?

The DHIS2 Rule Engine evaluates **program rules** — configurable business logic that can show or hide fields, assign computed values, display warnings, and enforce data quality constraints in DHIS2 tracker programs.

A program rule has two parts:

- **Condition** — a boolean expression that determines whether the rule fires (e.g. `V{event_status} == 'COMPLETED'`).
- **Actions** — a list of operations to perform when the condition is true (e.g. assign a calculated value to a data element, hide a field, or show an error).

The engine takes a set of rules together with contextual data (an enrollment, a list of events, variable definitions, constants, and supplementary data such as user groups) and returns the list of `RuleEffect`s that should be applied.

## Multiplatform

The rule engine is a **Kotlin Multiplatform** library. A single shared codebase compiles to three targets:

| Target | Distribution | Artifact |
|--------|-------------|---------|
| **JVM** (Java 17) | Maven Central | `org.hisp.dhis.rules:rule-engine` |
| **JavaScript / Node.js** | NPM | `@dhis2/rule-engine` |
| **Kotlin/Native** | — | macOS, Linux, Windows (host-specific build) |

TypeScript definitions (`.d.ts`) are generated automatically from the JS target.

## Installation

**Kotlin / JVM (Gradle)**

```kotlin
implementation("org.hisp.dhis.rules:rule-engine:<version>")
```

**JavaScript / TypeScript (npm)**

```bash
npm install @dhis2/rule-engine
```

## API Overview

All public types live under `org.hisp.dhis.rules.api` and `org.hisp.dhis.rules.models`.

### RuleEngine

The entry point. Obtain a stateless singleton instance and call one of the evaluation methods.

```kotlin
val engine: RuleEngine = RuleEngine.getInstance()
```

| Method | Description |
|--------|-------------|
| `evaluate(target: RuleEvent, ruleEnrollment, ruleEvents, executionContext)` | Evaluate a single event |
| `evaluate(target: RuleEnrollment, ruleEvents, executionContext)` | Evaluate an enrollment |
| `evaluateAll(enrollmentTarget, eventsTarget, executionContext)` | Evaluate enrollment + all events in one call |
| `analyzeContextRequirements(rules, variables)` | Determine what data the rules actually need |
| `validate(expression, dataItemStore)` | Validate a rule condition expression |
| `validateDataFieldExpression(expression, dataItemStore)` | Validate a rule action data expression |
| `order(ruleEvents)` | Sort events by date and creation time |

### RuleEngineContext

Bundles together all configuration needed for one evaluation run. It is a plain data class and can be reused freely.

```kotlin
data class RuleEngineContext(
    val rules: List<Rule>,
    val ruleVariables: List<RuleVariable> = emptyList(),
    val ruleSupplementaryData: RuleSupplementaryData = RuleSupplementaryData(),
    val constantsValues: Map<String, String> = emptyMap(),
)
```

### Rule

A single program rule consisting of a condition and one or more actions.

```kotlin
data class Rule(
    val condition: String,          // Boolean expression; empty string always evaluates to false
    val actions: List<RuleAction>,
    val uid: String = "",
    val name: String? = null,
    val programStage: String? = null, // If set, only fires for this program stage
    val priority: Int? = null,        // Lower number = higher priority
)
```

### RuleAction

Describes what to do when the rule condition is true.

```kotlin
data class RuleAction(
    val data: String?,              // Expression for a computed value (may be null or empty)
    val type: String,               // e.g. "ASSIGN", "HIDEFIELD", "DISPLAYTEXT", "SHOWERROR"
    val values: Map<String, String> = emptyMap(), // e.g. mapOf("field" to "<dataElementUid>")
    val priority: Int? = null,
)
```

Convenience accessors: `field()`, `content()`, `attributeType()`.

### RuleVariable

Represents a program rule variable that maps a data element, tracked entity attribute, or calculated expression to a name that can be referenced in conditions and actions as `#{variableName}`.

```kotlin
interface RuleVariable {
    val name: String
    val useCodeForOptionSet: Boolean
    val options: List<Option>
    val field: String              // UID of the referenced data element or attribute
    val fieldType: RuleValueType   // TEXT, NUMERIC, BOOLEAN, DATE
}
```

Built-in implementations:

| Class | Reads from |
|-------|-----------|
| `RuleVariableCurrentEvent` | Data value of the event being evaluated |
| `RuleVariableNewestEvent` | Most recent data value across all events |
| `RuleVariableNewestStageEvent` | Most recent data value in a specific program stage |
| `RuleVariablePreviousEvent` | Data value from the event just before the current one |
| `RuleVariableAttribute` | Tracked entity attribute value from the enrollment |
| `RuleVariableCalculatedValue` | Dynamically computed expression |

### RuleEvent and RuleEnrollment

These are the data objects passed to the engine as evaluation context.

```kotlin
data class RuleEvent(
    val event: String,
    val programStage: String,
    val programStageName: String,
    val status: RuleEventStatus,       // ACTIVE, COMPLETED, SCHEDULE, OVERDUE, SKIPPED, VISITED
    val eventDate: RuleLocalDate,
    val createdDate: RuleInstant,
    val createdAtClientDate: RuleInstant?,
    val dueDate: RuleLocalDate?,
    val completedDate: RuleLocalDate?,
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val dataValues: List<RuleDataValue>,
)

data class RuleEnrollment(
    val enrollment: String,
    val programName: String,
    val incidentDate: RuleLocalDate,
    val enrollmentDate: RuleLocalDate,
    val status: RuleEnrollmentStatus,  // ACTIVE, COMPLETED, CANCELLED
    val organisationUnit: String,
    val organisationUnitCode: String?,
    val attributeValues: List<RuleAttributeValue>,
)
```

### RuleEffect and RuleEffects

The result of an evaluation.

```kotlin
data class RuleEffect(
    val ruleId: String,
    val ruleAction: RuleAction,
    val data: String?,   // Computed value of the action's data expression
)

data class RuleEffects(
    val trackerObjectType: TrackerObjectType, // EVENT or ENROLLMENT
    val trackerObjectUid: String,
    val ruleEffects: List<RuleEffect>,
)
```

### RuleSupplementaryData

Extra runtime context for user-specific and org-unit-specific rule functions.

```kotlin
data class RuleSupplementaryData(
    val userGroups: List<String> = emptyList(),
    val userRoles: List<String> = emptyList(),
    val orgUnitGroups: Map<String, List<String>> = emptyMap(), // orgUnitUid -> listOf(groupUid, ...)
)
```

### RuleContextRequirements

Returned by `analyzeContextRequirements()` to tell callers exactly what data the configured rules need. Use this to avoid fetching data that rules do not reference.

```kotlin
data class RuleContextRequirements(
    val needsAllEvents: Boolean,
    val needsEnrollment: Boolean,
    val needsDataValues: Boolean,
    val needsAttributes: Boolean,
    val needsOrgUnitGroups: Boolean,
)
```

### Environment Variables

Built-in variables available in every rule expression via `V{variableName}`:

| Variable | Type | Description |
|----------|------|-------------|
| `current_date` | DATE | Today's date |
| `event_date` | DATE | Date of the event being evaluated |
| `due_date` | DATE | Due date of the event |
| `completed_date` | DATE | Date the event was completed |
| `enrollment_date` | DATE | Date of the enrollment |
| `incident_date` | DATE | Incident date of the enrollment |
| `event_id` | NUMBER | UID of the current event |
| `enrollment_id` | NUMBER | UID of the enrollment |
| `program_stage_id` | NUMBER | UID of the current program stage |
| `event_count` | NUMBER | Total number of events in context |
| `enrollment_count` | NUMBER | Number of enrollments |
| `tei_count` | NUMBER | Number of tracked entity instances |
| `event_status` | TEXT | Status of the current event |
| `enrollment_status` | TEXT | Status of the enrollment |
| `org_unit` | TEXT | Organisation unit UID |
| `orgunit_code` | TEXT | Organisation unit code |
| `program_name` | TEXT | Name of the program |
| `program_stage_name` | TEXT | Name of the current program stage |
| `environment` | TEXT | Execution environment identifier |

The full map of environment variables and their types is available via `EnvironmentVariables.ENV_VARIABLES`.

## How to Use the Rule Engine

### 1. Evaluate a single event (program without registration)

```kotlin
val ruleAction = RuleAction(
    data = "'HIGH_RISK'",
    type = "ASSIGN",
    values = mapOf("field" to "risk_category_data_element_uid"),
)

val rule = Rule(
    condition = "#{weight} > 90",
    actions = listOf(ruleAction),
    uid = "rule_uid",
)

val weightVariable = RuleVariableCurrentEvent(
    name = "weight",
    useCodeForOptionSet = false,
    options = emptyList(),
    field = "weight_data_element_uid",
    fieldType = RuleValueType.NUMERIC,
)

val context = RuleEngineContext(
    rules = listOf(rule),
    ruleVariables = listOf(weightVariable),
)

val event = RuleEvent(
    event = "event_uid",
    programStage = "stage_uid",
    programStageName = "Consultation",
    status = RuleEventStatus.ACTIVE,
    eventDate = RuleLocalDate(2024, 6, 1),
    createdDate = RuleInstant.now(),
    createdAtClientDate = null,
    dueDate = null,
    completedDate = null,
    organisationUnit = "org_unit_uid",
    organisationUnitCode = null,
    dataValues = listOf(RuleDataValue("weight_data_element_uid", "95")),
)

val effects: List<RuleEffect> = RuleEngine.getInstance().evaluate(
    target = event,
    ruleEnrollment = null,
    ruleEvents = emptyList(),
    executionContext = context,
)
// effects[0].data == "HIGH_RISK"
```

### 2. Evaluate an event with enrollment context (tracker program)

```kotlin
val enrollment = RuleEnrollment(
    enrollment = "enrollment_uid",
    programName = "Antenatal Care",
    incidentDate = RuleLocalDate(2024, 1, 10),
    enrollmentDate = RuleLocalDate(2024, 1, 10),
    status = RuleEnrollmentStatus.ACTIVE,
    organisationUnit = "org_unit_uid",
    organisationUnitCode = null,
    attributeValues = listOf(
        RuleAttributeValue("age_attribute_uid", "17"),
    ),
)

val effects: List<RuleEffect> = RuleEngine.getInstance().evaluate(
    target = event,
    ruleEnrollment = enrollment,
    ruleEvents = previousEvents,
    executionContext = context,
)
```

### 3. Evaluate an enrollment

```kotlin
val effects: List<RuleEffect> = RuleEngine.getInstance().evaluate(
    target = enrollment,
    ruleEvents = allEnrollmentEvents,
    executionContext = context,
)
```

### 4. Evaluate everything at once

`evaluateAll` processes the enrollment and all provided events in a single call and returns one `RuleEffects` per evaluated object.

```kotlin
val allEffects: List<RuleEffects> = RuleEngine.getInstance().evaluateAll(
    enrollmentTarget = enrollment,
    eventsTarget = listOf(event1, event2, event3),
    executionContext = context,
)

allEffects.forEach { ruleEffects ->
    if (ruleEffects.isEnrollment) {
        // apply enrollment effects
    } else {
        // apply event effects for ruleEffects.trackerObjectUid
    }
}
```

### 5. Analyze context requirements

Before loading data from the server, call `analyzeContextRequirements` to find out exactly what the configured rules need.

```kotlin
val requirements: RuleContextRequirements = RuleEngine.getInstance()
    .analyzeContextRequirements(rules, ruleVariables)

if (requirements.needsEnrollment) { /* fetch enrollment */ }
if (requirements.needsAllEvents)  { /* fetch all events */ }
if (requirements.needsAttributes) { /* fetch TEA values */ }
if (requirements.needsDataValues) { /* fetch event data values */ }
if (requirements.needsOrgUnitGroups) { /* fetch org unit groups */ }
```

### 6. Validate an expression

```kotlin
val dataItems = mapOf(
    "weight" to DataItem("Weight (kg)", ItemValueType.NUMBER),
) + EnvironmentVariables.ENV_VARIABLES.mapValues { (k, v) ->
    DataItem(k, v)
}

val result: RuleValidationResult = RuleEngine.getInstance().validate(
    expression = "#{weight} > 90 && V{event_status} != 'COMPLETED'",
    dataItemStore = dataItems,
)

if (result.valid) {
    println("Expression is valid: ${result.description}")
} else {
    println("Invalid expression: ${result.errorMessage}")
}
```

### 7. Using supplementary data (user groups, org unit groups)

```kotlin
val context = RuleEngineContext(
    rules = listOf(rule),
    ruleSupplementaryData = RuleSupplementaryData(
        userGroups = listOf("admin_group_uid", "data_entry_group_uid"),
        orgUnitGroups = mapOf(
            "facility_uid" to listOf("urban_group_uid", "level3_group_uid"),
        ),
    ),
    constantsValues = mapOf("THRESHOLD" to "90"),
)
```

### 8. Using constants

Constants are referenced in expressions as `C{constantName}`:

```kotlin
val context = RuleEngineContext(
    rules = listOf(Rule("#{weight} > C{WEIGHT_THRESHOLD}", listOf(action))),
    constantsValues = mapOf("WEIGHT_THRESHOLD" to "90"),
)
```

---

## Development

### API Validator

This library uses the [Binary Compatibility Validator plugin](https://github.com/Kotlin/binary-compatibility-validator), which verifies that there are no changes in the public API of the library.

The way of work is: there is a file generated by the plugin (`api/rule-engine.api`) with the binary representation of the expected public API. The plugin has a task (`apiCheck`) that verifies that the public API of the code matches the expected API in the file.

If there is an intentional change in the public API, this file must be updated to reflect the changes and included as part of the pull request. It can be updated running the task `apiDump`. This task can be run in the command line (`./gradlew apiDump`) or using Gradle window in Intellij.

### Version

Library version is defined in the file `build.gradle.kts`. The version must be manually increased and include the `-SNAPSHOT` suffix. Please make sure the version is updated before opening the PR.

### Publications

On merged pull request to `main`:
- Production release to Maven.
- Production release to NPMJS.

On pull request creation/update:
- Snapshot release to Maven.

On demand:
- Beta releases to NPMJS can be triggered on demand by using the action "Publish NPM beta".
  Please make sure you select the right branch in the selector.

Publication can be skipped by adding `[skip publish]` to the pull request title.
