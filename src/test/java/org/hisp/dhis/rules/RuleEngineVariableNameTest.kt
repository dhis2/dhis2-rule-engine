package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RuleEngineVariableNameTest {
    @Test
    fun evaluateD2Round() {
        val ruleAction1: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + UID01 + "})"
        )
        val ruleAction2: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + VARIABLE_NAME + "})"
        )
        val ruleAction3: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + UID0 + "})"
        )
        val ruleAction4: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + UID0WILD + "})"
        )
        val ruleAction5: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + UID01WILD + "})"
        )
        val ruleAction6: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + UID012 + "})"
        )
        val ruleAction7: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(#{" + UID0WILD2 + "})"
        )
        val ruleAction8: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(A{" + UID0 + "})"
        )
        val ruleAction9: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(A{" + UID01 + "})"
        )
        val ruleAction10: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:round(A{" + VARIABLE_NAME + "})"
        )
        val ruleVariable1: RuleVariable = RuleVariableNewestEvent(
            UID01, true, ArrayList(), "test_data_element1", RuleValueType.NUMERIC
        )
        val ruleVariable2: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element2", RuleValueType.NUMERIC
        )
        val ruleVariable3: RuleVariable = RuleVariableNewestEvent(
            UID0, true, ArrayList(), "test_data_element3", RuleValueType.NUMERIC
        )
        val ruleVariable4: RuleVariable = RuleVariableNewestEvent(
            UID0WILD, true, ArrayList(), "test_data_element4", RuleValueType.NUMERIC
        )
        val ruleVariable5: RuleVariable = RuleVariableNewestEvent(
            UID01WILD, true, ArrayList(), "test_data_element5", RuleValueType.NUMERIC
        )
        val ruleVariable6: RuleVariable = RuleVariableNewestEvent(
            UID012, true, ArrayList(), "test_data_element6", RuleValueType.NUMERIC
        )
        val ruleVariable7: RuleVariable = RuleVariableNewestEvent(
            UID0WILD2, true, ArrayList(), "test_data_element7", RuleValueType.NUMERIC
        )
        val actions = listOf(
            ruleAction1, ruleAction2, ruleAction3, ruleAction4, ruleAction5, ruleAction6, ruleAction7,
            ruleAction8, ruleAction9, ruleAction10
        )
        val rule: Rule = Rule(
            "true",
            actions, "", ""
        )
        val ruleVariables = listOf(
            ruleVariable1, ruleVariable2, ruleVariable3, ruleVariable4, ruleVariable5, ruleVariable6,
            ruleVariable7
        )
        val ruleEngine = getRuleEngine(
            rule,
            ruleVariables
        )
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date(), null, "", null, listOf(
                RuleDataValue(Date(), "test_program_stage", "test_data_element1", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element2", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element3", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element4", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element5", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element6", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element7", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element8", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element9", "2.6"),
                RuleDataValue(Date(), "test_program_stage", "test_data_element10", "2.6")
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(10, ruleEffects.size)
        assertEquals(ruleAction1, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
        assertEquals(ruleAction2, ruleEffects[1].ruleAction())
        assertEquals("3", ruleEffects[1].data())
        assertEquals(ruleAction3, ruleEffects[2].ruleAction())
        assertEquals("3", ruleEffects[2].data())
        assertEquals(ruleAction4, ruleEffects[3].ruleAction())
        assertEquals("3", ruleEffects[3].data())
        assertEquals(ruleAction5, ruleEffects[4].ruleAction())
        assertEquals("3", ruleEffects[4].data())
        assertEquals(ruleAction6, ruleEffects[5].ruleAction())
        assertEquals("3", ruleEffects[5].data())
        assertEquals(ruleAction7, ruleEffects[6].ruleAction())
        assertEquals("3", ruleEffects[6].data())
        assertEquals(ruleAction8, ruleEffects[7].ruleAction())
        assertEquals("3", ruleEffects[7].data())
        assertEquals(ruleAction9, ruleEffects[8].ruleAction())
        assertEquals("3", ruleEffects[8].data())
        assertEquals(ruleAction10, ruleEffects[9].ruleAction())
        assertEquals("3", ruleEffects[9].data())
    }

    @Test
    fun evaluateHasValueFunctionMustReturnTrueIfVariableIsComposedUIDs() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasValue(#{" + UID01 + "})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            UID01, true, ArrayList(), "test_data_element", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(
                    Date(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("true", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateHasValueFunctionMustReturnTrueIfVariableIsVariableName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:hasValue(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, Date(), Date (), null, "", null, listOf(
                RuleDataValue(
                    Date(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects = ruleEngine.evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals("true", ruleEffects[0].data())
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2CountIfValueIsVariableName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfValue(#{" + VARIABLE_NAME + "}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne))
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent2, ruleEvent3)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfValueIsComposedUid() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfValue(#{" + UID01 + "}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne))
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent2, ruleEvent3)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfVariableName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:count(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne))
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent2, ruleEvent3, ruleEvent4)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfVariableNameIfComposedUid() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:count(#{" + UID01 + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, true, ArrayList(), "test_data_element_one", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne))
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent2, ruleEvent3, ruleEvent4)).evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("3", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfZeroPosIfVariableName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne))
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2CountIfZeroPosIfComposedUid() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{" + UID01 + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(rule, listOf(ruleVariableOne))
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("2", ruleEffects[0].data())
    }

    @Test
    fun evaluateD2MaxValueIfVariableName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule = Rule("d2:maxValue(#{" + VARIABLE_NAME + "}) == 8.0", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun evaluateD2MaxValueIfComposedUid() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule = Rule("d2:maxValue(#{" + UID01 + "}) == 8.0", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
    }

    @Test
    fun testMinValueIfVariableName() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:minValue(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("5", ruleEffects[0].data())
    }

    @Test
    fun testMinValueIfComposedUid() {
        val ruleAction: RuleAction = RuleActionText.createForFeedback(
            "test_action_content", "d2:minValue(#{" + UID01 + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, true, ArrayList(), "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", true, ArrayList(), "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule = Rule("true", listOf(ruleAction), "", "")
        val ruleEngine = getRuleEngine(
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
        val ruleEffects = ruleEngine.copy(events = listOf(ruleEvent1, ruleEvent2))
            .evaluate(ruleEvent3).call()
        assertEquals(1, ruleEffects.size)
        assertEquals(ruleAction, ruleEffects[0].ruleAction())
        assertEquals("5", ruleEffects[0].data())
    }

    private fun getRuleEngine(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngine {
        return RuleEngine(RuleEngineContext(listOf(rule),ruleVariables))
    }

    companion object {
        private const val UID0 = "Aabcde12345"
        private const val UID0WILD = "Babcde12345.*"
        private const val UID01 = "Cabcde12345.Dabcde12345"
        private const val UID01WILD = "Eabcde12345.Fabcde12345.*"
        private const val UID012 = "Gabcde12345.Habcde12345.Iabcde12345"
        private const val UID0WILD2 = "Labcde12345.*.Mabcde12345"
        private const val VARIABLE_NAME = "Variable.name_3_4-1"
    }
}
