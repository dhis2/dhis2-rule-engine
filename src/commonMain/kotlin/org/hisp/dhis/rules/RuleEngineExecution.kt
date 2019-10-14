package org.hisp.dhis.rules


import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEffect

expect class RuleEngineExecution(expressionEvaluator: RuleExpressionEvaluator,
                                 rules: List<Rule>,
                                 valueMap: Map<String, RuleVariableValue>,
                                 supplementaryData: Map<String, List<String>>) {

    fun call(): List<RuleEffect>

}
