package org.dhis2.ruleengine.models

import org.dhis2.ruleengine.RuleVariableValue

actual fun expressionEvaluator(): ExpressionParserEvaluator {
   return object: ExpressionParserEvaluator{
       override fun evaluate(
           condition: String,
           valueMap: Map<String, RuleVariableValue>,
           supplementaryData: Map<String, List<String>>
       ): String {
           return ""
       }

   }
}