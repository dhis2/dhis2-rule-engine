package org.hisp.dhis.rules.functions;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.parser.expression.function.SimpleNoSqlFunction;

import static org.apache.commons.lang3.math.NumberUtils.toDouble;

public class RuleFunctionFloor
    extends SimpleNoSqlFunction
{
        @Override
        public Object evaluate( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
        {
                return String.valueOf( (long) Math.floor( toDouble( visitor.castStringVisit( ctx.expr( 0 ) ), 0.0 ) ) );
        }
}
