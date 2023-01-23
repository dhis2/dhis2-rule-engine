package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.assertEquals

internal class RuleVariableValueAssert private constructor(private val variableValue: RuleVariableValue?) {

    fun hasValue(value: String?): RuleVariableValueAssert {
        assertEquals(variableValue?.value, value)
        return this
    }

    fun hasCandidates(vararg candidates: String): RuleVariableValueAssert {
        assertEquals(variableValue?.candidates?.size, candidates.size)
        for (index in candidates.indices) {
            assertEquals(variableValue?.candidates?.get(index), candidates[index])
        }
        return this
    }

    fun isTypeOf(valueType: RuleValueType): RuleVariableValueAssert {
        assertEquals(variableValue?.ruleValueType, valueType)
        return this
    }

    companion object {
        fun assertEqualsVariable(variableValue: RuleVariableValue?): RuleVariableValueAssert {
            return RuleVariableValueAssert(variableValue)
        }
    }
}