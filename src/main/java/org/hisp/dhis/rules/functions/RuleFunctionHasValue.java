package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleExpression;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import java.util.Map;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor.DEFAULT_BOOLEAN_VALUE;

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

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        ctx

        visitor.getItemDescriptions().put( ctx.getText(), "" );

        return DEFAULT_BOOLEAN_VALUE;
    }
}
