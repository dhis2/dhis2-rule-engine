package org.hisp.dhis.rules.models

import kotlin.test.assertEquals
import kotlin.test.Test

class RuleActionHideFieldTest {
    @Test
    fun createMustSubstituteEmptyStringIfArgumentsNull() {
        val ruleActionHideField = RuleActionHideField("test_field")
        assertEquals("", ruleActionHideField.content)
    }
}
