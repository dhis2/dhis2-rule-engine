package org.dhis2.ruleengine

import org.dhis2.ruleengine.models.*

class RuleEngine(
    val ruleEngineContext: RuleEngineContext,
    val ruleEvents: List<RuleEvent>,
    val ruleEnrollment: RuleEnrollment?
) {

    private val ruleConditionEvaluator: RuleConditionEvaluator = RuleConditionEvaluator()

    fun evaluate(
        ruleEvent: RuleEvent? = null,
        ruleEnrollment: RuleEnrollment? = null,
        rulesToEvaluate: List<Rule> = ruleEngineContext.rules
    ): List<RuleEffect> {

        if (ruleEvent == null && ruleEnrollment == null)
            return emptyList()

        val valueMap = buildValueMap(
            ruleEvents = ruleEvents,
            ruleEnrollment = ruleEnrollment,
            ruleEvent = ruleEvent,
            ruleVariables = ruleEngineContext.ruleVariables,
            constantsValues = ruleEngineContext.constantsValues
        )

        val trackerObjectType = TrackerObjectType.EVENT.takeIf { ruleEvent != null } ?: TrackerObjectType.ENROLLMENT
        val targetUid = ruleEvent?.event ?: ruleEnrollment?.enrollment

        return targetUid?.let {
            ruleConditionEvaluator.getRuleEffects(
                trackerObjectType = trackerObjectType,
                targetUid = targetUid,
                valueMap = valueMap.toMutableMap(),
                supplementaryData = ruleEngineContext.supplementaryData,
                rules = rulesToEvaluate
            )
        } ?: emptyList()
    }

    fun evaluate(): List<RuleEffects> {
        val ruleEffects = mutableListOf<RuleEffects>()

        val ruleVariableValueMap = RuleVariableValueMap(
            enrollmentMap = ruleEnrollment?.let {
                mapOf(
                    Pair(
                        ruleEnrollment,
                        buildValueMap(
                            ruleEvents = ruleEvents,
                            ruleEnrollment = ruleEnrollment,
                            ruleEvent = null,
                            ruleVariables = ruleEngineContext.ruleVariables,
                            constantsValues = ruleEngineContext.constantsValues
                        )
                    )
                )
            }?: emptyMap(),
            eventMap = ruleEvents.associateWith { ruleEvent ->
                buildValueMap(
                    ruleEvents = ruleEvents,
                    ruleEnrollment = ruleEnrollment,
                    ruleEvent = ruleEvent,
                    ruleVariables = ruleEngineContext.ruleVariables,
                    constantsValues = ruleEngineContext.constantsValues
                )
            }
        )

        ruleVariableValueMap.enrollmentMap.forEach { (enrollment, value) ->
            val enrollmentRuleEffects: List<RuleEffect> = ruleConditionEvaluator
                .getRuleEffects(
                    TrackerObjectType.ENROLLMENT,
                    enrollment.enrollment,
                    value.toMutableMap(),
                    ruleEngineContext.supplementaryData,
                    ruleEngineContext.rules.filter {
                        it.programStage.isNullOrEmpty()
                    }.map { rule ->
                        val filterRuleAction = rule.actions.filter { action ->
                            action.attributeType == AttributeType.TRACKED_ENTITY_ATTRIBYTE || action.attributeType == AttributeType.UNKNOWN
                        }
                        rule.copy(actions = filterRuleAction)
                    }
                )
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.ENROLLMENT,
                    enrollment.enrollment,
                    enrollmentRuleEffects
                )
            )
        }

        ruleVariableValueMap.eventMap.forEach { (event, value) ->
            ruleEffects.add(
                RuleEffects(
                    TrackerObjectType.EVENT, event.event,
                    ruleConditionEvaluator.getRuleEffects(
                        TrackerObjectType.EVENT,
                        event.event,
                        value.toMutableMap(),
                        ruleEngineContext.supplementaryData,
                        ruleEngineContext.rules.filter {
                            it.programStage.isNullOrEmpty() || it.programStage == event.programStage
                        }.map { rule ->
                            val filterRuleAction = rule.actions.filter { action ->
                                action.attributeType == AttributeType.DATA_ELEMENT || action.attributeType == AttributeType.UNKNOWN
                            }
                            rule.copy(actions = filterRuleAction)
                        }
                    )
                )
            )
        }

        return ruleEffects
    }

    fun getExpressionDescription(expression: String, castAsBoolean: Boolean = false): RuleValidationResult {
        return try {
            val description = ruleConditionEvaluator.expressionParserEvaluator.getExpressionDescription(
                expression,
                ruleEngineContext.dataItemStore,
                castAsBoolean
            )
            RuleValidationResult.Valid(description = description)
        } catch (e: Exception) {
            RuleValidationResult.Error(e.message ?: "", e)
        }
    }
}