package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.fail
import org.hisp.dhis.rules.models.RuleActionText.Companion.LOCATION_FEEDBACK_WIDGET
import org.hisp.dhis.rules.models.RuleActionText.Companion.LOCATION_INDICATOR_WIDGET
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RuleActionDisplayTextTests {

    @Test
    fun createForFeedbackMustSubstituteCorrectLocation() {
        val displayTextAction = RuleActionDisplayText.createForFeedback("test_content", "test_data")

        assertThat(displayTextAction.location).isEqualTo(LOCATION_FEEDBACK_WIDGET)
        assertThat(displayTextAction.content).isEqualTo("test_content")
        assertThat(displayTextAction.data).isEqualTo("test_data")
    }

    @Test
    fun createForIndicatorsMustSubstituteCorrectLocation() {
        val displayTextAction = RuleActionDisplayText.createForIndicators("test_content", "test_data")

        assertThat(displayTextAction.location).isEqualTo(LOCATION_INDICATOR_WIDGET)
        assertThat(displayTextAction.content).isEqualTo("test_content")
        assertThat(displayTextAction.data).isEqualTo("test_data")
    }

    @Test
    fun createForFeedbackMustThrowWhenBothArgumentsNull() {
        try {
            RuleActionDisplayText.createForFeedback(null, null)
            fail("IllegalArgumentException was expected, but nothing was thrown.")
        } catch (illegalArgumentException: IllegalArgumentException) {
            // noop
        }

    }

    @Test
    fun createForIndicatorsMustThrowWhenBothArgumentsNull() {
        try {
            RuleActionDisplayText.createForIndicators(null, null)
            fail("IllegalArgumentException was expected, but nothing was thrown.")
        } catch (illegalArgumentException: IllegalArgumentException) {
            // noop
        }

    }

    @Test
    fun createForFeedbackMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionNoContent = RuleActionDisplayText.createForFeedback(null, "test_data")
        val ruleActionNoData = RuleActionDisplayText.createForFeedback("test_content", null)

        assertThat(ruleActionNoContent.content).isEqualTo("")
        assertThat(ruleActionNoContent.data).isEqualTo("test_data")

        assertThat(ruleActionNoData.content).isEqualTo("test_content")
        assertThat(ruleActionNoData.data).isEqualTo("")
    }

    @Test
    fun createForIndicatorsMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionNoContent = RuleActionDisplayText.createForIndicators(null, "test_data")
        val ruleActionNoData = RuleActionDisplayText.createForIndicators("test_content", null)

        assertThat(ruleActionNoContent.content).isEqualTo("")
        assertThat(ruleActionNoContent.data).isEqualTo("test_data")

        assertThat(ruleActionNoData.content).isEqualTo("test_content")
        assertThat(ruleActionNoData.data).isEqualTo("")
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionDisplayText.createForFeedback("", "").javaClass)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify()
    }
}
