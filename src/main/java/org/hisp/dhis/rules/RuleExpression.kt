package org.hisp.dhis.rules

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import java.util.regex.Pattern

class RuleExpression(val expression: String,
                     val variable: PersistentSet<String>,
                     val functions: PersistentSet<String>) {

    companion object {
        private val VARIABLE_PATTERN = "[A#CV]\\{([\\w -_.]+)\\}".toRegex()

        val FUNCTION_PATTERN = ("d2:(\\w+.?\\w*)\\( *(([\\d/\\*\\+\\-%\\. ]+)|" +
                "( *'[^']*'))*( *, *(([\\d/\\*\\+\\-%\\. ]+)|'[^']*'))* *\\)").toRegex()


        // TODO remove this when the classes where its called are migrated to Kotlin
        @JvmField val FUNCTION_PATTERN_COMPILED = Pattern.compile(FUNCTION_PATTERN.toString())!!

        @JvmStatic
        fun unwrapVariableName(variable: String): String {

            val variableNameMatcher = VARIABLE_PATTERN.find(variable)
            variableNameMatcher?.let {
                if (it.groups.isEmpty())
                    throw IllegalArgumentException("Malformed variable: $variable")
            } ?: throw IllegalArgumentException("Malformed variable: $variable")
            return variableNameMatcher.groupValues[1]
        }

        @JvmStatic
        fun from(expression: String?): RuleExpression {
            expression?.let {
                val variables = hashSetOf<String>()
                val functions = hashSetOf<String>()

                val variableMatcher = VARIABLE_PATTERN.findAll(it)
                val functionMatcher = FUNCTION_PATTERN.findAll(it)

                // iterate over matched values and aggregate them
                variableMatcher.asSequence().forEach { result -> variables.add(result.value) }
                functionMatcher.asSequence().forEach { result -> functions.add(result.value) }


                return RuleExpression(it, variables.toPersistentSet(), functions.toPersistentSet())
            } ?: throw NullPointerException("expression == null")

        }

    }
}