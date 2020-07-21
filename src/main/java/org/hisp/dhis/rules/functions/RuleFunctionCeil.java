package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.antlr.AntlrParserUtils.castDouble;

public class RuleFunctionCeil
    extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return String.valueOf( (long) Math.ceil( toDouble( visitor.castStringVisit( ctx.expr( 0 ) ), 0.0 ) ) );
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        castDouble( visitor.visit( ctx.expr( 0 ) ) );
        return CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
    }
}
