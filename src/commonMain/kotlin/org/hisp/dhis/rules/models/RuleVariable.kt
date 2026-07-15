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

    // Concrete classes should override this with a stored val so the map is built once.
    // The default recomputes on every call, which is safe for external implementations.
    val optionsByCode: Map<String, String> get() = options.associate { it.code to it.name }

    fun getOptionName(value: String): String = optionsByCode[value] ?: value
}
