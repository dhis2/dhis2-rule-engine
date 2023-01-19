package org.dhis2.ruleengine

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.dhis2.ruleengine.models.*
import org.dhis2.ruleengine.utils.*

inline fun buildValueMap(
    ruleEvents: List<RuleEvent>,
    ruleEnrollment: RuleEnrollment?,
    ruleEvent: RuleEvent?,
    ruleVariables: List<RuleVariable>,
    constantsValues: Map<String, String>
): Map<String, RuleVariableValue> {
    return mutableMapOf<String, RuleVariableValue>().buildValueMap(
        ruleEvents,
        ruleEnrollment,
        ruleEvent,
        ruleVariables,
        constantsValues
    )
}

fun MutableMap<String, RuleVariableValue>.buildValueMap(
    ruleEvents: List<RuleEvent>,
    ruleEnrollment: RuleEnrollment?,
    ruleEvent: RuleEvent?,
    ruleVariables: List<RuleVariable>,
    constantsValues: Map<String, String>
): Map<String, RuleVariableValue> {
    return this.buildEnvironmentVariables(
        ruleEvents = ruleEvents,
        ruleEnrollment = ruleEnrollment,
        ruleEvent = ruleEvent
    )
        .buildRuleVariableValues(
            ruleVariables = ruleVariables,
            ruleEvents = ruleEvents,
            ruleEvent = ruleEvent,
            ruleEnrollment = ruleEnrollment
        )
        .buildConstantsValues(
            constantsValues
        )
}

fun MutableMap<String, RuleVariableValue>.buildEnvironmentVariables(
    ruleEvents: List<RuleEvent>,
    ruleEnrollment: RuleEnrollment?,
    ruleEvent: RuleEvent?

): MutableMap<String, RuleVariableValue> {
    val valueMap: MutableMap<String, RuleVariableValue> = mutableMapOf()
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    valueMap[ENV_VAR_CURRENT_DATE] =
        RuleVariableValue(currentDate.toString(), RuleValueType.TEXT, listOf(currentDate.toString()), currentDate)
    val environment: String = getClientName().clientName
    valueMap[ENV_VAR_ENVIRONMENT] =
        RuleVariableValue(environment, RuleValueType.TEXT, listOf(environment), currentDate)

    if (ruleEvents.isNotEmpty()) {
        valueMap[ENV_VAR_EVENT_COUNT] = RuleVariableValue(
            ruleEvents.size.toString(),
            RuleValueType.NUMERIC, listOf(ruleEvents.size.toString()), currentDate
        )
    }
    if (ruleEnrollment != null) {
        valueMap[ENV_VAR_ENROLLMENT_ID] = RuleVariableValue(
            ruleEnrollment.enrollment,
            RuleValueType.TEXT, listOf(ruleEnrollment.enrollment), currentDate
        )
        valueMap[ENV_VAR_ENROLLMENT_COUNT] = RuleVariableValue(
            "1",
            RuleValueType.NUMERIC, listOf("1"), currentDate
        )
        valueMap[ENV_VAR_TEI_COUNT] = RuleVariableValue(
            "1",
            RuleValueType.NUMERIC, listOf("1"), currentDate
        )
        val enrollmentDate: String = ruleEnrollment.enrollmentDate.toString()
        valueMap[ENV_VAR_ENROLLMENT_DATE] = RuleVariableValue(
            enrollmentDate,
            RuleValueType.TEXT, listOf(enrollmentDate), currentDate
        )
        val incidentDate: String = ruleEnrollment.incidentDate.toString()
        valueMap[ENV_VAR_INCIDENT_DATE] = RuleVariableValue(
            incidentDate,
            RuleValueType.TEXT, listOf(incidentDate), currentDate
        )
        val status: String = ruleEnrollment.status.toString()
        valueMap[ENV_VAR_ENROLLMENT_STATUS] = RuleVariableValue(
            status,
            RuleValueType.TEXT, listOf(status), currentDate
        )
        val organisationUnit: String = ruleEnrollment.organisationUnit
        valueMap[ENV_VAR_OU] = RuleVariableValue(organisationUnit, RuleValueType.TEXT)
        val programName: String = ruleEnrollment.programName
        valueMap[ENV_VAR_PROGRAM_NAME] = RuleVariableValue(programName, RuleValueType.TEXT)
        valueMap[ENV_VAR_OU_CODE] = RuleVariableValue(ruleEnrollment.organisationUnitCode, RuleValueType.TEXT)

    }
    if (ruleEvent != null) {
        val eventDate: String = ruleEvent.eventDate.toString()
        valueMap[ENV_VAR_EVENT_DATE] = RuleVariableValue(
            eventDate, RuleValueType.TEXT,
            listOf(eventDate), currentDate
        )
        if (ruleEvent.dueDate != null) {
            val dueDate: String = ruleEvent.dueDate.toString()
            valueMap[ENV_VAR_DUE_DATE] = RuleVariableValue(
                dueDate, RuleValueType.TEXT,
                listOf(dueDate), currentDate
            )
        }
        if (ruleEvent.completedDate != null) {
            val completedDate: String = ruleEvent.completedDate.toString()
            valueMap[ENV_VAR_COMPLETED_DATE] = RuleVariableValue(
                completedDate, RuleValueType.TEXT,
                listOf(completedDate), currentDate
            )
        }

        // override value of event count
        var eventCount: String = (ruleEvents.size + 1).toString()
        if (ruleEvents.contains(ruleEvent)) {
            eventCount = ruleEvents.size.toString()
        }
        valueMap[ENV_VAR_EVENT_COUNT] = RuleVariableValue(
            eventCount,
            RuleValueType.NUMERIC, listOf(eventCount), currentDate
        )
        valueMap[ENV_VAR_EVENT_ID] = RuleVariableValue(
            ruleEvent.event,
            RuleValueType.TEXT, listOf(ruleEvent.event), currentDate
        )
        val status: String = ruleEvent.status.toString()
        valueMap[ENV_VAR_EVENT_STATUS] = RuleVariableValue(
            status,
            RuleValueType.TEXT, listOf(status), currentDate
        )
        val organisationUnit: String = ruleEvent.organisationUnit
        valueMap[ENV_VAR_OU] = RuleVariableValue(organisationUnit, RuleValueType.TEXT)
        val programStageId: String = ruleEvent.programStage
        valueMap[ENV_VAR_PROGRAM_STAGE_ID] = RuleVariableValue(programStageId, RuleValueType.TEXT)
        val programStageName: String = ruleEvent.programStageName
        valueMap[ENV_VAR_PROGRAM_STAGE_NAME] = RuleVariableValue(programStageName, RuleValueType.TEXT)
        valueMap[ENV_VAR_OU_CODE] = RuleVariableValue(ruleEvent.organisationUnitCode, RuleValueType.TEXT)

    }
    putAll(valueMap)
    return this
}

fun MutableMap<String, RuleVariableValue>.buildRuleVariableValues(
    ruleVariables: List<RuleVariable>,
    ruleEvents: List<RuleEvent>,
    ruleEvent: RuleEvent?,
    ruleEnrollment: RuleEnrollment?
): MutableMap<String, RuleVariableValue> {
    val valueMap: MutableMap<String, RuleVariableValue> = mutableMapOf()

    // map data values within all events to data elements
    val allEventValues = buildAllEventValues(
        ruleEvents = ruleEvents,
        ruleEvent = ruleEvent
    )

    // map tracked entity attributes to values from enrollment
    val currentEnrollmentValues = buildCurrentEnrollmentValues(
        ruleEnrollment = ruleEnrollment
    )

    // build a map of current event values
    val currentEventValues = buildCurrentEventValues(
        ruleEvent = ruleEvent
    )
    ruleVariables.forEach { ruleVariable ->
        ruleVariable.toRuleVariableValue(
            ruleEnrollment,
            ruleEvent,
            allEventValues,
            currentEnrollmentValues,
            currentEventValues
        )?.let { ruleVariableValue ->
            valueMap[ruleVariable.name] = ruleVariableValue
        }
    }
    putAll(valueMap)
    return this
}

fun MutableMap<String, RuleVariableValue>.buildConstantsValues(
    constantsValues: Map<String, String>
): Map<String, RuleVariableValue> {
    val valueMap: MutableMap<String, RuleVariableValue> = mutableMapOf()
    constantsValues.forEach { (key, value) ->
        valueMap[key] = RuleVariableValue(value, RuleValueType.NUMERIC)
    }
    putAll(valueMap)
    return this
}

private fun buildAllEventValues(
    ruleEvents: List<RuleEvent>,
    ruleEvent: RuleEvent?
): Map<String, MutableList<RuleDataValue>> {
    val allEventsValues: MutableMap<String, MutableList<RuleDataValue>> = mutableMapOf()
    val events: MutableList<RuleEvent> = ruleEvents.toMutableList()
    if (ruleEvent != null) {
        // target event should be among the list of all
        // events in order to achieve correct behavior
        events.add(ruleEvent)
    }

    // sort list of events by eventDate:
    events.sortByDescending { it.eventDate }

    // aggregating values by data element uid
    events.forEach { currentRuleEvent ->
        currentRuleEvent.dataValues.forEach { ruleDataValue ->
            // push new list if it is not there for the given data element
            if (!allEventsValues.containsKey(ruleDataValue.dataElement)) {
                allEventsValues[ruleDataValue.dataElement] = mutableListOf() //NOPMD
            }

            // append data value to the list
            allEventsValues[ruleDataValue.dataElement]!!.add(ruleDataValue)
        }
    }
    return allEventsValues
}

private fun buildCurrentEnrollmentValues(
    ruleEnrollment: RuleEnrollment?
): Map<String, RuleAttributeValue> {
    val currentEnrollmentValues: MutableMap<String, RuleAttributeValue> = mutableMapOf()
    if (ruleEnrollment != null) {
        val ruleAttributeValues: List<RuleAttributeValue> = ruleEnrollment.attributeValues
        for (index in ruleAttributeValues.indices) {
            val attributeValue = ruleAttributeValues[index]
            currentEnrollmentValues[attributeValue.trackedEntityAttribute] = attributeValue
        }
    }
    return currentEnrollmentValues
}

private fun buildCurrentEventValues(
    ruleEvent: RuleEvent?
): Map<String, RuleDataValue> {
    val currentEventValues: MutableMap<String, RuleDataValue> = mutableMapOf()
    ruleEvent?.dataValues?.forEach { ruleDataValue ->
        currentEventValues[ruleDataValue.dataElement] = ruleDataValue
    }
    return currentEventValues
}