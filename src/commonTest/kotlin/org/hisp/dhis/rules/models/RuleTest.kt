package org.hisp.dhis.rules.models

import kotlin.test.Test
import kotlin.test.assertEquals

class RuleTest {
    @Test
    fun createShouldPropagatePropertiesCorrectly() {
        val ruleAction = RuleAction("data", "ERROR")
        val (condition, actions, uid, _, programStage, priority) =
            Rule(
                "test_condition",
                listOf(ruleAction),
                "uid",
                "",
                "test_program_stage",
                1,
            )
        assertEquals("test_program_stage", programStage)
        assertEquals("test_condition", condition)
        assertEquals(1, priority)
        assertEquals(1, actions.size)
        assertEquals(ruleAction, actions[0])
        assertEquals("uid", uid)
    }
}
