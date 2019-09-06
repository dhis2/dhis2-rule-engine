package org.hisp.dhis.rules

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import kotlin.jvm.JvmStatic

class RuleExpression(val expression: String,
                     val variable: PersistentSet<String>,
                     val functions: PersistentSet<String>) {

    companion object {
        private val VARIABLE_PATTERN = "[A#CV]\\{([\\w -_.]+)}".toRegex()

        val FUNCTION_PATTERN = ("d2:(\\w+.?\\w*)\\( *(([\\d/\\*\\+\\-%\\. ]+)|" +
                "( *'[^']*'))*( *, *(([\\d/\\*\\+\\-%\\. ]+)|'[^']*'))* *\\)").toRegex()


        @JvmStatic fun unwrapVariableName(variable: String): String {

            val variableNameMatcher = VARIABLE_PATTERN.find(variable)
            variableNameMatcher?.let {
                if (it.groups.isEmpty())
                    throw IllegalArgumentException("Malformed variable: $variable")
            } ?: throw IllegalArgumentException("Malformed variable: $variable")
            return variableNameMatcher.groupValues[1]
        }

        @JvmStatic fun from(expression: String?): RuleExpression {
            expression?.let {
                val variables = hashSetOf<String>()
                val functions = hashSetOf<String>()

                val variableMatcher = VARIABLE_PATTERN.findAll(it)
                val functionMatcher = FUNCTION_PATTERN.findAll(it)

                // iterate over matched values and aggregate them
                variableMatcher.asIterable().map { result -> variables.add(result.groupValues[0]) }
                functionMatcher.asIterable().map { result -> functions.add(result.groupValues[0]) }


                return RuleExpression(it, variables.toPersistentSet(), functions.toPersistentSet())
            } ?: throw NullPointerException("expression == null")

        }

    }
}