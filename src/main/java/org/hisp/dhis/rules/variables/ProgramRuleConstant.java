package org.hisp.dhis.rules.variables;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class ProgramRuleConstant
    extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        RuleVariableValue variableValue = visitor.getValueMap().get( ctx.uid0.getText() );

        if ( variableValue == null )
        {
            throw new ParserExceptionWithoutContext( "Variable " + ctx.uid0.getText() + " not present" );
        }

        return variableValue.value() == null ?
            variableValue.type().defaultValue() : getValueCastedByType( variableValue );
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String variable = ctx.uid0.getText();

        if ( visitor.getItemStore().containsKey( variable ) )
        {
            visitor.getItemDescriptions().put( ctx.getText(), visitor.getItemStore().get( variable ).getDisplayName() );

            return visitor.getItemStore().get( ctx.uid0.getText() ).getValueType().getValue();
        }

        throw new ParserExceptionWithoutContext(
            "Variable " + ctx.uid0.getText() + " does not exist" );
    }
}
