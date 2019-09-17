package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleVariableNewestStageEventTests {

    @Test
    fun createShouldThrowOnNullName() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestStageEvent
                    .create(null!!, "test_dataelement", "test_programstage", RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElement() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestStageEvent
                    .create("test_variable", null!!, "test_programstage", RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullProgramStage() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestStageEvent
                    .create("test_variable", "test_dataelement", null!!, RuleValueType.TEXT)
        }
    }

    @Test
    fun createShouldThrowOnNullDataElementType() {
        assertFailsWith<NullPointerException> {
            RuleVariableNewestStageEvent
                    .create("test_variable", "test_dataelement", "test_programstage", null!!)
        }
    }

    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleVariablePreviousEvent = RuleVariableNewestStageEvent.create(
                "test_variable", "test_dataelement", "test_programstage", RuleValueType.NUMERIC)

        assertThat(ruleVariablePreviousEvent.name).isEqualTo("test_variable")
        assertThat(ruleVariablePreviousEvent.dataElement).isEqualTo("test_dataelement")
        assertThat(ruleVariablePreviousEvent.programStage).isEqualTo("test_programstage")
        assertThat(ruleVariablePreviousEvent.dataElementType).isEqualTo(RuleValueType.NUMERIC)
    }
}
