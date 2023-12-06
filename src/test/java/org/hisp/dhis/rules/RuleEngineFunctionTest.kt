package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuleEngineFunctionTest {
    private val DATE_FORMAT = SimpleDateFormat(DATE_PATTERN, Locale.US)
    @Test
    fun evaluateFailingRule() {
        val enrollmentDate = Date()
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "2 + 2"
        )
        val failingRule: Rule = Rule("d2:daysBetween(V{enrollment_date},V{event_date}) < 0",
                listOf(ruleAction), "", ""
            )
        val validRule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(java.util.List.of(failingRule, validRule))
        val ruleEnrollment = RuleEnrollment.create(
            "test_enrollment",
            enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", "", listOf(),
            ""
        )
        val ruleEffects = ruleEngine.evaluate(ruleEnrollment).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("4", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateFailingRuleInMultipleContext() {
        val today = Date()
        val yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "2 + 2"
        )
        val failingRule: Rule = Rule("d2:daysBetween(V{enrollment_date},V{event_date}) < 0",
                java.util.List.of(ruleAction), "", ""
            )
        val ruleEnrollment = RuleEnrollment.create(
            "test_enrollment",
            today, today, RuleEnrollment.Status.ACTIVE, "", null, listOf(),
            ""
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "",
            null
        )
        val ruleNotFailingEvent = RuleEvent.create(
            "test_not_failing_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, yesterday, yesterday, "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "",
            null
        )
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(
            java.util.List.of(failingRule), ruleEnrollment,
            java.util.List.of(ruleEvent, ruleNotFailingEvent)
        )
        val ruleEffects = ruleEngine.evaluate()
        assertEquals(3, ruleEffects.size.toLong())
        assertTrue(getRuleEffectsByUid(ruleEffects, "test_event")!!.ruleEffects().isEmpty())
        assertFalse(getRuleEffectsByUid(ruleEffects, "test_not_failing_event")!!.ruleEffects().isEmpty())
        assertEquals("4", getRuleEffectsByUid(ruleEffects, "test_not_failing_event")!!.ruleEffects()[0].data())
        assertFalse(getRuleEffectsByUid(ruleEffects, "test_enrollment")!!.ruleEffects().isEmpty())
    }

    private fun getRuleEffectsByUid(ruleEffects: List<RuleEffects>, uid: String): RuleEffects? {
        for (ruleEffect in ruleEffects) {
            if (ruleEffect.trackerObjectUid() == uid) {
                return ruleEffect
            }
        }
        return null
    }

    @Test
    fun evaluateHasValueFunctionMustReturnTrueIfValueSpecified() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasValue(#{test_variable})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(
                    Date(), "test_program_stage", "test_data_element", "test_value"
                )
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("true", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun optionSetNameShouldBeUsed() {
        val option1 = Option("name1", "code1")
        val option2 = Option("name2", "code2")
        val options = java.util.List.of(option1, option2)
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "#{test_variable}"
        )
        val ruleVariable: RuleVariable = RuleVariableNewestEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT, USE_NAME_FOR_OPTION_SET, options
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(
                    Date(), "test_program_stage", "test_data_element", option1.code
                )
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(option1.name, ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun optionSetCodeShouldBeUsed() {
        val option1 = Option("name1", "code1")
        val option2 = Option("name2", "code2")
        val options = java.util.List.of(option1, option2)
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "#{test_variable}"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT, USE_CODE_FOR_OPTION_SET, options
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(
                    Date(), "test_program_stage", "test_data_element", option2.code
                )
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(option2.code, ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    @Deprecated("")
    fun evaluateHasValueFunctionWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasValue('test_variable')"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(
                    Date(), "test_program_stage", "test_data_element", "test_value"
                )
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("true", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateHasValueFunctionMustReturnTrueIfNoValueSpecified() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasValue(#{test_variable})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent.create(
            "test_variable", "test_data_element", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(),
            "test_program_stage_name", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("false", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateEnvironmentVariableProgramStageName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "V{program_stage_name}"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent
            .create("variable", "test_data_element", RuleValueType.TEXT, true, ArrayList())
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariable))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage_id",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(),
            "test_program_stage_name", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("test_program_stage_name", ruleEffects[0].data())
    }

    @Test
    fun evaluateDaysBetweenMustReturnCorrectDiff() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:daysBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariableOne, ruleVariableTwo))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue
                    .create(Date(), "test_program_stage", "test_data_element_two", "2017-02-01")
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("31", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateDaysBetweenWithSingleQuotedDateMustReturnCorrectDiff() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:daysBetween(#{test_var_one}, '2018-01-01')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariableOne, ruleVariableTwo))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue
                    .create(Date(), "test_program_stage", "test_data_element_two", "2017-02-01")
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("365", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2InOrgUnitGroup() {
        val members = listOf("location1", "location2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["OU_GROUP_ID"] = members
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:inOrgUnitGroup(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineContext
            .builder()
            .rules(java.util.List.of(rule))
            .ruleVariables(java.util.List.of(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "location1", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")
            ),
            "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2InOrgUnitGroupWithStringValue() {
        val members = listOf("location1", "location2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["OU_GROUP_ID"] = members
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:inOrgUnitGroup('OU_GROUP_ID')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineContext
            .builder()
            .rules(java.util.List.of(rule))
            .ruleVariables(java.util.List.of(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "location1", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")
            ),
            "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2HasUserRole() {
        val roles = listOf("role1", "role2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["USER"] = roles
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasUserRole(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineContext
            .builder()
            .rules(java.util.List.of(rule))
            .ruleVariables(java.util.List.of(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "location1", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "role1")
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2HasUserRoleWithStringValue() {
        val roles = listOf("role1", "role2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["USER"] = roles
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasUserRole('role1')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineContext
            .builder()
            .rules(java.util.List.of(rule))
            .ruleVariables(java.util.List.of(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "location1", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "role1")
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2AddDays() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:addDays(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, java.util.List.of(ruleVariableOne, ruleVariableTwo))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2")
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(ruleEffects.size.toLong(), 1)
        assertEquals(ruleEffects[0].ruleAction(), ruleAction)
        assertEquals(ruleEffects[0].data(), "2017-01-03")
        val ruleEvent2 = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2017-01-03"),
                RuleDataValue
                    .create(Date(), "test_program_stage", "test_data_element_two", "-2")
            ), "", null
        )
        val ruleEffects2 = ruleEngine.evaluate(ruleEvent2).call()
        assertEquals(1, ruleEffects2.size.toLong())
        assertEquals(ruleAction, ruleEffects2[0].ruleAction())
        assertEquals("2017-01-01", ruleEffects2[0].data())
    }

    @Test
    fun evaluateD2CountIfValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfValue(#{test_var_one}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(java.util.List.of(ruleEvent2, ruleEvent3))
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2CountIfValueWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfValue('test_var_one', 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(java.util.List.of(ruleEvent2, ruleEvent3))
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Count() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent4 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(java.util.List.of(ruleEvent2, ruleEvent3, ruleEvent4))
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateLogicalAnd() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule(
            "d2:hasValue(V{current_date}) && d2:count(#{test_var_one}) > 0",
            java.util.List.of(ruleAction), "", ""
        )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        ruleEngineBuilder.events(java.util.List.of(ruleEvent2))
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateLogicalOr() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule(
            "d2:hasValue(V{current_date}) || d2:count(#{test_var_one}) > 0",
            java.util.List.of(ruleAction), "", ""
        )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        ruleEngineBuilder.events(java.util.List.of(ruleEvent2))
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2CountWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:count('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent4 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(java.util.List.of(ruleEvent2, ruleEvent3, ruleEvent4))
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Round() {
        val ruleAction1: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction1), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2.6")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction1, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Modulus() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:modulus(#{test_var_one}, 2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2.6")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("0.6", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2SubString() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:substring(#{test_var_one}, 1, 3)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "ABCD")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("BC", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2WeeksBetween() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:weeksBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2018-02-01")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("4", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2MonthsBetween() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:monthsBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2018-09-01")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("8", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2YearsBetween() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:yearsBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:yearsBetween('2016-01-01', '2018-09-01') == 2", java.util.List.of(ruleAction), "",
                ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "2016-01-01"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "2018-09-01")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Zpvc() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:zpvc( '1', '0', '-1' )"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableThree: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_three", "test_data_element_two", RuleValueType.NUMERIC, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Zing() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:zing( '-1' )"
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, listOf())
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("0", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Oizp() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:oizp( '0' )"
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, listOf())
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, listOf(), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("1", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfZeroPos() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "0")
            ), "", null
        )
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "1")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "-3")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2CountIfZeroPosWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "0")
            ), "", null
        )
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "1")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "-3")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Left() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:left(#{test_var_one}, 4)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("yyyy", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Right() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:right(#{test_var_one}, 2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("dd", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Concatenate() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:concatenate(#{test_var_one}, '+days')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "weeks")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("weeks+days", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2ValidatePattern() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:validatePattern(#{test_var_one}, '.*555.*')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "44455545454")
            ),
            "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(ruleEffects.size.toLong(), 1)
        assertEquals(ruleEffects[0].ruleAction(), ruleAction)
        assertEquals("true", ruleEffects[0].data())
        val ruleEvent2 = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "444887777")
            ), "", null
        )
        val ruleEffects2 = ruleEngineBuilder.build().evaluate(ruleEvent2).call()
        assertEquals(1, ruleEffects2.size.toLong())
        assertEquals(ruleAction, ruleEffects2[0].ruleAction())
        assertEquals("false", ruleEffects2[0].data())
    }

    @Test
    fun evaluateD2Length() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:length(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "testString")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("10", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Split() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:split(#{test_var_one},'-',2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue
                    .create(Date(), "test_program_stage", "test_data_element_one", "test-String-for-split")
            ),
            "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("for", ruleEffects[0].data())
    }

    @Test
    fun evaluateNestedFunctionCalls() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:floor(#{test_var_one} + d2:ceil(#{test_var_three})) " +
                    "/ 5 * d2:ceil(#{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableThree: RuleVariable = RuleVariableCurrentEvent.create(
            "test_var_three", "test_data_element_three", RuleValueType.NUMERIC, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo, ruleVariableThree)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "19.9"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "0.9"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_three", "10.6")
            ), "", null
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals("6", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreWFA() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:zScoreWFA(1,#{test_var_one},#{test_var_two}) == 0", java.util.List.of(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "4.5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreHFAGirl() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:zScoreHFA(12,#{test_var_one},#{test_var_two}) == -3", java.util.List.of(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "66.3"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "1")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreHFABoy() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:zScoreHFA(10,#{test_var_one},#{test_var_two}) == -2", java.util.List.of(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "68.7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreWFHBoy() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:zScoreWFH(52,#{test_var_one},A{test_var_two}) < 2", java.util.List.of(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "3"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreWFHGirl() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:zScoreWFH(81.5,9.6,'female') == 2"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:zScoreWFH(81.5,#{test_var_one},#{test_var_two}) == 2", java.util.List.of(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent.create(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "12.5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "1")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2MaxValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:maxValue(#{test_var_one}) == 8.0", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    @Deprecated("")
    fun evaluateD2MaxValueWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("d2:maxValue('test_var_one') == 8.0", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun testMinValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:minValue(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("5", ruleEffects[0].data())
    }

    @Test
    fun testMinValueWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:minValue('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC, true, ArrayList()
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", java.util.List.of(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(
            rule,
            java.util.List.of(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, Date(), Date(), "", null, java.util.List.of(
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue.create(Date(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("5", ruleEffects[0].data())
    }

    @Test
    fun evaluateLastEventDate() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val yesterday = cal.time
        cal.add(Calendar.DATE, -1)
        val dayBeforeYesterday = cal.time
        cal.add(Calendar.DATE, 4)
        val dayAfterTomorrow = cal.time
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:lastEventDate('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent.create(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT, true, ArrayList()
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "test_rule", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngineBuilder(rule, java.util.List.of(ruleVariableOne))
        val ruleEvent1 = RuleEvent.create(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, dayBeforeYesterday, Date(), "", null, java.util.List.of(
                RuleDataValue.create(dayBeforeYesterday, "test_program_stage1", "test_data_element_one", "value1")
            ),
            "", null
        )
        val ruleEvent2 = RuleEvent.create(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, yesterday, Date(), "", null, java.util.List.of(
                RuleDataValue.create(yesterday, "test_program_stage2", "test_data_element_one", "value2")
            ), "", null
        )
        val ruleEvent3 = RuleEvent.create(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, dayAfterTomorrow, dayAfterTomorrow, "", null, java.util.List.of(
                RuleDataValue.create(dayAfterTomorrow, "test_program_stage3", "test_data_element_one", "value3")
            ),
            "", null
        )
        val ruleEffects = ruleEngineBuilder.events(java.util.List.of(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size.toLong())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals(DATE_FORMAT.format(dayAfterTomorrow), ruleEffects[0].data())
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private const val USE_CODE_FOR_OPTION_SET = true
        private const val USE_NAME_FOR_OPTION_SET = !USE_CODE_FOR_OPTION_SET
    }
}
