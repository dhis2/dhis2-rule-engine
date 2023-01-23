package org.dhis2.ruleengine.utils

object VariableNameUnwrapper {
    private const val VARIABLE_PATTERN = "[#AV]\\{([\\w -_.]+)\\}"
    private const val CONSTANT_PATTERN = "[C]\\{([\\w -_.]+)\\}"
    private val variableNameRegex = Regex(VARIABLE_PATTERN)
    private val constantNameRegex = Regex(CONSTANT_PATTERN)

    fun unwrap(variable: String, defaultValue:String? = null): String {
        return variableNameRegex.find(variable)?.groups?.get(1)?.value
            ?: constantNameRegex.find(variable)?.groups?.get(1)?.value
            ?:defaultValue
            ?:throw IllegalArgumentException("Malformed variable: $variable")
    }

    fun matchesVariable(variable: String): Boolean {
        return variableNameRegex.matches(variable)
    }

    fun matchesConstant(variable: String):Boolean{
        return constantNameRegex.matches(variable)
    }
}