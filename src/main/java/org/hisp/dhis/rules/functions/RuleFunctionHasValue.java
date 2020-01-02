package org.hisp.dhis.rules.functions;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.function.SimpleNoSqlFunction;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.RuleVariableValue;

import java.util.Map;

public class RuleFunctionHasValue
    extends SimpleNoSqlFunction
{
        @Override
        public Object evaluate( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
        {
                Map<String, RuleVariableValue> valueMap = visitor.getValueMap();

                String variableName = visitor.castStringVisit( ctx.expr( 0 ) ).replace( "'", "" );
                RuleVariableValue variableValue = valueMap.get( variableName );

                if ( variableValue == null )
                {
                        return String.valueOf( false );
                }

                return String.valueOf( valueMap.get( variableName ).value() != null );
        }
}
