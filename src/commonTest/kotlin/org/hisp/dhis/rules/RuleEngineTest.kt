package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.RuleEventStatus
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.utils.currentDate
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
            RuleEventStatus.ACTIVE,
            Clock.System.now(),
            LocalDate.currentDate(),
            null,
            "",
            null,
            ArrayList()
        )
        val ruleEvents: MutableList<RuleEvent> = ArrayList()
        ruleEvents.add(ruleEvent)
        assertFailsWith(IllegalStateException::class) { RuleEngine.getInstance().evaluate(ruleEvent, null, ruleEvents, ruleEngineContext) }
    }
}
