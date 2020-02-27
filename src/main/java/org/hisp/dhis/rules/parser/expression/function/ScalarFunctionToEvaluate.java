package org.hisp.dhis.rules.parser.expression.function;

import org.hisp.dhis.antlr.AntlrExprItem;
import org.hisp.dhis.antlr.AntlrExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

/**
 * Scalar function that needs to be evaluated using the specific CommonExpressionVisitor.
 *
 * @author Enrico Colasante
 */
public abstract class ScalarFunctionToEvaluate
    implements AntlrExprItem
{
    /**
     * Finds the value of an expression function, evaluating arguments only
     * when necessary.
     *
     * @param ctx     the expression context
     * @param visitor the specific tree visitor
     * @return the value of the function, evaluating necessary args
     */
    public abstract Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor );

    /**
     * Override the default evaluate method using the specific visitor CommonExpressionVisitor.
     *
     * @param ctx     the expression context
     * @param visitor the specific tree visitor
     * @return the value of the function, evaluating necessary args
     */
    public Object evaluate( ExprContext ctx, AntlrExpressionVisitor visitor )
    {
        return evaluate( ctx, (CommonExpressionVisitor) visitor );
    }
}
