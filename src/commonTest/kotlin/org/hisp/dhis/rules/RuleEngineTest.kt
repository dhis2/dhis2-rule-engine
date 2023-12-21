package org.hisp.dhis.rules

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEvent
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class RuleEngineTest {

    private val ruleEngineContext: RuleEngineContext = RuleEngineContext(
        listOf(Rule("true", listOf())),listOf())

    @Test
    fun evaluateShouldThrowIfEventIsAlreadyInContext() {
        val ruleEvent = RuleEvent(
            "test_event",
            "test_programstage",
            "",
            RuleEvent.Status.ACTIVE,
            LocalDate.currentDate(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEvents: MutableList<RuleEvent> = ArrayList()
        ruleEvents.add(ruleEvent)
        assertFailsWith(IllegalStateException::class) { RuleEngine().evaluate(ruleEvent, ruleEngineContext.copy(events = ruleEvents)) }
    }
}
