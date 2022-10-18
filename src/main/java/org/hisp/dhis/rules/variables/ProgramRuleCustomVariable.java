package org.hisp.dhis.rules.variables;

import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.RuleExpression;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

public class ProgramRuleCustomVariable
    extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        RuleVariableValue variableValue = visitor.getValueMap().get( ctx.programRuleVariableName().getText() );

        if ( variableValue == null )
        {
            throw new ParserExceptionWithoutContext(
                "Variable " + ctx.programRuleVariableName().getText() + " not present" );
        }

        return variableValue.value() == null ?
                variableValue.type().defaultValue() : getValueCastedByType( variableValue );
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String variable = null;

        if ( ctx.programRuleVariableName() != null )
        {
            variable = ctx.programRuleVariableName().getText();
        }
        else if ( ctx.programRuleStringVariableName() != null )
        {
            variable = ctx.programRuleStringVariableName().getText().replace("\'","");
        }

        if ( visitor.getItemStore().containsKey( variable ) )
        {
            visitor.getItemDescriptions().put( ctx.getText(), visitor.getItemStore().get( variable ).getDisplayName() );

            return visitor.getItemStore().get( variable ).getValueType().getValue();
        }

        throw new ParserExceptionWithoutContext(
            "Variable " + variable + " does not exist" );
    }
}
