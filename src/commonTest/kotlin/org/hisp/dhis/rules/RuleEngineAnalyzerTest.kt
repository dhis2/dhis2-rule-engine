package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.RuleContextRequirements
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.engine.RuleEngineAnalyzer
import org.hisp.dhis.rules.models.Option
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleAction
import org.hisp.dhis.rules.models.RuleValueType
import org.hisp.dhis.rules.models.RuleVariableAttribute
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent
import org.hisp.dhis.rules.models.RuleVariableNewestEvent
import org.hisp.dhis.rules.models.RuleVariableNewestStageEvent
import org.hisp.dhis.rules.models.RuleVariableCalculatedValue
import org.hisp.dhis.rules.models.RuleVariablePreviousEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuleEngineAnalyzerTest {

    // --- helpers ---

    private fun currentEvent(name: String) = RuleVariableCurrentEvent(
        name = name,
        useCodeForOptionSet = false,
        options = emptyList<Option>(),
        field = "field_$name",
        fieldType = RuleValueType.TEXT,
    )

    private fun newestEvent(name: String) = RuleVariableNewestEvent(
        name = name,
        useCodeForOptionSet = false,
        options = emptyList<Option>(),
        field = "field_$name",
        fieldType = RuleValueType.TEXT,
    )

    private fun newestStageEvent(name: String) = RuleVariableNewestStageEvent(
        name = name,
        useCodeForOptionSet = false,
        options = emptyList<Option>(),
        field = "field_$name",
        fieldType = RuleValueType.TEXT,
        programStage = "stage1",
    )

    private fun previousEvent(name: String) = RuleVariablePreviousEvent(
        name = name,
        useCodeForOptionSet = false,
        options = emptyList<Option>(),
        field = "field_$name",
        fieldType = RuleValueType.TEXT,
    )

    private fun attribute(name: String) = RuleVariableAttribute(
        name = name,
        useCodeForOptionSet = false,
        options = emptyList<Option>(),
        field = "field_$name",
        fieldType = RuleValueType.TEXT,
    )

    private fun rule(condition: String, vararg actionData: String?): Rule {
        val actions = actionData.map { RuleAction(it, "DISPLAYTEXT") }
        return Rule(condition, actions)
    }

    private fun analyze(rules: List<Rule>, variables: List<org.hisp.dhis.rules.models.RuleVariable> = emptyList()) =
        RuleEngineAnalyzer.analyzeContextRequirements(rules, variables)

    // --- NONE constant ---

    @Test
    fun noneConstantHasAllFieldsFalse() {
        val none = RuleContextRequirements.NONE
        assertFalse(none.needsAllEvents)
        assertFalse(none.needsEnrollment)
        assertFalse(none.needsDataValues)
        assertFalse(none.needsAttributes)
        assertTrue(none.orgUnitGroups.isEmpty())
    }

    @Test
    fun emptyRulesAndVariablesReturnsNone() {
        assertEquals(RuleContextRequirements.NONE, analyze(emptyList()))
    }

    // --- needsAllEvents ---

    @Test
    fun eventCountEnvVarInConditionTriggersAllEvents() {
        assertTrue(analyze(listOf(rule("V{event_count} > 2"))).needsAllEvents)
    }

    @Test
    fun eventCountEnvVarInActionDataTriggersAllEvents() {
        assertTrue(analyze(listOf(rule("true", "V{event_count}"))).needsAllEvents)
    }

    @Test
    fun unrelatedEnvVarDoesNotTriggerAllEvents() {
        assertFalse(analyze(listOf(rule("V{enrollment_date} > '2020-01-01'"))).needsAllEvents)
    }

    @Test
    fun newestEventVariableTriggersAllEvents() {
        val result = analyze(listOf(rule("#{weight} > 0")), listOf(newestEvent("weight")))
        assertTrue(result.needsAllEvents)
    }

    @Test
    fun newestStageEventVariableTriggersAllEvents() {
        val result = analyze(listOf(rule("#{bp} > 90")), listOf(newestStageEvent("bp")))
        assertTrue(result.needsAllEvents)
    }

    @Test
    fun previousEventVariableTriggersAllEvents() {
        val result = analyze(listOf(rule("#{prev_weight} < #{weight}")), listOf(previousEvent("prev_weight"), currentEvent("weight")))
        assertTrue(result.needsAllEvents)
    }

    @Test
    fun currentEventVariableAloneDoesNotTriggerAllEvents() {
        val result = analyze(listOf(rule("#{score} > 0")), listOf(currentEvent("score")))
        assertFalse(result.needsAllEvents)
    }

    @Test
    fun variableDefinedButNotReferencedDoesNotTriggerAllEvents() {
        val result = analyze(listOf(rule("#{other} > 0")), listOf(newestEvent("unreferenced"), currentEvent("other")))
        assertFalse(result.needsAllEvents)
    }

    // --- needsDataValues ---

    @Test
    fun currentEventVariableTriggersNeedsDataValues() {
        val result = analyze(listOf(rule("#{score} > 0")), listOf(currentEvent("score")))
        assertTrue(result.needsDataValues)
    }

    @Test
    fun newestEventVariableTriggersNeedsDataValues() {
        val result = analyze(listOf(rule("#{weight} > 0")), listOf(newestEvent("weight")))
        assertTrue(result.needsDataValues)
    }

    @Test
    fun newestStageEventVariableTriggersNeedsDataValues() {
        val result = analyze(listOf(rule("#{bp} > 90")), listOf(newestStageEvent("bp")))
        assertTrue(result.needsDataValues)
    }

    @Test
    fun previousEventVariableTriggersNeedsDataValues() {
        val result = analyze(listOf(rule("#{prev} > 0")), listOf(previousEvent("prev")))
        assertTrue(result.needsDataValues)
    }

    @Test
    fun attributeVariableAloneDoesNotTriggerNeedsDataValues() {
        val result = analyze(listOf(rule("#{attr} > 0")), listOf(attribute("attr")))
        assertFalse(result.needsDataValues)
    }

    @Test
    fun noVariablesReferencedDoesNotTriggerNeedsDataValues() {
        assertFalse(analyze(listOf(rule("true"))).needsDataValues)
    }

    // --- needsAttributes ---

    @Test
    fun attributeVariableTriggersNeedsAttributes() {
        val result = analyze(listOf(rule("#{attr} == 'value'")), listOf(attribute("attr")))
        assertTrue(result.needsAttributes)
    }

    @Test
    fun attributeVariableInActionDataTriggersNeedsAttributes() {
        val result = analyze(listOf(rule("true", "#{attr}")), listOf(attribute("attr")))
        assertTrue(result.needsAttributes)
    }

    @Test
    fun nonAttributeVariableDoesNotTriggerNeedsAttributes() {
        val result = analyze(listOf(rule("#{score} > 0")), listOf(currentEvent("score")))
        assertFalse(result.needsAttributes)
    }

    @Test
    fun unreferencedAttributeDoesNotTriggerNeedsAttributes() {
        val result = analyze(listOf(rule("#{other} > 0")), listOf(attribute("unreferenced"), currentEvent("other")))
        assertFalse(result.needsAttributes)
    }

    // --- needsEnrollment ---

    @Test
    fun enrollmentDateEnvVarTriggersNeedsEnrollment() {
        assertTrue(analyze(listOf(rule("V{enrollment_date} > '2020-01-01'"))).needsEnrollment)
    }

    @Test
    fun enrollmentIdEnvVarTriggersNeedsEnrollment() {
        assertTrue(analyze(listOf(rule("V{enrollment_id} != ''"))).needsEnrollment)
    }

    @Test
    fun enrollmentCountEnvVarTriggersNeedsEnrollment() {
        assertTrue(analyze(listOf(rule("V{enrollment_count} > 0"))).needsEnrollment)
    }

    @Test
    fun enrollmentStatusEnvVarTriggersNeedsEnrollment() {
        assertTrue(analyze(listOf(rule("V{enrollment_status} == 'ACTIVE'"))).needsEnrollment)
    }

    @Test
    fun incidentDateEnvVarTriggersNeedsEnrollment() {
        assertTrue(analyze(listOf(rule("V{incident_date} > '2020-01-01'"))).needsEnrollment)
    }

    @Test
    fun teiCountEnvVarTriggersNeedsEnrollment() {
        assertTrue(analyze(listOf(rule("V{tei_count} > 0"))).needsEnrollment)
    }

    @Test
    fun attributeVariableTriggersNeedsEnrollment() {
        val result = analyze(listOf(rule("#{attr} == 'value'")), listOf(attribute("attr")))
        assertTrue(result.needsEnrollment)
    }

    @Test
    fun unrelatedEnvVarDoesNotTriggerNeedsEnrollment() {
        assertFalse(analyze(listOf(rule("V{event_date} > '2020-01-01'"))).needsEnrollment)
    }

    @Test
    fun dataValueVariableAloneDoesNotTriggerNeedsEnrollment() {
        val result = analyze(listOf(rule("#{score} > 0")), listOf(currentEvent("score")))
        assertFalse(result.needsEnrollment)
    }

    // --- orgUnitGroups ---

    @Test
    fun orgUnitGroupInConditionIsCollected() {
        val result = analyze(listOf(rule("d2:inOrgUnitGroup('GroupA')")))
        assertEquals(setOf("GroupA"), result.orgUnitGroups)
    }

    @Test
    fun orgUnitGroupInActionDataIsCollected() {
        val result = analyze(listOf(rule("true", "d2:inOrgUnitGroup('GroupB')")))
        assertEquals(setOf("GroupB"), result.orgUnitGroups)
    }

    @Test
    fun multipleOrgUnitGroupsAcrossRulesAreCollected() {
        val rules = listOf(
            rule("d2:inOrgUnitGroup('GroupA')"),
            rule("true", "d2:inOrgUnitGroup('GroupB')"),
            rule("d2:inOrgUnitGroup('GroupA') || d2:inOrgUnitGroup('GroupC')"),
        )
        assertEquals(setOf("GroupA", "GroupB", "GroupC"), analyze(rules).orgUnitGroups)
    }

    @Test
    fun noOrgUnitGroupReturnsEmptySet() {
        assertTrue(analyze(listOf(rule("#{score} > 5")), listOf(currentEvent("score"))).orgUnitGroups.isEmpty())
    }

    // --- RuleEngine interface delegation ---

    @Test
    fun ruleEngineAnalyzeContextRequirementsDelegatesCorrectly() {
        val rules = listOf(rule("V{event_count} > 2"))
        assertTrue(RuleEngine.getInstance().analyzeContextRequirements(rules, emptyList()).needsAllEvents)
    }

    @Test
    fun ruleEngineAnalyzeContextRequirementsReturnsFalseForUnrelatedContext() {
        val rules = listOf(rule("#{score} > 5"))
        val variables = listOf(currentEvent("score"))
        assertFalse(RuleEngine.getInstance().analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    // --- parse error resilience ---

    @Test
    fun ruleWithInvalidConditionDoesNotCrashAndYieldsNoRequirements() {
        // Expression construction fails for malformed syntax; analyzer must skip it gracefully via getOrNull()
        val badRule = Rule("(((this is not valid", emptyList())
        val result = analyze(listOf(badRule))
        assertFalse(result.needsAllEvents)
        assertFalse(result.needsEnrollment)
        assertFalse(result.needsDataValues)
        assertFalse(result.needsAttributes)
        assertTrue(result.orgUnitGroups.isEmpty())
    }

    @Test
    fun ruleWithInvalidActionDataDoesNotCrashAndYieldsNoRequirements() {
        val badRule = Rule("true", listOf(RuleAction("(((invalid action", "DISPLAYTEXT")))
        val result = analyze(listOf(badRule))
        assertFalse(result.needsAllEvents)
        assertFalse(result.needsEnrollment)
        assertFalse(result.needsDataValues)
        assertFalse(result.needsAttributes)
        assertTrue(result.orgUnitGroups.isEmpty())
    }

    @Test
    fun mixOfValidAndInvalidRulesYieldsRequirementsFromValidRulesOnly() {
        val badRule = Rule("(((invalid", emptyList())
        val goodRule = rule("V{event_count} > 0")
        val result = analyze(listOf(badRule, goodRule))
        assertTrue(result.needsAllEvents)
    }

    // --- RuleVariableCalculatedValue ---

    @Test
    fun calculatedValueVariableDoesNotTriggerAnyRequirementFlag() {
        // RuleVariableCalculatedValue is derived from ASSIGN actions and needs no server-fetched context
        val calcVar = RuleVariableCalculatedValue(
            name = "calcVar",
            useCodeForOptionSet = false,
            options = emptyList(),
            field = "field_calc",
            fieldType = RuleValueType.NUMERIC,
        )
        val result = analyze(listOf(rule("#{calcVar} > 0")), listOf(calcVar))
        assertFalse(result.needsAllEvents)
        assertFalse(result.needsDataValues)
        assertFalse(result.needsAttributes)
        assertFalse(result.needsEnrollment)
    }
}
