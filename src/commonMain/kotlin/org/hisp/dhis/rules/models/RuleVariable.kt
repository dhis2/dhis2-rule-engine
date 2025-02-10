package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue

interface RuleVariable {
    val name: String
    val useCodeForOptionSet: Boolean
    val options: List<Option>
    val field: String
    val fieldType: RuleValueType

    fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
    ): RuleVariableValue

    fun getOptionName(value: String): String {
        return options
            .find { (_, code): Option -> value == code }?.name ?: value
    }
}
