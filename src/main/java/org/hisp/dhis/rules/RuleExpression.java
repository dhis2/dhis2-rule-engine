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
    static final String VARIABLE_PATTERN = "[#]\\{([\\w -_.]+)\\}";

    static final Pattern VARIABLE_PATTERN_COMPILED = Pattern.compile( VARIABLE_PATTERN );

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

    /* This method should probably be removed creating a new prefix for program rule variables that is
     *  not shared with indicators.*/
    @Nonnull
    public static String getProgramRuleVariable( ExpressionParser.ExprContext ctx )
    {
        return isProgramRuleVariable(ctx)
            ? getProgramRuleVariableText(ctx)
            : ctx.uid0.getText() + secondPart( ctx ) + thirdPart( ctx );
    }

    private static String getProgramRuleVariableText( ExpressionParser.ExprContext ctx )
    {
        if ( ctx.programRuleVariableName() != null ) {
            return ctx.programRuleVariableName().getText();
        }
        else
        {
            return ctx.STRING_LITERAL().getText().replaceAll( "\'", "" );
        }
    }

    private static boolean isProgramRuleVariable( ExpressionParser.ExprContext ctx )
    {
        return ctx.programRuleVariableName() != null || ctx.STRING_LITERAL() != null;
    }

    private static String secondPart( ExpressionParser.ExprContext ctx )
    {
        if ( ctx.uid1 != null )
        {
            return "." + ctx.uid1.getText();
        }
        else if ( ctx.wild1 != null )
        {
            return ctx.wild1.getText();
        }
        return "";
    }

    private static String thirdPart( ExpressionParser.ExprContext ctx )
    {
        if ( ctx.uid2 != null && ctx.uid1 == null )
        {
            return ".*." + ctx.uid2.getText();
        }
        else if ( ctx.uid2 != null )
        {
            return "." + ctx.uid2.getText();
        }
        else if ( ctx.wild2 != null )
        {
            return ctx.wild2.getText();
        }
        return "";
    }

    @Nonnull
    public abstract Set<String> variables();
}
