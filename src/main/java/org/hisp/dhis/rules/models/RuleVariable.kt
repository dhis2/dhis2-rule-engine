package org.hisp.dhis.rules.models

import org.hisp.dhis.rules.Option
import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.RuleVariableValueMapBuilder
import java.util.function.Predicate
import javax.annotation.Nonnull

interface RuleVariable {
    fun options(): List<Option>
    fun createValues(
        builder: RuleVariableValueMapBuilder,
        allEventValues: Map<String, List<RuleDataValue>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentEventValues: Map<String, RuleDataValue>
    ): Map<String, RuleVariableValue>

    fun getOptionName(value: String?): String? {
        // if no option found then existing value in the context will be used
        return options()
            .filter{ (_, code): Option -> value == code }
            .map(Option::name)
            .getOrElse(0) {_ -> value}
    }
}
