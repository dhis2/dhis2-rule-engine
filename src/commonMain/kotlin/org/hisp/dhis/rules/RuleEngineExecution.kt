package org.hisp.dhis.rules


import org.hisp.dhis.rules.models.Rule
import org.hisp.dhis.rules.models.RuleEffect
import org.hisp.dhis.rules.utils.Callable

expect class RuleEngineExecution(expressionEvaluator: RuleExpressionEvaluator,
                                 rules: List<Rule>,
                                 valueMap: Map<String, RuleVariableValue>,
                                 supplementaryData: Map<String, List<String>>): Callable<List<RuleEffect>> {

    override fun call(): List<RuleEffect>

}
