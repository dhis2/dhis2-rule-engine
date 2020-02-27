package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleExpression;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import java.util.Map;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class RuleFunctionHasValue
    extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        Map<String, RuleVariableValue> valueMap = visitor.getValueMap();

        String variableName = RuleExpression.getProgramRuleVariable( ctx );
        RuleVariableValue variableValue = valueMap.get( variableName );

        if ( variableValue == null )
        {
            return String.valueOf( false );
        }

        return String.valueOf( valueMap.get( variableName ).value() != null );
    }
}
