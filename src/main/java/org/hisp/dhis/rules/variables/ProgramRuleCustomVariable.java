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
        String variable = variableValue.value() == null ?
            variableValue.type().defaultValue() : variableValue.value();

        if ( variable == null )
        {
            throw new ParserExceptionWithoutContext(
                "Variable " + ctx.programRuleVariableName().getText() + " not present" );
        }

        return variable;
    }

    @Override
    public Object getDescription( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        String variable = ctx.programRuleVariableName().getText();

        if ( visitor.getItemStore().containsKey( variable ) )
        {
            visitor.getItemDescriptions().put( ctx.getText(), visitor.getItemStore().get( variable ).getDisplayName() );

            return visitor.getItemStore().get( ctx.programRuleVariableName().getText() ).getValueType().getValue();
        }

        throw new ParserExceptionWithoutContext(
            "Variable " + ctx.programRuleVariableName().getText() + " does not exist present" );
    }
}
