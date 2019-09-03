package org.hisp.dhis.rules

import com.google.common.truth.Truth.assertThat
import org.hisp.dhis.rules.models.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.*

@RunWith(JUnit4::class)
class RuleEngineFunctionTests {

    private val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)

    @Test
    @Throws(Exception::class)
    fun evaluateHasValueFunctionMustReturnTrueIfValueSpecified() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:hasValue('test_variable')")
        val ruleVariable = RuleVariableCurrentEvent.create(
                "test_variable", "test_data_element", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(RuleDataValue.create(
                Date(), "test_program_stage", "test_data_element", "test_value")), "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("true")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateHasValueFunctionMustReturnTrueIfNoValueSpecified() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:hasValue('test_variable')")
        val ruleVariable = RuleVariableCurrentEvent.create(
                "test_variable", "test_data_element", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "test_program_stage_name")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("false")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateEnvironmentVariableProgramStageName() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "V{program_stage_name}")
        val ruleVariable = RuleVariableCurrentEvent.create("variable", "test_data_element", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage_id",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "test_program_stage_name")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertThat(ruleEffects[0].data).isEqualTo("test_program_stage_name")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateDaysBetweenMustReturnCorrectDiff() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:daysBetween(#{test_var_one}, #{test_var_two})")
        val ruleVariableOne = RuleVariableCurrentEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableCurrentEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2017-02-01")
        ),
                "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("31")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2InOrgUnitGroup() {
        val members = listOf("location1", "location2")

        val supplementaryData = HashMap<String, List<String>>()
        supplementaryData["OU_GROUP_ID"] = members

        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:inOrgUnitGroup(#{test_var_one})")
        val ruleVariableOne = RuleVariableCurrentEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(listOf(rule))
                .ruleVariables(listOf(ruleVariableOne))
                .supplementaryData(supplementaryData)
                .calculatedValueMap(HashMap())
                .constantsValue(HashMap())
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
                .build()

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "location1", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")), "")

        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertThat(ruleEffects[0].data).isEqualTo("true")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2AddDays() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:addDays(#{test_var_one}, #{test_var_two})")
        val ruleVariableOne = RuleVariableCurrentEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableCurrentEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2")), "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertThat(ruleEffects[0].data).isEqualTo("2017-01-03")

        val ruleEvent2 = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-03"),
                RuleDataValue
                        .create(Date(), "test_program_stage", "test_data_element_two", "-2")), "")
        val ruleEffects2 = ruleEngine.evaluate(ruleEvent2).call()

        assertThat(ruleEffects2.size).isEqualTo(1)
        assertThat(ruleEffects2[0].ruleAction).isEqualTo(ruleAction)
        assertThat(ruleEffects2[0].data).isEqualTo("2017-01-01")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfValue() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:countIfValue('test_var_one', 'condition')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")), "")
        val ruleEvent2 = RuleEvent.create("test_event2", "test_program_stage2",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")), "")
        val ruleEvent3 = RuleEvent.create("test_event3", "test_program_stage3",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")), "")

        ruleEngineBuilder.events(listOf(ruleEvent2, ruleEvent3))

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertTrue(ruleEffects[0].data == "2")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Count() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:count('test_var_one')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")), "")
        val ruleEvent2 = RuleEvent.create("test_event2", "test_program_stage2",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")), "")
        val ruleEvent3 = RuleEvent.create("test_event3", "test_program_stage3",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")), "")

        val ruleEvent4 = RuleEvent.create("test_event3", "test_program_stage3",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "condition")), "")

        ruleEngineBuilder.events(listOf(ruleEvent2, ruleEvent3, ruleEvent4))

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("3", ruleEffects[0].data)
    }


    @Test
    @Throws(Exception::class)
    fun evaluateD2Round() {
        val ruleAction1 = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:round(#{test_var_one})")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)

        val rule = Rule.create(null, null, "true", listOf(ruleAction1), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2.6")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction1)
        assertEquals("3", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Modulus() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:modulus(#{test_var_one}, 2)")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2.6")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("0.6", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2SubString() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:substring(#{test_var_one}, 1, 3)")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "ABCD")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("BC", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2MonthsBetween() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:monthsBetween(#{test_var_one}, #{test_var_two})")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2018-09-01")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("8", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2YearsBetween() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:yearsBetween(#{test_var_one}, #{test_var_two})")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)
        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)
        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2016-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2018-09-01")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Zpvc() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:zpvc( '1', '0', '-1' )")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)
        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.NUMERIC)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Zing() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:zing( '-1' )")

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf())

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("0", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Oizp() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:oizp( '0' )")

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf())

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("1", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfZeroPos() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:countIfZeroPos('test_var_one')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "0")), "")

        val ruleEvent1 = RuleEvent.create("test_event1", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "1")), "")

        val ruleEvent2 = RuleEvent.create("test_event1", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "-3")), "")

        val ruleEffects = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Left() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:left(#{test_var_one}, 4)")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("yyyy", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Right() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:right(#{test_var_one}, 2)")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("dd", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Concatenate() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:concatenate(#{test_var_one}, '+days')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "weeks")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("weeks+days", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ValidatePattern() {
        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:validatePattern(#{test_var_one}, '.*555.*')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "44455545454")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("true", ruleEffects[0].data)

        val ruleEvent2 = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "444887777")), "")

        val ruleEffects2 = ruleEngineBuilder.build().evaluate(ruleEvent2).call()

        assertThat(ruleEffects2.size).isEqualTo(1)
        assertThat(ruleEffects2[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("false", ruleEffects2[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Length() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:length(#{test_var_one})")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "testString")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("10", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Split() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:split(#{test_var_one},'-',2)")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "test-String-for-split")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("for", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateNestedFunctionCalls() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:floor(#{test_var_one} + d2:ceil(#{test_var_three})) " + "/ 5 * d2:ceil(#{test_var_two})")

        val ruleVariableOne = RuleVariableCurrentEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)
        val ruleVariableTwo = RuleVariableCurrentEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.NUMERIC)
        val ruleVariableThree = RuleVariableCurrentEvent.create(
                "test_var_three", "test_data_element_three", RuleValueType.NUMERIC)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "19.9"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "0.9"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_three", "10.6")), "")
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].data).isEqualTo("6")
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }


    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreWFA() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "true")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "d2:zScoreWFA(1,#{test_var_one},#{test_var_two}) == 0", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "4.5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreHFAGirl() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "true")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "d2:zScoreHFA(12,#{test_var_one},#{test_var_two}) == -3", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "66.3"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "1")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreHFABoy() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "true")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "d2:zScoreHFA(10,#{test_var_one},#{test_var_two}) == -2", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "68.7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreWFHBoy() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "true")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "d2:zScoreWFH(55,#{test_var_one},#{test_var_two}) == -2", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "3.8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreWFHGirl() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "true")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "d2:zScoreWFH(81.5,#{test_var_one},#{test_var_two}) == 2", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent = RuleEvent.create("test_event", "test_program_stage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "12.5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "1")), "")

        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }


    @Test
    @Throws(Exception::class)
    fun evaluateD2MaxValue() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "true")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "d2:maxValue('test_var_one') == 8.0", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent1 = RuleEvent.create("test_event1", "test_program_stage1",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEvent2 = RuleEvent.create("test_event2", "test_program_stage2",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEvent3 = RuleEvent.create("test_event3", "test_program_stage3",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")


        val ruleEffects = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build().evaluate(ruleEvent3).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testMinValue() {
        val ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
                "test_action_content", "d2:minValue('test_var_one')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.NUMERIC)

        val ruleVariableTwo = RuleVariableNewestEvent.create(
                "test_var_two", "test_data_element_two", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne, ruleVariableTwo))

        val ruleEvent1 = RuleEvent.create("test_event1", "test_program_stage1",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEvent2 = RuleEvent.create("test_event2", "test_program_stage2",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")

        val ruleEvent3 = RuleEvent.create("test_event3", "test_program_stage3",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")), "")


        val ruleEffects = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build().evaluate(ruleEvent3).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertThat(ruleEffects[0].data).isEqualTo("5.0")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateLastEventDate() {
        val cal = Calendar.getInstance()

        cal.add(Calendar.DATE, -1)
        val yesterday = cal.time
        cal.add(Calendar.DATE, -1)
        val dayBeforeYesterday = cal.time
        cal.add(Calendar.DATE, 4)
        val dayAfterTomorrow = cal.time

        val ruleAction = RuleActionDisplayText.createForFeedback(
                "test_action_content", "d2:lastEventDate('test_var_one')")
        val ruleVariableOne = RuleVariableNewestEvent.create(
                "test_var_one", "test_data_element_one", RuleValueType.TEXT)

        val rule = Rule.create(null, null, "true", listOf(ruleAction), "test_rule")

        val ruleEngineBuilder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))

        val ruleEvent1 = RuleEvent.create("test_event1", "test_program_stage1",
                RuleEvent.Status.ACTIVE, dayBeforeYesterday, Date(), "", null, listOf(
                RuleDataValue.create(dayBeforeYesterday, "test_program_stage1", "test_data_element_one", "value1")), "")

        val ruleEvent2 = RuleEvent.create("test_event2", "test_program_stage2",
                RuleEvent.Status.ACTIVE, yesterday, Date(), "", null, listOf(
                RuleDataValue.create(yesterday, "test_program_stage2", "test_data_element_one", "value2")), "")

        val ruleEvent3 = RuleEvent.create("test_event3", "test_program_stage3",
                RuleEvent.Status.ACTIVE, dayAfterTomorrow, dayAfterTomorrow, "", null, listOf(
                RuleDataValue.create(dayAfterTomorrow, "test_program_stage3", "test_data_element_one", "value3")), "")


        val ruleEffects = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build().evaluate(ruleEvent3).call()

        assertThat(ruleEffects.size).isEqualTo(1)
        assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        assertEquals(dateFormat.format(yesterday), ruleEffects[0].data)
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

    private fun getRuleEngineBuilder(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngine.Builder {
        return RuleEngineContext
                .builder(ExpressionEvaluator())
                .rules(listOf(rule))
                .ruleVariables(ruleVariables)
                .calculatedValueMap(hashMapOf())
                .supplementaryData(hashMapOf())
                .constantsValue(hashMapOf())
                .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
