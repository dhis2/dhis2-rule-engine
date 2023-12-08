package org.hisp.dhis.rules

import io.mockk.mockk
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleVariable
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertTrue

internal class RuleEngineTest {

    private val ruleEngineContext: RuleEngineContext = RuleEngineContext(
        listOf(Rule("true", listOf())),listOf(mockk<RuleVariable>()))

    @Test(expected = IllegalStateException::class)
    fun evaluateShouldThrowIfEventIsAlreadyInContext() {
        val ruleEvent = RuleEvent("test_event", "test_programstage","",
                RuleEvent.Status.ACTIVE, Date(), Date(), null,"", null, ArrayList())
        val ruleEvents: MutableList<RuleEvent> = ArrayList()
        ruleEvents.add(ruleEvent)
        val ruleEngine = RuleEngine(ruleEngineContext, ruleEvents)
        ruleEngine.evaluate(ruleEvent)
    }

    @Test
    @Throws(InterruptedException::class)
    fun concurrentIterationOverRulesListShouldNotFail() {
        val executionContext = RuleEngineContext(listOf(mockk<Rule>(), mockk<Rule>()), emptyList())
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
