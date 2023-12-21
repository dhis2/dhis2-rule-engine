package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.Expression
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.ParseException
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.rules.models.*

class RuleEngine {

    fun evaluate(ruleEvent: RuleEvent, executionContext: RuleEngineContext): List<RuleEffect> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEvent)
            .ruleVariables(executionContext.ruleVariables)
            .ruleEnrollment(executionContext.enrollment)
            .triggerEnvironment(TriggerEnvironment.SERVER)
            .ruleEvents(executionContext.events)
            .constantValueMap(executionContext.constantsValues)
            .build()
        return RuleEngineExecution(ruleEvent, null, executionContext.rules, valueMap, executionContext.supplementaryData).execute()
    }

    fun evaluate(ruleEnrollment: RuleEnrollment, executionContext: RuleEngineContext): List<RuleEffect> {
        val valueMap = RuleVariableValueMapBuilder.target(ruleEnrollment)
                .ruleVariables(executionContext.ruleVariables)
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(executionContext.events)
                .constantValueMap(executionContext.constantsValues)
                .build()
        return RuleEngineExecution(null, ruleEnrollment, executionContext.rules, valueMap, executionContext.supplementaryData).execute()
    }

    fun evaluate(executionContext: RuleEngineContext): List<RuleEffects> {
        val valueMap = RuleVariableValueMapBuilder.target()
                .ruleVariables(executionContext.ruleVariables)
                .ruleEnrollment(executionContext.enrollment)
                .triggerEnvironment(TriggerEnvironment.SERVER)
                .ruleEvents(executionContext.events)
                .constantValueMap(executionContext.constantsValues)
                .multipleBuild()
        return RuleEngineMultipleExecution(executionContext.rules, valueMap,
                executionContext.supplementaryData).execute()
    }
}
