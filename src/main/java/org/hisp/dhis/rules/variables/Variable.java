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
        String variable = variableValue.value() == null ?
            variableValue.type().defaultValue() : variableValue.value();

        if ( variable == null )
        {
            throw new ParserExceptionWithoutContext( "Variable " + RuleExpression.getProgramRuleVariable( ctx ) + " not present" );
        }

        return variable;
    }
}
