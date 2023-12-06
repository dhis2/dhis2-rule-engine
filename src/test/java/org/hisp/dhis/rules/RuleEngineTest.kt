package org.hisp.dhis.rules

import io.mockk.mockk
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.util.MockRuleVariable
import kotlin.test.Test
import kotlin.test.assertEquals
import java.util.*
import java.util.List
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.test.assertTrue

internal class RuleEngineTest {

    private var ruleEngineContext: RuleEngineContext = RuleEngineContext.builder()
    .ruleVariables(listOf(MockRuleVariable()))
    .rules(listOf(Rule("true", listOf())))
    .build()

    @Test
    fun builderShouldPropagateImmutableEventsList() {
        val ruleEventOne = RuleEvent.MOCK
        val ruleEventTwo = RuleEvent.MOCK
        val ruleEvents: MutableList<RuleEvent> = ArrayList()
        ruleEvents.add(ruleEventOne)
        val engine: RuleEngine = ruleEngineContext.toEngineBuilder()
                .events(ruleEvents)
                .build()
        ruleEvents.add(ruleEventTwo)
        assertEquals(1, engine.events.size)
        assertEquals(ruleEventOne, engine.events[0])
    }

    @Test
    fun builderShouldPropagateImmutableEmptyListIfNoEventsProvided() {
        val engine: RuleEngine = ruleEngineContext.toEngineBuilder().build()
        assertEquals(0, engine.events.size)
    }

    @Test
    fun builderShouldPropagateRuleEngineContext() {
        val (executionContext) = ruleEngineContext.toEngineBuilder().build()
        assertEquals(ruleEngineContext, executionContext)
    }

    @Test(expected = IllegalStateException::class)
    fun evaluateShouldThrowIfEventIsAlreadyInContext() {
        val ruleEvent = RuleEvent.create("test_event", "test_programstage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "", null)
        val ruleEvents: MutableList<RuleEvent> = ArrayList()
        ruleEvents.add(ruleEvent)
        val ruleEngine = ruleEngineContext.toEngineBuilder()
                .events(ruleEvents)
                .build()
        ruleEngine.evaluate(ruleEvent)
    }

    @Test
    @Throws(InterruptedException::class)
    fun concurrentIterationOverRulesListShouldNotFail() {
        val (executionContext) = RuleEngineContext.builder()
                .rules(listOf(mockk<Rule>(), mockk<Rule>()))
                .build().toEngineBuilder().build()
        val threadOneLatch = CountDownLatch(1)
        val threadTwoLatch = CountDownLatch(1)
        object : Thread() {
            override fun run() {
                for (rule in executionContext.rules) {
                    try {
                        threadTwoLatch.await()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    threadOneLatch.countDown()
                }
            }
        }.start()
        object : Thread() {
            override fun run() {
                for (rule in executionContext.rules) {
                    threadTwoLatch.countDown()
                    try {
                        threadOneLatch.await()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
        assertTrue(threadOneLatch.await(4, TimeUnit.SECONDS))
        assertTrue(threadTwoLatch.await(4, TimeUnit.SECONDS))
    }
}
