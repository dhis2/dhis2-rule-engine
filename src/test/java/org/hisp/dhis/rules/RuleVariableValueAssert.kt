package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleValueType

import org.assertj.core.api.Java6Assertions.assertThat

class RuleVariableValueAssert (private val variableValue: RuleVariableValue) {

    fun hasValue(value: String?) = apply { assertThat(variableValue.value).isEqualTo(value) }

    fun hasCandidates(vararg candidates: String) = apply {
        assertThat(variableValue.candidates.size).isEqualTo(candidates.size)
        candidates.forEachIndexed { index, candidate ->
            assertThat(variableValue.candidates[index]).isEqualTo(candidate)
        }
    }

    fun isTypeOf(valueType: RuleValueType) = apply { assertThat(variableValue.type).isEqualTo(valueType) }

    companion object {

        @JvmStatic
        fun assertThatVariable(variableValue: RuleVariableValue) = RuleVariableValueAssert(variableValue)

    }
}
