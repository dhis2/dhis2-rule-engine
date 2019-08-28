package org.hisp.dhis.rules.models

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Java6Assertions.assertThat
import org.hisp.dhis.rules.models.RuleActionText.Companion.LOCATION_FEEDBACK_WIDGET
import org.hisp.dhis.rules.models.RuleActionText.Companion.LOCATION_INDICATOR_WIDGET
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleActionDisplayKeyValuePairTests {

    @Test
    fun createForFeedbackMustSubstituteCorrectLocation() {
        val displayTextAction = RuleActionDisplayKeyValuePair.createForFeedback("test_content", "test_data")

        assertThat(displayTextAction.location).isEqualTo(LOCATION_FEEDBACK_WIDGET)
        assertThat(displayTextAction.content).isEqualTo("test_content")
        assertThat(displayTextAction.data).isEqualTo("test_data")
    }

    @Test
    fun createForIndicatorsMustSubstituteCorrectLocation() {
        val displayTextAction = RuleActionDisplayKeyValuePair.createForIndicators("test_content", "test_data")

        assertThat(displayTextAction.location).isEqualTo(LOCATION_INDICATOR_WIDGET)
        assertThat(displayTextAction.content).isEqualTo("test_content")
        assertThat(displayTextAction.data).isEqualTo("test_data")
    }

    @Test
    fun createForFeedbackMustThrowWhenBothArgumentsNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleActionDisplayKeyValuePair.createForFeedback(null, null)
        }
    }

    @Test
    fun createForIndicatorsMustThrowWhenBothArgumentsNull() {
        assertFailsWith<IllegalArgumentException> {
            RuleActionDisplayKeyValuePair.createForIndicators(null, null)
        }
    }

    @Test
    fun createForFeedbackMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionNoContent = RuleActionDisplayKeyValuePair.createForFeedback(null, "test_data")
        val ruleActionNoData = RuleActionDisplayKeyValuePair.createForFeedback("test_content", null)

        assertThat(ruleActionNoContent.content).isEqualTo("")
        assertThat(ruleActionNoContent.data).isEqualTo("test_data")

        assertThat(ruleActionNoData.content).isEqualTo("test_content")
        assertThat(ruleActionNoData.data).isEqualTo("")
    }

    @Test
    fun createForIndicatorsMustSubstituteEmptyStringsForNullArguments() {
        val ruleActionNoContent = RuleActionDisplayKeyValuePair.createForIndicators(null, "test_data")
        val ruleActionNoData = RuleActionDisplayKeyValuePair.createForIndicators("test_content", null)

        assertThat(ruleActionNoContent.content).isEqualTo("")
        assertThat(ruleActionNoContent.data).isEqualTo("test_data")

        assertThat(ruleActionNoData.content).isEqualTo("test_content")
        assertThat(ruleActionNoData.data).isEqualTo("")
    }

    @Test
    fun equalsAndHashcodeFunctionsMustConformToContract() {
        EqualsVerifier.forClass(RuleActionDisplayKeyValuePair.createForFeedback("", "").javaClass)
                .suppress( Warning.NONFINAL_FIELDS )
                .verify()
    }
}
