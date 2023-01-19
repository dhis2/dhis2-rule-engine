package org.dhis2.ruleengine.utils

object VariableNameUnwrapper {
    private const val VARIABLE_PATTERN = "[#]\\{([\\w -_.]+)\\}"
    private val variableNameRegex = Regex(VARIABLE_PATTERN)

    fun unwrap(variable: String): String {
        return variableNameRegex.find(variable)?.groups?.get(1)?.value
            ?: throw IllegalArgumentException("Malformed variable: $variable")
    }
}