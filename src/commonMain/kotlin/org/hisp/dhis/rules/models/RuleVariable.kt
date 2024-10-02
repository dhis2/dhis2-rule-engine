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
        currentEventValues: Map<String, RuleDataValue>
    ): RuleVariableValue

    fun getOptionName(value: String): String {
        // if no option found then existing value in the context will be used
        return options
            .filter{ (_, code): Option -> value == code }
            .map(Option::name)
            .getOrElse(0) {_ -> value}
    }
}
