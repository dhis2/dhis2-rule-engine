package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.spi.VariableValue
import org.hisp.dhis.rules.models.RuleValueType
import kotlin.test.assertEquals

internal class RuleVariableValueAssert private constructor(
    private val variableValue: VariableValue,
) {
    fun hasValue(value: String?): RuleVariableValueAssert {
        assertEquals(value, variableValue.value)
        return this
    }

    fun hasCandidates(vararg candidates: String?): RuleVariableValueAssert {
        assertEquals(candidates.size, variableValue.candidates.size)
        for (index in candidates.indices) {
            assertEquals(candidates[index], variableValue.candidates.get(index))
        }
        return this
    }

    fun isTypeOf(valueType: RuleValueType?): RuleVariableValueAssert {
        assertEquals(valueType?.toValueType(), variableValue.valueType)
        return this
    }

    companion object {
        fun assertThatVariable(variableValue: VariableValue): RuleVariableValueAssert = RuleVariableValueAssert(variableValue)
    }
}
