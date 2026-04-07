package org.hisp.dhis.rules

import org.hisp.dhis.rules.api.RuleContextRequirements
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.engine.RuleEngineAnalyzer
import org.hisp.dhis.rules.models.Option
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleAction
import org.hisp.dhis.rules.models.RuleValueType
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent
import org.hisp.dhis.rules.models.RuleVariableNewestEvent
import org.hisp.dhis.rules.models.RuleVariableNewestStageEvent
import org.hisp.dhis.rules.models.RuleVariablePreviousEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuleEngineAnalyzerTest {

    private fun variable(
        name: String,
        field: String = "field_$name",
    ) = RuleVariableCurrentEvent(
        name = name,
        useCodeForOptionSet = false,
        options = emptyList<Option>(),
        field = field,
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

    private fun rule(condition: String, vararg actionData: String?): Rule {
        val actions = actionData.map { RuleAction(it, "DISPLAYTEXT") }
        return Rule(condition, actions)
    }

    // --- needsAllEvents = false cases ---

    @Test
    fun emptyRulesAndVariablesReturnsFalse() {
        val result = RuleEngineAnalyzer.analyzeContextRequirements(emptyList(), emptyList())
        assertEquals(RuleContextRequirements.NONE, result)
        assertFalse(result.needsAllEvents)
    }

    @Test
    fun unrelatedRulesAndVariablesReturnFalse() {
        val rules = listOf(rule("#{score} > 5", "#{score} + 1"))
        val variables = listOf(variable("score"))
        val result = RuleEngineAnalyzer.analyzeContextRequirements(rules, variables)
        assertFalse(result.needsAllEvents)
    }

    @Test
    fun currentEventVariableDoesNotTriggerAllEvents() {
        val rules = listOf(rule("#{current_val} > 0"))
        val variables = listOf(variable("current_val"))
        assertFalse(RuleEngineAnalyzer.analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    // --- needsAllEvents = true via env-var ---

    @Test
    fun eventCountEnvVarInConditionTriggersAllEvents() {
        val rules = listOf(rule("V{event_count} > 2"))
        assertTrue(RuleEngineAnalyzer.analyzeContextRequirements(rules, emptyList()).needsAllEvents)
    }

    @Test
    fun eventCountEnvVarInActionDataTriggersAllEvents() {
        val rules = listOf(rule("true", "V{event_count}"))
        assertTrue(RuleEngineAnalyzer.analyzeContextRequirements(rules, emptyList()).needsAllEvents)
    }

    @Test
    fun unrelatedEnvVarDoesNotTriggerAllEvents() {
        val rules = listOf(rule("V{enrollment_date} > '2020-01-01'"))
        assertFalse(RuleEngineAnalyzer.analyzeContextRequirements(rules, emptyList()).needsAllEvents)
    }

    // --- needsAllEvents = true via variable type ---

    @Test
    fun newestEventVariableTriggersAllEvents() {
        val rules = listOf(rule("#{weight} > 0"))
        val variables = listOf(newestEvent("weight"))
        assertTrue(RuleEngineAnalyzer.analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    @Test
    fun newestStageEventVariableTriggersAllEvents() {
        val rules = listOf(rule("#{bp} > 90"))
        val variables = listOf(newestStageEvent("bp"))
        assertTrue(RuleEngineAnalyzer.analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    @Test
    fun previousEventVariableTriggersAllEvents() {
        val rules = listOf(rule("#{prev_weight} < #{weight}"))
        val variables = listOf(previousEvent("prev_weight"), variable("weight"))
        assertTrue(RuleEngineAnalyzer.analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    @Test
    fun variableDefinedButNotReferencedDoesNotTrigger() {
        // The variable is a "needsAllEvents" type, but it isn't referenced in any rule
        val rules = listOf(rule("#{other} > 0"))
        val variables = listOf(newestEvent("unreferenced"), variable("other"))
        assertFalse(RuleEngineAnalyzer.analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    @Test
    fun newestEventVariableReferencedInActionDataTriggers() {
        val rules = listOf(rule("true", "#{latest_score}"))
        val variables = listOf(newestEvent("latest_score"))
        assertTrue(RuleEngineAnalyzer.analyzeContextRequirements(rules, variables).needsAllEvents)
    }

    // --- NONE constant ---

    @Test
    fun noneConstantHasNeedsAllEventsFalse() {
        assertFalse(RuleContextRequirements.NONE.needsAllEvents)
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
        val variables = listOf(variable("score"))
        assertFalse(RuleEngine.getInstance().analyzeContextRequirements(rules, variables).needsAllEvents)
    }
}
