package org.dhis2.ruleengine.models

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.dhis2.ruleengine.RuleVariableValue

sealed class RuleVariable(
    open val name: String,
    open val ruleValueType: RuleValueType,
    open val useCodeForOptionSet: Boolean,
    open val options: List<Option>
) {
    data class RuleVariableAttribute(
        override val name: String,
        val trackedEntityAttribute: String,
        val trackedEntityAttributeType: RuleValueType,
        override val useCodeForOptionSet: Boolean,
        override val options: List<Option>
    ) : RuleVariable(name, trackedEntityAttributeType, useCodeForOptionSet, options) {
        fun getRuleVariableValue(
            ruleEnrollment: RuleEnrollment?,
            currentEnrollmentValues: Map<String, RuleAttributeValue>
        ): RuleVariableValue? {
            return ruleEnrollment?.let {
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                val ruleAttributeValue = currentEnrollmentValues[this.trackedEntityAttribute]
                val candidates = ruleAttributeValue?.let { listOf(it.value) } ?: emptyList()
                val value = if (!useCodeForOptionSet) getOptionName(options, ruleAttributeValue?.value ) else ruleAttributeValue?.value;
                return RuleVariableValue(
                    variableValue = value,
                    ruleValueType = trackedEntityAttributeType,
                    candidates = candidates,
                    eventDate = currentDate
                )
            }
        }
    }

    data class RuleVariableCalculatedValue(
        override val name: String,
        val calculatedValueVariable: String,
        val calculatedValueType: RuleValueType,
        override val useCodeForOptionSet: Boolean,
        override val options: List<Option>
    ) : RuleVariable(name, calculatedValueType, useCodeForOptionSet, options) {
        fun getRuleVariableValue(): RuleVariableValue {
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            return RuleVariableValue(
                variableValue = null,
                ruleValueType = calculatedValueType,
                candidates = emptyList(),
                eventDate = currentDate
            )
        }
    }

    data class RuleVariableCurrentEvent(
        override val name: String,
        val dataElement: String,
        val dataElementValueType: RuleValueType,
        override val useCodeForOptionSet: Boolean,
        override val options: List<Option>
    ) : RuleVariable(name, dataElementValueType, useCodeForOptionSet, options) {
        fun getRuleVariableValue(
            ruleEvent: RuleEvent?,
            currentEventValues: Map<String, RuleDataValue>
        ): RuleVariableValue? {
            return ruleEvent?.let {
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                val ruleValue = currentEventValues[this.dataElement]
                val candidates = ruleValue?.let { listOf(it.value) } ?: emptyList()
                val value = if (!useCodeForOptionSet) getOptionName(options, ruleValue?.value ) else ruleValue?.value
                return RuleVariableValue(
                    variableValue = value,
                    ruleValueType = dataElementValueType,
                    candidates = candidates,
                    eventDate = currentDate
                )
            }
        }
    }

    data class RuleVariableNewestEvent(
        override val name: String,
        val dataElement: String,
        val dataElementValueType: RuleValueType,
        override val useCodeForOptionSet: Boolean,
        override val options: List<Option>
    ) : RuleVariable(name, dataElementValueType, useCodeForOptionSet, options) {
        fun getRuleVariableValue(
            allEventValues: Map<String, MutableList<RuleDataValue>>
        ): RuleVariableValue {
            val ruleDataValues = allEventValues[dataElement]
            val candidates = ruleDataValues?.map { it.value } ?: emptyList()
            val currentDate = if(ruleDataValues.isNullOrEmpty()){
                Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            }else{
                ruleDataValues.maxBy { it.eventDate }.eventDate
            }
            val value = if (!useCodeForOptionSet) getOptionName(options, ruleDataValues?.firstOrNull()?.value ) else ruleDataValues?.firstOrNull()?.value
            return RuleVariableValue(
                variableValue = value,
                ruleValueType = dataElementValueType,
                candidates = candidates,
                eventDate = currentDate
            )
        }
    }

    data class RuleVariableNewestStageEvent(
        override val name: String,
        val dataElement: String,
        val dataElementValueType: RuleValueType,
        val programStage: String,
        override val useCodeForOptionSet: Boolean,
        override val options: List<Option>
    ) : RuleVariable(name, dataElementValueType, useCodeForOptionSet, options) {
        fun getRuleVariableValue(
            allEventValues: Map<String, MutableList<RuleDataValue>>
        ): RuleVariableValue {
            val stageRuleDataValues: MutableList<RuleDataValue> = mutableListOf()
            val sourceRuleDataValues = allEventValues[this.dataElement]
            if (!sourceRuleDataValues.isNullOrEmpty()) {
                // filter data values based on program stage
                sourceRuleDataValues.forEach { ruleDataValue ->
                    if (programStage == ruleDataValue.programStage) {
                        stageRuleDataValues.add(ruleDataValue)
                    }
                }
            }

            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            val value = if (!useCodeForOptionSet) getOptionName(options, stageRuleDataValues.firstOrNull()?.value ) else stageRuleDataValues.firstOrNull()?.value
            return if (stageRuleDataValues.isEmpty()) {
                RuleVariableValue(
                    variableValue = null,
                    ruleValueType = dataElementValueType,
                    candidates = emptyList(),
                    eventDate = currentDate
                )
            } else {
                RuleVariableValue(
                    variableValue = value,
                    ruleValueType = dataElementValueType,
                    candidates = stageRuleDataValues.map { it.value },
                    eventDate = stageRuleDataValues.getLastUpdateList()
                )
            }
        }
    }

    data class RuleVariablePreviousEvent(
        override val name: String,
        val dataElement: String,
        val dataElementValueType: RuleValueType,
        override val useCodeForOptionSet: Boolean,
        override val options: List<Option>
    ) : RuleVariable(name, dataElementValueType, useCodeForOptionSet, options) {
        fun getRuleVariableValue(
            ruleEvent: RuleEvent?,
            allEventValues: Map<String, MutableList<RuleDataValue>>
        ): RuleVariableValue? {
            if (ruleEvent == null) {
                return null
            }

            var variableValue: RuleVariableValue? = null
            val ruleDataValues: List<RuleDataValue>? = allEventValues[dataElement]
            if (!ruleDataValues.isNullOrEmpty()) {
                for (ruleDataValue in ruleDataValues) {
                    // We found preceding value to the current currentEventValues,
                    // which is assumed to be the best candidate.
                    if (ruleEvent.eventDate > ruleDataValue.eventDate) {
                        val value = if (!useCodeForOptionSet) getOptionName(options, ruleDataValue.value ) else ruleDataValue.value
                        variableValue = RuleVariableValue(
                            value,
                            dataElementValueType,
                            ruleDataValues.map { it.value },
                            ruleDataValues.getLastUpdateDateForPrevious(ruleEvent)
                        )
                        break
                    }
                }
            }

            if (variableValue == null) {
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                variableValue = RuleVariableValue(
                    null,
                    dataElementValueType,
                    emptyList(),
                    currentDate
                )
            }
            return variableValue
        }
    }
}

fun getOptionName(
    options: List<Option>,
    value: String?
): String? {
    return options.filter { option -> option.code == value }.map { option -> option.name }.firstOrNull()?: value
}

fun RuleVariable.toRuleVariableValue(
    ruleEnrollment: RuleEnrollment?,
    ruleEvent: RuleEvent?,
    allEventValues: Map<String, MutableList<RuleDataValue>>,
    currentEnrollmentValues: Map<String, RuleAttributeValue>,
    currentEventValues: Map<String, RuleDataValue>
): RuleVariableValue? {
    return when (this) {
        is RuleVariable.RuleVariableAttribute -> getRuleVariableValue(ruleEnrollment, currentEnrollmentValues)
        is RuleVariable.RuleVariableCalculatedValue -> getRuleVariableValue()
        is RuleVariable.RuleVariableCurrentEvent -> getRuleVariableValue(ruleEvent, currentEventValues)
        is RuleVariable.RuleVariableNewestEvent -> getRuleVariableValue(allEventValues)
        is RuleVariable.RuleVariableNewestStageEvent -> getRuleVariableValue(allEventValues)
        is RuleVariable.RuleVariablePreviousEvent -> getRuleVariableValue(ruleEvent, allEventValues)
    }
}