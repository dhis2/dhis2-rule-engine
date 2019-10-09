package org.hisp.dhis.rules

import org.assertj.core.api.Assertions.assertThat
import org.hisp.dhis.rules.models.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class RuleEngineValueTypesTests {

    @Test
    @Throws(Exception::class)
    fun booleanVariableWithoutValueMustFallbackToDefaultBooleanValue() {
        val ruleAction = RuleActionDisplayKeyValuePair
                .createForFeedback("test_action_content", "#{test_variable}")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")
        val ruleVariable = RuleVariableCurrentEvent
                .create("test_variable", "test_data_element", RuleValueType.BOOLEAN)

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("false")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun numericVariableWithoutValueMustFallbackToDefaultNumericValue() {
        val ruleAction = RuleActionDisplayKeyValuePair
                .createForFeedback("test_action_content", "#{test_variable}")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")
        val ruleVariable = RuleVariableCurrentEvent
                .create("test_variable", "test_data_element", RuleValueType.NUMERIC)

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("0.0")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun textVariableWithoutValueMustFallbackToDefaultTextValue() {
        val ruleAction = RuleActionDisplayKeyValuePair
                .createForFeedback("test_action_content", "#{test_variable}")
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")
        val ruleVariable = RuleVariableCurrentEvent
                .create("test_variable", "test_data_element", RuleValueType.TEXT)

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    private fun getRuleEngine(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngine {
        return RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(listOf(rule))
                .ruleVariables(ruleVariables)
                .calculatedValueMap(hashMapOf())
                .supplementaryData(hashMapOf())
                .constantsValue(hashMapOf())
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
                .build()
    }
}
