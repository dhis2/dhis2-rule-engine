package org.hisp.dhis.rules

import js.array.tupleOf
import js.collections.JsMap
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.api.DataItem
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.models.*

@JsExport
class RuleEngineJs {
    fun validate(expression: String, dataItemStore: JsMap<String, DataItemJs>): RuleValidationResult{
        return RuleEngine.getInstance().validate(expression, toMap(dataItemStore, {it}, ::toDataItemJava))
    }
    fun validateDataFieldExpression(expression: String, dataItemStore: JsMap<String, DataItemJs>): RuleValidationResult{
        return RuleEngine.getInstance().validateDataFieldExpression(expression, toMap(dataItemStore, {it}, ::toDataItemJava))
    }
    fun evaluateAll(enrollmentTarget: RuleEnrollmentJs?, eventsTarget: Array<RuleEventJs>, executionContext: RuleEngineContextJs): Array<RuleEffectsJs>{
        return toRuleEffectsJsList(RuleEngine.getInstance().evaluateAll(toEnrollmentJava(enrollmentTarget),
            eventsTarget.map(::toEventJava).toList(),
            toRuleEngineContextJava(executionContext)))
    }

    fun evaluateEnrollment(target: RuleEnrollmentJs, ruleEvents: Array<RuleEventJs>, executionContext: RuleEngineContextJs): Array<RuleEffectJs>{
        return RuleEngine.getInstance().evaluate(toEnrollmentJava(target)!!,
                ruleEvents.map(::toEventJava).toList(),
                toRuleEngineContextJava(executionContext))
            .map(::toRuleEffectJs).toTypedArray()
    }
    fun evaluateEvent(target: RuleEventJs, ruleEnrollment: RuleEnrollmentJs?, ruleEvents: Array<RuleEventJs>, executionContext: RuleEngineContextJs): Array<RuleEffectJs>{
        return RuleEngine.getInstance().evaluate(toEventJava(target),
                toEnrollmentJava(ruleEnrollment),
                ruleEvents.map(::toEventJava).toList(),
                toRuleEngineContextJava(executionContext))
            .map(::toRuleEffectJs).toTypedArray()
    }

    private fun <Kf, Vf, K, V> toMap(map: JsMap<Kf, Vf>, key: (Kf) -> K, value: (Vf) -> V): Map<K, V> {
        val res : MutableMap<K, V> = mutableMapOf()
        map.forEach { v, k -> res[key(k)] = value(v) }
        return res
    }

    private fun toDataItemJava(item: DataItemJs) : DataItem {
        return DataItem(
            valueType = item.valueType,
            displayName = item.displayName
        )
    }

    private fun toEnrollmentJava(enrollmentTarget: RuleEnrollmentJs?): RuleEnrollment? {
        if(enrollmentTarget == null) return null

        return RuleEnrollment(
            enrollment = enrollmentTarget.enrollment,
            programName = enrollmentTarget.programName,
            incidentDate = LocalDate.fromEpochDays(enrollmentTarget.incidentDate.toEpochDay().toInt()),
            enrollmentDate = LocalDate.fromEpochDays(enrollmentTarget.enrollmentDate.toEpochDay().toInt()),
            status = enrollmentTarget.status,
            organisationUnit = enrollmentTarget.organisationUnit,
            organisationUnitCode = enrollmentTarget.organisationUnitCode,
            attributeValues = enrollmentTarget.attributeValues.toList()
        )
    }

    private fun toEventJava(event: RuleEventJs): RuleEvent {
        return RuleEvent(
            event = event.event,
            programStage = event.programStage,
            programStageName = event.programStageName,
            status = event.status,
            eventDate = Instant.fromEpochMilliseconds(event.eventDate.toEpochMilli().toLong()),
            dueDate = toLocalDate(event.dueDate),
            completedDate = toLocalDate(event.completedDate),
            organisationUnit = event.organisationUnit,
            organisationUnitCode = event.organisationUnitCode,
            dataValues = event.dataValues.map(::toRuleDataValueJava).toList()
        )
    }

    private fun toRuleJava(rule: RuleJs): Rule {
        return Rule(
            condition = rule.condition,
            actions = rule.actions.map(::toRuleActionJava),
            uid = rule.uid,
            name = rule.name,
            programStage = rule.programStage,
            priority = rule.priority
        )
    }

    private fun toRuleDataValueJava(ruleDataValue: RuleDataValueJs): RuleDataValue {
        return RuleDataValue(
            eventDate = Instant.fromEpochMilliseconds(ruleDataValue.eventDate.toEpochMilli().toLong()),
            programStage = ruleDataValue.programStage,
            dataElement = ruleDataValue.dataElement,
            value = ruleDataValue.value
        )
    }

    private fun toRuleActionJava(ruleAction: RuleActionJs): RuleAction {
        return RuleAction(
            data = ruleAction.data,
            type = ruleAction.type,
            values = toMap(ruleAction.values, {it}, {it})
        )
    }

    private fun toRuleEngineContextJava(executionContext: RuleEngineContextJs): RuleEngineContext {
        return RuleEngineContext(
            rules = executionContext.rules.map(::toRuleJava),
            supplementaryData = toMap(executionContext.supplementaryData, {it}, { v -> v.toList() }),
            constantsValues = toMap(executionContext.constantsValues, {it}, {it}),
            ruleVariables = executionContext.ruleVariables.map(::toRuleVariableJava)
        )
    }

    private fun toRuleActionJs(ruleAction: RuleAction): RuleActionJs {
        return RuleActionJs(
            data = ruleAction.data,
            type = ruleAction.type,
            values = JsMap(ruleAction.values.entries.map { e -> tupleOf(e.key, e.value) }.toTypedArray())
        )
    }

    private fun toRuleEffectJs(ruleEffect: RuleEffect): RuleEffectJs {
        return RuleEffectJs(
            ruleId = ruleEffect.ruleId,
            ruleAction = toRuleActionJs(ruleEffect.ruleAction),
            data = ruleEffect.data
        )
    }

    private fun toRuleEffectsJs(ruleEffects: RuleEffects): RuleEffectsJs {
        return RuleEffectsJs(
            trackerObjectType = ruleEffects.trackerObjectType,
            trackerObjectUid = ruleEffects.trackerObjectUid,
            ruleEffects = ruleEffects.ruleEffects.map(::toRuleEffectJs).toTypedArray()
        )

    }

    private fun toRuleEffectsJsList(ruleEffects: List<RuleEffects>): Array<RuleEffectsJs> {
        return ruleEffects.map(::toRuleEffectsJs).toTypedArray()
    }

    private fun toLocalDate(localDate: kotlinx.datetime.internal.JSJoda.LocalDate?): LocalDate? {
        if (localDate == null) return null
        return LocalDate.fromEpochDays(localDate.toEpochDay().toInt())
    }

    private fun toRuleVariableJava(ruleVariableJs: RuleVariableJs): RuleVariable {
        return when(ruleVariableJs.type){
            RuleVariableType.DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE -> RuleVariableNewestStageEvent(
                name = ruleVariableJs.name,
                useCodeForOptionSet = ruleVariableJs.useCodeForOptionSet,
                options = ruleVariableJs.options.toList(),
                field = ruleVariableJs.field,
                fieldType = ruleVariableJs.fieldType,
                programStage = ruleVariableJs.programStage ?: ""
            )
            RuleVariableType.DATAELEMENT_NEWEST_EVENT_PROGRAM -> RuleVariableNewestEvent(
                name = ruleVariableJs.name,
                useCodeForOptionSet = ruleVariableJs.useCodeForOptionSet,
                options = ruleVariableJs.options.toList(),
                field = ruleVariableJs.field,
                fieldType = ruleVariableJs.fieldType
            )
            RuleVariableType.DATAELEMENT_CURRENT_EVENT -> RuleVariableCurrentEvent(
                name = ruleVariableJs.name,
                useCodeForOptionSet = ruleVariableJs.useCodeForOptionSet,
                options = ruleVariableJs.options.toList(),
                field = ruleVariableJs.field,
                fieldType = ruleVariableJs.fieldType
            )
            RuleVariableType.DATAELEMENT_PREVIOUS_EVENT -> RuleVariablePreviousEvent(
                name = ruleVariableJs.name,
                useCodeForOptionSet = ruleVariableJs.useCodeForOptionSet,
                options = ruleVariableJs.options.toList(),
                field = ruleVariableJs.field,
                fieldType = ruleVariableJs.fieldType
            )
            RuleVariableType.CALCULATED_VALUE -> RuleVariableCalculatedValue(
                name = ruleVariableJs.name,
                useCodeForOptionSet = ruleVariableJs.useCodeForOptionSet,
                options = ruleVariableJs.options.toList(),
                field = ruleVariableJs.field,
                fieldType = ruleVariableJs.fieldType
            )
            RuleVariableType.TEI_ATTRIBUTE -> RuleVariableAttribute(
                name = ruleVariableJs.name,
                useCodeForOptionSet = ruleVariableJs.useCodeForOptionSet,
                options = ruleVariableJs.options.toList(),
                field = ruleVariableJs.field,
                fieldType = ruleVariableJs.fieldType
            )
        }
    }
}