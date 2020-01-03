package org.hisp.dhis.rules.variables;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.ParserExceptionWithoutContext;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.parser.expression.function.SimpleNoSqlFunction;
import org.hisp.dhis.rules.RuleVariableValue;

public class ProgramRuleVariable
    extends SimpleNoSqlFunction
{
    @Override
    public Object evaluate( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
    {
        RuleVariableValue variableValue = visitor.getValueMap().get( ctx.fun.getText() );
        String variable = variableValue.value() == null ?
            variableValue.type().defaultValue() : variableValue.value();

        if ( variable == null )
        {
            throw new ParserExceptionWithoutContext( "Variable " + ctx.fun.getText() + " not present" );
        }

        return variable;
    }
}
