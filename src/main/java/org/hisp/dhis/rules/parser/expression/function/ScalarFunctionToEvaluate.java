package org.hisp.dhis.rules.parser.expression.function;

import org.hisp.dhis.antlr.AntlrExprItem;
import org.hisp.dhis.antlr.AntlrExpressionVisitor;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.variables.ProgramRuleCustomVariable;
import org.hisp.dhis.rules.variables.ProgramRuleVariable;

import javax.annotation.Nonnull;

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

    /**
     * Finds the description of an expression function.
     *
     * @param ctx     the expression context
     * @param visitor the specific tree visitor
     * @return expression description
     */
    public abstract Object getDescription(  ExprContext ctx, CommonExpressionVisitor visitor );

    protected ScalarFunctionToEvaluate getProgramArgType( ExprContext ctx )
    {
        if ( ctx.programVariable() != null )
        {
            return new ProgramRuleVariable();
        }

        if ( ctx.programRuleVariableName() != null )
        {
            return new ProgramRuleCustomVariable();
        }

        if ( ctx.programRuleStringVariableName() != null )
        {
            return new ProgramRuleCustomVariable();
        }

        throw new ParserExceptionWithoutContext( "Illegal argument in program rule expression: " + ctx.getText() );
    }

    protected Object getValueCastedByType( @Nonnull RuleVariableValue value )
    {
        switch (value.type()) {
            case NUMERIC:
                return Double.parseDouble(value.value());
            case BOOLEAN:
                return Boolean.valueOf(value.value());
            default:
                return value.value();
        }
    }
}
