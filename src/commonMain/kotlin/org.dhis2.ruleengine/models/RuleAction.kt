package org.dhis2.ruleengine.models

sealed class RuleAction(open val data: String, val attributeType: AttributeType? = AttributeType.UNKNOWN) {
    data class Assign(
        val field: String?,
        override val data: String,
        val content: String
    ) : RuleAction(data)

    data class CreateEvent(
        val content: String,
        val programStage: String,
        override val data: String
    ) : RuleAction(data)

    data class DisplayKeyValuePair(
        val content: String,
        private val displayLocation: DisplayLocation,
        override val data: String
    ) : RuleAction(data) {
        val location get() = displayLocation
    }

    data class DisplayText(
        val content: String,
        private val displayLocation: DisplayLocation,
        override val data: String
    ) : RuleAction(data) {
        val location get() = displayLocation
    }

    data class ErrorOnCompletion(
        val type: AttributeType,
        val content: String,
        val field: String,
        override val data: String,
    ) : RuleAction(data, type)

    data class WarningOnCompletion(
        val type: AttributeType,
        val content: String,
        val field: String,
        override val data: String,
    ) : RuleAction(data, type)

    data class HideField(
        val type: AttributeType,
        val content: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class HideOption(
        val type: AttributeType,
        val content: String,
        val option: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class HideOptionGroup(
        val type: AttributeType,
        val content: String,
        val optionGroup: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class HideProgramStage(
        val programStage: String,
        override val data: String
    ) : RuleAction(data)

    data class HideSection(
        val programStageSection: String,
        override val data: String
    ) : RuleAction(data)

    data class Message(
        val type: AttributeType,
        val content: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class ScheduleMessage(
        val notification: String,
        override val data: String
    ) : RuleAction(data)

    data class SendMessage(
        val notification: String,
        override val data: String
    ) : RuleAction(data)

    data class SetMandatory(
        val type: AttributeType,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class ShowError(
        val type: AttributeType,
        val content: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class ShowOptionGroup(
        val type: AttributeType,
        val content: String,
        val optionGroup: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class ShowWarning(
        val type: AttributeType,
        val content: String,
        val field: String,
        override val data: String
    ) : RuleAction(data, type)

    data class RuleActionError(
        val action: String,
        val message: String
    ) : RuleAction("")

    data class RuleActionUnsupported(
        val content: String,
        val actionValueType: String
    ) : RuleAction("")

    fun isAssignToCalculatedValue(): Boolean {
        return this is Assign && this.field.isNullOrEmpty()
    }
}