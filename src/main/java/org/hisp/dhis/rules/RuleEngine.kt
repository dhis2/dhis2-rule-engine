package org.hisp.dhis.rules

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEffect
import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.TriggerEnvironment
import java.util.*
import java.util.concurrent.Callable

// ToDo: logging
class RuleEngine (
        private val ruleEngineContext: RuleEngineContext,
        private val ruleEvents: PersistentList<RuleEvent>,
        private val ruleEnrollment: RuleEnrollment?,
        private val triggerEnvironment: TriggerEnvironment?) {

    fun events() = ruleEvents

    fun enrollment() = ruleEnrollment

    fun triggerEnvironment() = triggerEnvironment

    fun executionContext() = ruleEngineContext

    fun evaluate(ruleEvent: RuleEvent?): Callable<List<RuleEffect>> {
        ruleEvent?.let {
            ruleEvents.forEach {
                if (it.event == ruleEvent.event)
                    throw IllegalStateException("Event '${it.event}' is already set as a part of execution context.")
            }
            return internalEvaluate(ruleEvent, ruleEngineContext.rules())
        } ?: throw IllegalArgumentException("ruleEvent == null")
    }

    fun evaluate(ruleEvent: RuleEvent?, rulesToEvaluate: List<Rule>): Callable<List<RuleEffect>> {
        ruleEvent?.let {
            ruleEvents.forEach {
                if (it.event == ruleEvent.event)
                    throw IllegalStateException("Event '${it.event}' is already set as a part of execution context.")
            }
            return internalEvaluate(ruleEvent, rulesToEvaluate)
        } ?: throw IllegalArgumentException("ruleEvent == null")

    }

    fun evaluate(ruleEnrollment: RuleEnrollment?, rulesToEvaluate: List<Rule>): Callable<List<RuleEffect>> {
        return when {
            ruleEnrollment == null -> throw IllegalArgumentException("ruleEnrollment == null")
            this.ruleEnrollment != null ->
                throw IllegalStateException("Enrollment '${this.ruleEnrollment.enrollment}' is already set " +
                        "as a part of execution context.")
            else -> internalEvaluate(ruleEnrollment, rulesToEvaluate)
        }
    }

    fun evaluate(ruleEnrollment: RuleEnrollment?): Callable<List<RuleEffect>> {
        return when {
            ruleEnrollment == null -> throw IllegalArgumentException("ruleEnrollment == null")
            this.ruleEnrollment != null ->
                throw IllegalStateException("Enrollment '${this.ruleEnrollment.enrollment}' is already set as a " +
                        "part of execution context.")
            else -> internalEvaluate(ruleEnrollment, ruleEngineContext.rules())
        }
    }

    private fun internalEvaluate(ruleEvent: RuleEvent, rulesToEvaluate: List<Rule>): RuleEngineExecution {

        val valueMap = RuleVariableValueMapBuilder.target(ruleEvent)
                .ruleVariables(ruleEngineContext.ruleVariables())
                .ruleEnrollment(ruleEnrollment)
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(ruleEvents)
                .calculatedValueMap(ruleEngineContext.calculatedValueMap())
                .constantValueMap(ruleEngineContext.constantsValues())
                .build()

        return RuleEngineExecution(executionContext().expressionEvaluator(),
                rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData())
    }

    private fun internalEvaluate(ruleEnrollment: RuleEnrollment, rulesToEvaluate: List<Rule>): RuleEngineExecution {

        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
                .ruleVariables(ruleEngineContext.ruleVariables())
                .triggerEnvironment(triggerEnvironment)
                .ruleEvents(ruleEvents)
                .constantValueMap(ruleEngineContext.constantsValues())
                .build()

        return RuleEngineExecution(executionContext().expressionEvaluator(),
                rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData())
    }

    class Builder (private val ruleEngineContext: RuleEngineContext) {

        private var ruleEvents: PersistentList<RuleEvent>? = null

        private var ruleEnrollment: RuleEnrollment? = null

        private var triggerEnvironment: TriggerEnvironment? = null

        fun events(ruleEvents: List<RuleEvent>?) =
                apply {
                    ruleEvents?.let { this.ruleEvents = ruleEvents.toPersistentList() } ?:
                    throw IllegalArgumentException("ruleEvents == null")
                }


        fun enrollment(ruleEnrollment: RuleEnrollment?) =
                apply {
                    ruleEnrollment?.let { this.ruleEnrollment = it } ?:
                    throw IllegalArgumentException("ruleEnrollment == null")
                }

        fun triggerEnvironment(triggerEnvironment: TriggerEnvironment?) =
                apply {
                    triggerEnvironment?.let { this.triggerEnvironment = triggerEnvironment } ?:
                    throw IllegalArgumentException("triggerEnvironment == null")
                }

        fun build() =
                RuleEngine(ruleEngineContext,
                        ruleEvents ?: persistentListOf(),
                        ruleEnrollment,
                        triggerEnvironment)
    }
}
