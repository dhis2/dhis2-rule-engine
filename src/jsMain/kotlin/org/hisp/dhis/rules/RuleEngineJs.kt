package org.hisp.dhis.rules

import js.array.tupleOf
import js.collections.JsMap
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.api.DataItem
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.api.RuleEngineContext
import org.hisp.dhis.rules.api.RuleSupplementaryData
import org.hisp.dhis.rules.models.*


@JsExport
class RuleEngineJs(verbose: Boolean = false) {
    init {
        RuleEngineJs.verbose = verbose
    }

    fun validate(expression: String, dataItemStore: JsMap<String, DataItemJs>): RuleValidationResult{
        return RuleEngine.getInstance().validate(expression, toMap(dataItemStore, {it}, ::toDataItemJava))
    }
    fun validateDataFieldExpression(expression: String, dataItemStore: JsMap<String, DataItemJs>): RuleValidationResult{
        return RuleEngine.getInstance().validateDataFieldExpression(expression, toMap(dataItemStore, {it}, ::toDataItemJava))
    }
    fun evaluateAll(enrollmentTarget: RuleEnrollmentJs?, eventsTarget: Array<RuleEventJs>, executionContext: RuleEngineContextJs): Array<RuleEffectsJs>{
        return toRuleEffectsJsList(RuleEngine.getInstance().evaluateAll(toEnrollmentJava(enrollmentTarget),
            eventsTarget.map(::toEventJava),
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
            incidentDate = enrollmentTarget.incidentDate,
            enrollmentDate = enrollmentTarget.enrollmentDate,
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
            eventDate = event.eventDate ?: RuleInstant.fromInstant(Instant.DISTANT_FUTURE),
            createdDate = event.createdDate,
            dueDate = event.dueDate,
            completedDate = event.completedDate,
            organisationUnit = event.organisationUnit,
            organisationUnitCode = event.organisationUnitCode,
            dataValues = event.dataValues.toList()
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
            ruleSupplementaryData = toSupplementaryDataJava(executionContext.supplementaryData),
            constantsValues = toMap(executionContext.constantsValues, {it}, {it}),
            ruleVariables = executionContext.ruleVariables.map(::toRuleVariableJava)
        )
    }

    private fun toSupplementaryDataJava(ruleSupplementaryDataJs: RuleSupplementaryDataJs): RuleSupplementaryData {
        return RuleSupplementaryData(
            userRoles = ruleSupplementaryDataJs.userRoles.toList(),
            userGroups = ruleSupplementaryDataJs.userGroups.toList(),
            orgUnitGroups = toMap(ruleSupplementaryDataJs.orgUnitGroups, {it}, Array<String>::toList)
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

    internal companion object {
        var verbose: Boolean = false
        var lastDate: Instant = Clock.System.now()
    }
}
