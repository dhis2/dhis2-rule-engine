package org.hisp.dhis.parser.expression.function;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.ParserException;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;

public abstract class SimpleNoSqlFunction extends SimpleScalarFunction
{
    @Override
    public Object getSql( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
    {
        throw new ParserException("SQL generation is not allowed");
    }
}
