package org.dhis2.ruleengine

import org.assertj.core.api.Assertions.assertThat
import org.dhis2.ruleengine.models.RuleValueType

internal class RuleVariableValueAssert private constructor(private val variableValue: RuleVariableValue?) {

    fun hasValue(value: String?): RuleVariableValueAssert {
        assertThat(variableValue?.value).isEqualTo(value)
        return this
    }

    fun hasCandidates(vararg candidates: String): RuleVariableValueAssert {
        assertThat(variableValue?.candidates?.size).isEqualTo(candidates.size)
        for (index in candidates.indices) {
            assertThat(variableValue?.candidates?.get(index)).isEqualTo(candidates[index])
        }
        return this
    }

    fun isTypeOf(valueType: RuleValueType): RuleVariableValueAssert {
        assertThat(variableValue?.ruleValueType).isEqualTo(valueType)
        return this
    }

    companion object {
        fun assertThatVariable(variableValue: RuleVariableValue?): RuleVariableValueAssert {
            return RuleVariableValueAssert(variableValue)
        }
    }
}