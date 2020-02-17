package org.hisp.dhis.rules;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
public abstract class RuleExpression
{
        static final String VARIABLE_PATTERN = "[A#CV$]\\{([\\w -_.]+)\\}";

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
        public static String getProgramRuleVariable( ExpressionParser.ExprContext ctx )
        {
                return ctx.programRuleVariableName() != null
                    ? ctx.programRuleVariableName().getText()
                    : ctx.uid0.getText() + "." + ctx.uid1.getText();
        }
}
