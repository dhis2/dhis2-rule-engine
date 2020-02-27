package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class RuleFunctionCeil
    extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return String.valueOf( (long) Math.ceil( toDouble( visitor.castStringVisit( ctx.expr( 0 ) ), 0.0 ) ) );
    }
}
