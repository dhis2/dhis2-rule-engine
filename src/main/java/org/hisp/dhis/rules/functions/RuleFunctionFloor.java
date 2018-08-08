package org.hisp.dhis.rules.functions;

import org.hisp.dhis.rules.RuleVariableValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

final class RuleFunctionFloor
    extends RuleFunction
{
        static final String D2_FLOOR = "d2:floor";

        @Nonnull
        public static RuleFunctionFloor create()
        {
                return new RuleFunctionFloor();
        }

        @Nonnull
        @Override
        public String evaluate( @Nonnull List<String> arguments,
            Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
        {
                if ( arguments.size() != 1 )
                {
                        throw new IllegalArgumentException( "One argument was expected, " +
                            arguments.size() + " were supplied" );
                }

                return String.valueOf( (long) Math.floor( toDouble( arguments.get( 0 ), 0.0 ) ) );
        }
}
