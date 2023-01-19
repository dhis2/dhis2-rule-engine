package org.dhis2.ruleengine.exprk

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.models.RuleValueType
import kotlin.test.Test
import kotlin.test.assertTrue

class FunctionTest {
    @Test
    fun testHasValue() {
        assertTrue { Expressions().eval("d2:hasValue(12)").toBoolean() }
    }

    @Test
    fun testHasValueWithVariable() {
        assertTrue {
            Expressions()
                .withValueMap(
                    mapOf(
                        "test_variable" to RuleVariableValue("123", RuleValueType.NUMERIC)
                    )
                ).eval("d2:hasValue(#{test_variable})").toBoolean()
        }
    }
}