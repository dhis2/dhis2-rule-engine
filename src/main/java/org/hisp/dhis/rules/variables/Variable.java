package org.hisp.dhis.rules.variables;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.RuleExpression;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class Variable
    extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        RuleVariableValue variableValue = visitor.getValueMap().get( RuleExpression.getProgramRuleVariable( ctx ) );

        if ( variableValue == null )
        {
            throw new ParserExceptionWithoutContext(
                "Variable " + RuleExpression.getProgramRuleVariable( ctx ) + " not present" );
        }

        return variableValue.value() == null ?
                variableValue.type().defaultValue() : getValueCastedByType( variableValue );
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String variable = RuleExpression.getProgramRuleVariable( ctx );

        if ( visitor.getItemStore().containsKey( variable ) )
        {
            visitor.getItemDescriptions().put( ctx.getText(), visitor.getItemStore().get( variable ).getDisplayName() );

            return visitor.getItemStore().get( RuleExpression.getProgramRuleVariable( ctx ) ).getValueType().getValue();
        }

        throw new ParserExceptionWithoutContext(
            "Variable " + RuleExpression.getProgramRuleVariable( ctx ) + " does not exist" );
    }
}
