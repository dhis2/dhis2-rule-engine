package org.hisp.dhis.rules

import org.hisp.dhis.rules.RuleExpression.Companion.FUNCTION_PATTERN
import kotlin.jvm.JvmStatic

class RuleFunctionCall(val functionCall: String,
                       val functionName: String,
                       val arguments: List<String>) {

    companion object {
        private val JUST_PARAMS_PATTERN ="(^[^(]+\\()|\\)$".toRegex()

        private val SPLIT_PARAMS_PATTERN = "(('[^']+')|([^,]+))".toRegex()

        @JvmStatic fun from(functionCall: String?): RuleFunctionCall {
            functionCall?.let {
                val functionNameMatcher = FUNCTION_PATTERN.find(functionCall)

                functionNameMatcher?.let {
                    if (it.groups.isEmpty())
                        throw IllegalArgumentException("Malformed function call")
                } ?: throw IllegalArgumentException("Malformed function call")


                // Function name which later can be used for looking up functions
                val functionName = functionNameMatcher.groupValues[1]

                // strip all special characters and leave just parameters
                val justParameters = JUST_PARAMS_PATTERN.replace(functionCall, "")

                // match each parameter
                val parametersMatches = SPLIT_PARAMS_PATTERN.findAll(justParameters)

                // aggregate matched parameters into list
                val params = parametersMatches.asIterable().map { it.groupValues[1].trim() }.filter { it != ""}

                return RuleFunctionCall(functionCall, "d2:$functionName", params)
            } ?: throw NullPointerException("functionCall == null")

        }

    }
}