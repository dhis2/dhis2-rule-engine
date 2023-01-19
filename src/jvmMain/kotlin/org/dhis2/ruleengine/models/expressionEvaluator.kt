package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.DataItem
import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.ParserUtils.FUNCTION_EVALUATE
import org.dhis2.ruleengine.parser.expression.ParserUtils.FUNCTION_FOR_DESCRIPTION
import org.dhis2.ruleengine.utils.FUNCTIONS
import org.hisp.dhis.antlr.AntlrParserUtils
import org.hisp.dhis.antlr.Parser

actual fun expressionEvaluator(): ExpressionParserEvaluator {
    return object : ExpressionParserEvaluator {
        override fun evaluate(
            condition: String,
            valueMap: Map<String, RuleVariableValue>,
            supplementaryData: Map<String, List<String>>
        ): String {
            if (condition.isEmpty()) {
                return ""
            }

            val commonExpressionVisitor: CommonExpressionVisitor = CommonExpressionVisitor.newBuilder()
                .withFunctionMap(FUNCTIONS)
                .withFunctionMethod(FUNCTION_EVALUATE)
                .withVariablesMap(valueMap)
                .withSupplementaryData(supplementaryData)
                .validateCommonProperties()
            val result =
                Parser.visit(condition, commonExpressionVisitor, !isOldAndroidVersion(valueMap, supplementaryData))
            return convertInteger(result).toString()

        }

        override fun getExpressionDescription(
            expression: String,
            dataItemStore: Map<String, DataItem>,
            castAsBoolean: Boolean
        ): String {
            val itemDescriptions = mutableMapOf<String, String>()

            val visitor = CommonExpressionVisitor.newBuilder()
                .withIteamStore(dataItemStore)
                .withFunctionMethod(FUNCTION_FOR_DESCRIPTION)
                .withFunctionMap(FUNCTIONS)
                .withItemDescriptions(itemDescriptions)
                .validateAndBuildForDescription()

            if (castAsBoolean) {
                AntlrParserUtils.castClass(Boolean::class.java, Parser.visit(expression, visitor))
            } else {
                Parser.visit(expression, visitor)
            }

            var description = expression

            itemDescriptions.forEach { (key, value) ->
                description = description.replace(key, value)
            }

            return description
        }

        private fun isOldAndroidVersion(
            valueMap: Map<String, RuleVariableValue>,
            supplementaryData: Map<String, List<String>>
        ): Boolean {
            return valueMap.containsKey("environment") && valueMap["environment"]!!.value == getClientName().clientName &&
                    supplementaryData.containsKey("android_version") && supplementaryData["android_version"]!![0].toInt() < 21
        }

        private fun convertInteger(result: Any): Any {
            return if (result is Double && result % 1 == 0.0) {
                result.toInt()
            } else result
        }

    }


}