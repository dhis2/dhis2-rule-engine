package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.DataItem
import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.Expressions
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
            if(condition.isEmpty()) return ""
            return Expressions()
                .withValueMap(valueMap)
                .withSupplementaryData(supplementaryData)
                .eval(condition)
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
    }


}