package org.hisp.dhis.rules;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
abstract class RuleExpression
{
        static final String VARIABLE_PATTERN = "[AXCV]\\{([\\w -_.]+)\\}";

        static final Pattern VARIABLE_PATTERN_COMPILED = Pattern.compile( VARIABLE_PATTERN );

        @Nonnull
        public abstract Set<String> variables();

        @Nonnull
        static String unwrapVariableName( @Nonnull String variable )
        {
                Matcher variableNameMatcher = VARIABLE_PATTERN_COMPILED.matcher( variable );

                // extract variable name
                if ( variableNameMatcher.find() )
                {
                        return variableNameMatcher.group( 1 );
                }

                throw new IllegalArgumentException( "Malformed variable: " + variable );
        }

        @Nonnull
        static String unwrapVariableNameOrReturnOriginal( @Nonnull String variable )
        {
                try
                {
                        return unwrapVariableName( variable );
                } catch ( IllegalArgumentException e ) {
                        return variable;
                }
        }
}