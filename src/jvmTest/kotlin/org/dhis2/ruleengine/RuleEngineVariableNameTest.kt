package org.dhis2.ruleengine

import org.assertj.core.api.Assertions
import org.dhis2.ruleengine.RuleEngineTestUtils.currentDate
import org.dhis2.ruleengine.RuleEngineTestUtils.getRuleEngine
import org.dhis2.ruleengine.models.*
import org.dhis2.ruleengine.models.RuleAction.DisplayKeyValuePair
import org.dhis2.ruleengine.models.RuleVariable.RuleVariableCurrentEvent
import org.dhis2.ruleengine.models.RuleVariable.RuleVariableNewestEvent
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class RuleEngineVariableNameTest {
    @Test
    @Throws(Exception::class)
    fun evaluateD2Round() {
        val ruleAction1: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + UID01 + "})"
        )
        val ruleAction2: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + VARIABLE_NAME + "})"
        )
        val ruleAction3: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + UID0 + "})"
        )
        val ruleAction4: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + UID0WILD + "})"
        )
        val ruleAction5: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + UID01WILD + "})"
        )
        val ruleAction6: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + UID012 + "})"
        )
        val ruleAction7: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(#{" + UID0WILD2 + "})"
        )
        val ruleAction8: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(A{" + UID0 + "})"
        )
        val ruleAction9: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(A{" + UID01 + "})"
        )
        val ruleAction10: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:round(A{" + VARIABLE_NAME + "})"
        )
        val ruleVariable1: RuleVariable = RuleVariableNewestEvent(
            UID01, "test_data_element1", RuleValueType.NUMERIC
        )
        val ruleVariable2: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, "test_data_element2", RuleValueType.NUMERIC
        )
        val ruleVariable3: RuleVariable = RuleVariableNewestEvent(
            UID0, "test_data_element3", RuleValueType.NUMERIC
        )
        val ruleVariable4: RuleVariable = RuleVariableNewestEvent(
            UID0WILD, "test_data_element4", RuleValueType.NUMERIC
        )
        val ruleVariable5: RuleVariable = RuleVariableNewestEvent(
            UID01WILD, "test_data_element5", RuleValueType.NUMERIC
        )
        val ruleVariable6: RuleVariable = RuleVariableNewestEvent(
            UID012, "test_data_element6", RuleValueType.NUMERIC
        )
        val ruleVariable7: RuleVariable = RuleVariableNewestEvent(
            UID0WILD2, "test_data_element7", RuleValueType.NUMERIC
        )
        val actions = Arrays
            .asList(
                ruleAction1, ruleAction2, ruleAction3, ruleAction4, ruleAction5, ruleAction6, ruleAction7,
                ruleAction8, ruleAction9, ruleAction10
            )
        val rule = Rule(
            null, null, null, "true",
            actions, ""
        )
        val ruleVariables = Arrays
            .asList(
                ruleVariable1, ruleVariable2, ruleVariable3, ruleVariable4, ruleVariable5, ruleVariable6,
                ruleVariable7
            )

        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            currentDate(),
            currentDate(),
            null,
            "",
            null,
            Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element1", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element2", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element3", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element4", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element5", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element6", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element7", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element8", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element9", "2.6"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element10", "2.6")
            )
        )

        val ruleEffects: List<RuleEffect> = getRuleEngine(rule, ruleVariables).evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(10)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction1)
        Assert.assertEquals("3", ruleEffects[0].data)
        Assertions.assertThat(ruleEffects[1].ruleAction).isEqualTo(ruleAction2)
        Assert.assertEquals("3", ruleEffects[1].data)
        Assertions.assertThat(ruleEffects[2].ruleAction).isEqualTo(ruleAction3)
        Assert.assertEquals("3", ruleEffects[2].data)
        Assertions.assertThat(ruleEffects[3].ruleAction).isEqualTo(ruleAction4)
        Assert.assertEquals("3", ruleEffects[3].data)
        Assertions.assertThat(ruleEffects[4].ruleAction).isEqualTo(ruleAction5)
        Assert.assertEquals("3", ruleEffects[4].data)
        Assertions.assertThat(ruleEffects[5].ruleAction).isEqualTo(ruleAction6)
        Assert.assertEquals("3", ruleEffects[5].data)
        Assertions.assertThat(ruleEffects[6].ruleAction).isEqualTo(ruleAction7)
        Assert.assertEquals("3", ruleEffects[6].data)
        Assertions.assertThat(ruleEffects[7].ruleAction).isEqualTo(ruleAction8)
        Assert.assertEquals("3", ruleEffects[7].data)
        Assertions.assertThat(ruleEffects[8].ruleAction).isEqualTo(ruleAction9)
        Assert.assertEquals("3", ruleEffects[8].data)
        Assertions.assertThat(ruleEffects[9].ruleAction).isEqualTo(ruleAction10)
        Assert.assertEquals("3", ruleEffects[9].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateHasValueFunctionMustReturnTrueIfVariableIsComposedUIDs() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:hasValue(#{" + UID01 + "})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            UID01, "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule(null, null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            currentDate(),
            currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )

        val ruleEffects: List<RuleEffect> = getRuleEngine(rule, listOf(ruleVariable)).evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].data).isEqualTo("true")
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateHasValueFunctionMustReturnTrueIfVariableIsVariableName() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:hasValue(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariable: RuleVariable = RuleVariableCurrentEvent(
            VARIABLE_NAME, "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule(null, null, null, "true", Arrays.asList(ruleAction), "")

        val ruleEvent = RuleEvent(
            "test_event",
            "test_program_stage",
            "",
            RuleEvent.Status.ACTIVE,
            currentDate(),
            currentDate(),
            null,
            "",
            null,
            listOf(
                RuleDataValue(
                    currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            )
        )
        val ruleEffects: List<RuleEffect> = getRuleEngine(rule, listOf(ruleVariable)).evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].data).isEqualTo("true")
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfValueIsVariableName() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:countIfValue(#{" + VARIABLE_NAME + "}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )

        val ruleEffects: List<RuleEffect> = getRuleEngine(
            rules = listOf(rule),
            ruleVariables = listOf(ruleVariableOne),
            ruleEvents = listOf(ruleEvent2, ruleEvent3)
        ).evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertTrue(ruleEffects[0].data == "2")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfValueIsComposedUid() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:countIfValue(#{" + UID01 + "}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEngine = getRuleEngine(
            rules = listOf(rule),
            ruleVariables = listOf(ruleVariableOne),
            ruleEvents = listOf(ruleEvent2, ruleEvent3)
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertTrue(ruleEffects[0].data == "2")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfVariableName() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:count(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent4 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "condition")
            )
        )
        val ruleEngine = getRuleEngine(
            listOf(rule),
            listOf(ruleVariableOne),
            ruleEvents = listOf(ruleEvent2, ruleEvent3, ruleEvent4)
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertEquals("3", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfVariableNameIfComposedUid() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:count(#{" + UID01 + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEvent4 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "condition")
            )
        )
        val ruleEngine = getRuleEngine(
            listOf(rule),
            listOf(ruleVariableOne),
            ruleEvents = listOf(ruleEvent2, ruleEvent3, ruleEvent4)
        )
        val ruleEffects: List<RuleEffect> = ruleEngine.evaluate(ruleEvent)

        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertEquals("3", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfZeroPosIfVariableName() {
        val ruleAction: RuleAction = RuleAction.DisplayText(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:countIfZeroPos(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "0")
            )
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "1")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event1", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "-3")
            )
        )

        val ruleEffects =
            getRuleEngine(listOf(rule), listOf(ruleVariableOne), ruleEvents = listOf(ruleEvent1, ruleEvent2))
                .evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertEquals("2", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfZeroPosIfComposedUid() {
        val ruleAction: RuleAction = RuleAction.DisplayText(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:countIfZeroPos(#{" + UID01 + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "0")
            )
        )
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "1")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event1", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "-3")
            )
        )
        val ruleEffects =
            getRuleEngine(listOf(rule), listOf(ruleVariableOne), ruleEvents = listOf(ruleEvent1, ruleEvent2))
                .evaluate(ruleEvent)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertEquals("2", ruleEffects[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2MaxValueIfVariableName() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule =
            Rule("", null, null, "d2:maxValue(#{" + VARIABLE_NAME + "}) == 8.0", Arrays.asList(ruleAction), "")

        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects: List<RuleEffect> = getRuleEngine(
            listOf(rule),
            listOf(ruleVariableOne, ruleVariableTwo),
            ruleEvents = listOf(ruleEvent1, ruleEvent2)
        )
            .evaluate(ruleEvent3)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2MaxValueIfComposedUid() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule: Rule = Rule("", null, null, "d2:maxValue(#{" + UID01 + "}) == 8.0", Arrays.asList(ruleAction), "")
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = getRuleEngine(
            listOf(rule),
            listOf(ruleVariableOne, ruleVariableTwo),
            ruleEvents = listOf(ruleEvent1, ruleEvent2)
        )
            .evaluate(ruleEvent3)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testMinValueIfVariableName() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:minValue(#{" + VARIABLE_NAME + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")
        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = getRuleEngine(
            listOf(rule),
            listOf(ruleVariableOne, ruleVariableTwo),
            ruleEvents = listOf(ruleEvent1, ruleEvent2)
        )
            .evaluate(ruleEvent3)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assertions.assertThat(ruleEffects[0].data).isEqualTo("5.0")
    }

    @Test
    @Throws(Exception::class)
    fun testMinValueIfComposedUid() {
        val ruleAction: RuleAction = DisplayKeyValuePair(
            "test_action_content", DisplayLocation.LOCATION_FEEDBACK_WIDGET, "d2:minValue(#{" + UID01 + "})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            UID01, "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", Arrays.asList(ruleAction), "")

        val ruleEvent1 = RuleEvent(
            "test_event1", "test_program_stage1", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent2 = RuleEvent(
            "test_event2", "test_program_stage2", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEvent3 = RuleEvent(
            "test_event3", "test_program_stage3", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), null, "", null, Arrays.asList(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            )
        )
        val ruleEffects = getRuleEngine(
            listOf(rule),
            listOf(ruleVariableOne, ruleVariableTwo),
            ruleEvents = listOf(ruleEvent1, ruleEvent2)
        )
            .evaluate(ruleEvent3)
        Assertions.assertThat(ruleEffects.size).isEqualTo(1)
        Assertions.assertThat(ruleEffects[0].ruleAction).isEqualTo(ruleAction)
        Assertions.assertThat(ruleEffects[0].data).isEqualTo("5.0")
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