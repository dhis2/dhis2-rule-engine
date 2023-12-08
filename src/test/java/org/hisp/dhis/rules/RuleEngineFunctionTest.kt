package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
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
            RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "2 + 2"
        )
        val failingRule = Rule("d2:daysBetween(V{enrollment_date},V{event_date}) < 0",
                listOf(ruleAction), "", ""
            )
        val validRule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(listOf(failingRule, validRule))
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE,
            "", "", listOf(),
        )
        val ruleEffects = ruleEngine.evaluate(ruleEnrollment).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("4", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateFailingRuleInMultipleContext() {
        val today = Date()
        val yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
                    RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "2 + 2"
        )
        val failingRule = Rule("d2:daysBetween(V{enrollment_date},V{event_date}) < 0",
                listOf(ruleAction), "", ""
            )
        val ruleEnrollment = RuleEnrollment(
            "test_enrollment", "",
            today, today, RuleEnrollment.Status.ACTIVE, "", "", listOf()
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleNotFailingEvent = RuleEvent(
            "test_not_failing_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, yesterday, yesterday,  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(
            listOf(failingRule), ruleEnrollment,
            listOf(ruleEvent, ruleNotFailingEvent)
        )
        val ruleEffects = ruleEngine.evaluate()
        assertEquals(3, ruleEffects.size)
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
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:hasValue(#{test_variable})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            "test_variable", true, emptyList(),"test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "test_value")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("true", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun optionSetNameShouldBeUsed() {
        val option1 = Option("name1", "code1")
        val option2 = Option("name2", "code2")
        val options = listOf(option1, option2)
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "#{test_variable}"
        )
        val ruleVariable: RuleVariable = RuleVariableNewestEvent("test_variable",
            USE_NAME_FOR_OPTION_SET, options, "test_data_element", RuleValueType.TEXT)
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element", option1.code)
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(option1.name, ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun optionSetCodeShouldBeUsed() {
        val option1 = Option("name1", "code1")
        val option2 = Option("name2", "code2")
        val options = listOf(option1, option2)
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "#{test_variable}"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            "test_variable", USE_CODE_FOR_OPTION_SET, options, "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element", option2.code)
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(option2.code, ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    @Deprecated("")
    fun evaluateHasValueFunctionWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:hasValue('test_variable')"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            "test_variable", true, ArrayList(),"test_data_element", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element", "test_value")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("true", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateHasValueFunctionMustReturnTrueIfNoValueSpecified() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:hasValue(#{test_variable})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            "test_variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("false", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateEnvironmentVariableProgramStageName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "V{program_stage_name}"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent("variable", true, ArrayList(), "test_data_element", RuleValueType.TEXT)
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage_id", "test_program_stage_name",
            RuleEvent.Status.ACTIVE, Date(), Date(),  null, "",
            null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("test_program_stage_name", ruleEffects[0].data())
    }

    @Test
    fun evaluateDaysBetweenMustReturnCorrectDiff() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:daysBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "2017-02-01")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("31", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateDaysBetweenWithSingleQuotedDateMustReturnCorrectDiff() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:daysBetween(#{test_var_one}, '2018-01-01')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "2017-02-01")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("365", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2InOrgUnitGroup() {
        val members = listOf("location1", "location2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["OU_GROUP_ID"] = members
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:inOrgUnitGroup(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngine(RuleEngineContext(listOf(rule),listOf(ruleVariableOne),supplementaryData))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "location1", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2InOrgUnitGroupWithStringValue() {
        val members = listOf("location1", "location2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["OU_GROUP_ID"] = members
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:inOrgUnitGroup('OU_GROUP_ID')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngine(RuleEngineContext(listOf(rule),listOf(ruleVariableOne),supplementaryData))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "location1", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2HasUserRole() {
        val roles = listOf("role1", "role2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["USER"] = roles
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:hasUserRole(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngine(RuleEngineContext(listOf(rule),listOf(ruleVariableOne),supplementaryData))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "location1", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "role1")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2HasUserRoleWithStringValue() {
        val roles = listOf("role1", "role2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["USER"] = roles
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:hasUserRole('role1')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngine(RuleEngineContext(listOf(rule),listOf(ruleVariableOne),supplementaryData))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "location1", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "role1")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("true", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2AddDays() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:addDays(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "2")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(ruleEffects.size, 1)
        assertEquals(ruleEffects[0].ruleAction(), ruleAction)
        assertEquals(ruleEffects[0].data(), "2017-01-03")
        val ruleEvent2 = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2017-01-03"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "-2")
            )
        )
        val ruleEffects2 = ruleEngine.evaluate(ruleEvent2).call()
        assertEquals(1, ruleEffects2.size)
        assertEquals(ruleAction, ruleEffects2[0].ruleAction())
        assertEquals("2017-01-01", ruleEffects2[0].data())
    }

    @Test
    fun evaluateD2CountIfValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:countIfValue(#{test_var_one}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent2, ruleEvent3)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2CountIfValueWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:countIfValue('test_var_one', 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent2, ruleEvent3)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Count() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent4 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "condition")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent2, ruleEvent3, ruleEvent4)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateLogicalAnd() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(
            "d2:hasValue(V{current_date}) && d2:count(#{test_var_one}) > 0",
            listOf(ruleAction), "", ""
        )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent2)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateLogicalOr() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(
            "d2:hasValue(V{current_date}) || d2:count(#{test_var_one}) > 0",
            listOf(ruleAction), "", ""
        )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent2)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2CountWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:count('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent4 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "condition")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent2, ruleEvent3, ruleEvent4)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Round() {
        val ruleAction1: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:round(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule("true", listOf(ruleAction1), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2.6")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction1, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Modulus() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:modulus(#{test_var_one}, 2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2.6")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("0.6", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2SubString() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:substring(#{test_var_one}, 1, 3)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "ABCD")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("BC", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2WeeksBetween() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:weeksBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "2018-02-01")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("4", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2MonthsBetween() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:monthsBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "2018-09-01")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("8", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2YearsBetween() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:yearsBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:yearsBetween('2016-01-01', '2018-09-01') == 2", listOf(ruleAction), "",
                ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "2016-01-01"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "2018-09-01")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Zpvc() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:zpvc( '1', '0', '-1' )"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.NUMERIC
        )
        val ruleVariableThree: RuleVariable = RuleVariableNewestEvent(
            "test_var_three", true, ArrayList(), "test_data_element_two", RuleValueType.NUMERIC
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf()
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Zing() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:zing( '-1' )"
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf())
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf()
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("0", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Oizp() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:oizp( '0' )"
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf())
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf())
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("1", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfZeroPos() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:countIfZeroPos(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "0")
            )
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "1")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event1", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "-3")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    @Deprecated("")
    fun evaluateD2CountIfZeroPosWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:countIfZeroPos('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "0")
            )
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "1")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event1", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "-3")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Left() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:left(#{test_var_one}, 4)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("yyyy", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Right() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:right(#{test_var_one}, 2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("dd", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Concatenate() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:concatenate(#{test_var_one}, '+days')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "weeks")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("weeks+days", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2ValidatePattern() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:validatePattern(#{test_var_one}, '.*555.*')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "44455545454")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(ruleEffects.size, 1)
        assertEquals(ruleEffects[0].ruleAction(), ruleAction)
        assertEquals("true", ruleEffects[0].data())
        val ruleEvent2 = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "444887777")
            )
        )
        val ruleEffects2 = ruleEngineBuilder.evaluate(ruleEvent2).call()
        assertEquals(1, ruleEffects2.size)
        assertEquals(ruleAction, ruleEffects2[0].ruleAction())
        assertEquals("false", ruleEffects2[0].data())
    }

    @Test
    fun evaluateD2Length() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:length(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "testString")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("10", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2Split() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:split(#{test_var_one},'-',2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "test-String-for-split")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("for", ruleEffects[0].data())
    }

    @Test
    fun evaluateNestedFunctionCalls() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:floor(#{test_var_one} + d2:ceil(#{test_var_three})) " +
                    "/ 5 * d2:ceil(#{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableCurrentEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableCurrentEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.NUMERIC
        )
        val ruleVariableThree: RuleVariable = RuleVariableCurrentEvent(
            "test_var_three", true, ArrayList(), "test_data_element_three", RuleValueType.NUMERIC
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "19.9"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "0.9"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_three", "10.6")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("6", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreWFA() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:zScoreWFA(1,#{test_var_one},#{test_var_two}) == 0", listOf(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "4.5"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreHFAGirl() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:zScoreHFA(12,#{test_var_one},#{test_var_two}) == -3", listOf(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "66.3"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "1")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreHFABoy() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:zScoreHFA(10,#{test_var_one},#{test_var_two}) == -2", listOf(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "68.7"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreWFHBoy() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:zScoreWFH(52,#{test_var_one},A{test_var_two}) < 2", listOf(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "3"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2ZScoreWFHGirl() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:zScoreWFH(81.5,9.6,'female') == 2"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:zScoreWFH(81.5,#{test_var_one},#{test_var_two}) == 2", listOf(ruleAction),
                "", ""
            )
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "12.5"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "1")
            )
        )
        val ruleEffects = ruleEngineBuilder.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2MaxValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:maxValue(#{test_var_one}) == 8.0", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    @Deprecated("")
    fun evaluateD2MaxValueWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("d2:maxValue('test_var_one') == 8.0", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun testMinValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:minValue(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("5", ruleEffects[0].data())
    }

    @Test
    fun testMinValueWithStringValue() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:minValue('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
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
        val ruleAction: RuleAction = RuleActionText.createForFeedback(RuleActionText.Type.DISPLAYTEXT,
            "test_action_content", "d2:lastEventDate('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("true", listOf(ruleAction), "test_rule", "")
        val ruleEngineBuilder = RuleEngineTestUtils.getRuleEngine(rule, listOf(ruleVariableOne))
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1","",RuleEvent.Status.ACTIVE, dayBeforeYesterday, Date(), 
            null, "", null, listOf(
                RuleDataValue(dayBeforeYesterday, "test_program_stage1", "test_data_element_one", "value1")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2","",
            RuleEvent.Status.ACTIVE, yesterday, Date (), null, "", null, listOf(
                RuleDataValue(yesterday, "test_program_stage2", "test_data_element_one", "value2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3","",
            RuleEvent.Status.ACTIVE, dayAfterTomorrow, dayAfterTomorrow, null, "", null, listOf(
                RuleDataValue(dayAfterTomorrow, "test_program_stage3", "test_data_element_one", "value3")
            )
        )
        val ruleEffects = ruleEngineBuilder.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals(DATE_FORMAT.format(dayAfterTomorrow), ruleEffects[0].data())
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private const val USE_CODE_FOR_OPTION_SET = true
        private const val USE_NAME_FOR_OPTION_SET = !USE_CODE_FOR_OPTION_SET
    }
}
