package org.hisp.dhis.rules

import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleVariable
import kotlin.test.Test

internal class RuleEngineTest {

    private val ruleEngineContext: RuleEngineContext = RuleEngineContext(
        listOf(Rule("true", listOf())),listOf(mockk<RuleVariable>()))

    @Test(expected = IllegalStateException::class)
    fun evaluateShouldThrowIfEventIsAlreadyInContext() {
        val ruleEvent = RuleEvent("test_event", "test_programstage","",
                RuleEvent.Status.ACTIVE, LocalDate.Companion.currentDate(), LocalDate.Companion.currentDate(), null,"", null, ArrayList())
        val ruleEvents: MutableList<RuleEvent> = ArrayList()
        ruleEvents.add(ruleEvent)
        val ruleEngine = RuleEngine(ruleEngineContext, ruleEvents)
        ruleEngine.evaluate(ruleEvent)
    }
}
