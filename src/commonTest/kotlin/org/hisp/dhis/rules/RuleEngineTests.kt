package org.hisp.dhis.rules

import kotlinx.collections.immutable.ImmutableList
import org.assertj.core.api.Java6Assertions.assertThat
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleVariable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleEngineTests {

    @Mock private val evaluator: RuleExpressionEvaluator = ExpressionEvaluator()

    private lateinit var ruleEngineContext: RuleEngineContext

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        ruleEngineContext = RuleEngineContext.builder(evaluator)
                .ruleVariables(listOf(mock(RuleVariable::class.java)))
                .rules(listOf(mock(Rule::class.java)))
                .build()
    }

    @Test
    fun builderShouldThrowOnNullEnrollment() {
        assertFailsWith<IllegalArgumentException> {
            ruleEngineContext.toEngineBuilder()
                    .enrollment(null)
                    .events(ArrayList())
                    .build()
        }
    }

    @Test
    fun builderShouldThrowOnNullEvents() {
        assertFailsWith<IllegalArgumentException> {
            ruleEngineContext.toEngineBuilder()
                    .enrollment(null)
                    .events(null)
                    .build()
        }
    }

    @Test
    fun builderShouldPropagateImmutableEventsList() {
        val ruleEventOne = mock(RuleEvent::class.java)
        val ruleEventTwo = mock(RuleEvent::class.java)

        val ruleEvents = mutableListOf(ruleEventOne)

        val ruleEngine = ruleEngineContext.toEngineBuilder()
                .events(ruleEvents)
                .build()

        // test immutability
        ruleEngine.events.add(ruleEventTwo)

        assertThat(ruleEngine.events.size).isEqualTo(1)
        assertThat(ruleEngine.events[0]).isEqualTo(ruleEventOne)
        assertThat(ruleEventTwo).isNotIn(ruleEngine.events)
        assertThat(ruleEngine.events).isInstanceOf(ImmutableList::class.java)

    }

    @Test
    fun builderShouldPropagateImmutableEmptyListIfNoEventsProvided() {
        val ruleEngine = ruleEngineContext.toEngineBuilder().build()

        assertThat(ruleEngine.events).isNotNull
        assertThat(ruleEngine.events.size).isEqualTo(0)

    }

    @Test
    fun builderShouldPropagateRuleEngineContext() {
        val ruleEngine = ruleEngineContext.toEngineBuilder().build()

        assertThat(ruleEngine.executionContext).isEqualTo(ruleEngineContext)
    }

    @Test
    fun evaluateShouldThrowOnNullEvent() {
        val ruleEvent: RuleEvent? = null

        assertFailsWith<IllegalArgumentException> {
            ruleEngineContext.toEngineBuilder().build().evaluate(ruleEvent)
        }
    }

    @Test
    fun evaluateShouldThrowIfEventIsAlreadyInContext() {
        val ruleEvent = RuleEvent.create("test_event", "test_programstage",
                RuleEvent.Status.ACTIVE, Date(), Date(), "", null, ArrayList(), "")

        val ruleEvents = listOf(ruleEvent)

        val ruleEngine = ruleEngineContext.toEngineBuilder()
                .events(ruleEvents)
                .build()

        assertFailsWith<IllegalStateException> {
            ruleEngine.evaluate(ruleEvent)
        }
    }

    @Test
    fun evaluateShouldThrowIfEnrollmentIsAlreadyInContext() {
        val ruleEnrollment = mock(RuleEnrollment::class.java)

        val ruleEngine = ruleEngineContext.toEngineBuilder()
                .enrollment(ruleEnrollment)
                .build()

        assertFailsWith<IllegalStateException> {
            ruleEngine.evaluate(ruleEnrollment)
        }
    }

    @Test
    fun evaluateShouldThrowOnNullEnrollment() {
        val ruleEnrollment: RuleEnrollment? = null

        assertFailsWith<IllegalArgumentException> {
            ruleEngineContext.toEngineBuilder().build().evaluate(ruleEnrollment)
        }
    }

    @Test
    fun concurrentIterationOverRulesListShouldNotFail() {
        val ruleEngine = RuleEngineContext.builder(mock(RuleExpressionEvaluator::class.java))
                .rules(listOf(mock(Rule::class.java), mock(Rule::class.java)))
                .build().toEngineBuilder().build()

        val threadOneLatch = CountDownLatch(1)
        val threadTwoLatch = CountDownLatch(1)

        object : Thread() {
            override fun run() {
                for (rule in ruleEngine.executionContext.rules) {

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
                for (rule in ruleEngine.executionContext.rules) {
                    threadTwoLatch.countDown()
                    try {
                        threadOneLatch.await()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
        }.start()

        assertThat(threadOneLatch.await(4, TimeUnit.SECONDS)).isEqualTo(true)
        assertThat(threadTwoLatch.await(4, TimeUnit.SECONDS)).isEqualTo(true)
    }
}
