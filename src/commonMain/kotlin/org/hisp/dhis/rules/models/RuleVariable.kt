package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.engine.RuleVariableValue
import org.hisp.dhis.rules.engine.RuleVariableValueMapBuilder

interface RuleVariable {
    val options: List<Option>
    fun createValues(
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue>

    fun getOptionName(value: String?): String? {
        // if no option found then existing value in the context will be used
        return options
            .filter{ (_, code): Option -> value == code }
            .map(Option::name)
            .getOrElse(0) {_ -> value}
    }
}
