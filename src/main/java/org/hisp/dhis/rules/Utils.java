package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleDataValueHistory;
import org.hisp.dhis.rules.models.RuleEvent;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public final class Utils
{

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.US );
    static final String VARIABLE_PATTERN = "[#]\\{([\\w -_.]+)\\}";
    static final Pattern VARIABLE_PATTERN_COMPILED = Pattern.compile( VARIABLE_PATTERN );

    private Utils()
    {
        // no instances
    }

    public static List<String> values( List<RuleDataValue> ruleDataValues )
    {
        List<String> values = new ArrayList<>( ruleDataValues.size() );
        for ( RuleDataValue ruleDataValue : ruleDataValues )
        {
            values.add( ruleDataValue.value() );
        }
        return Collections.unmodifiableList( values );
    }

    public static List<String> valuesForHistory( List<RuleDataValueHistory> ruleDataValues )
    {
        List<String> values = new ArrayList<>( ruleDataValues.size() );
        for ( RuleDataValueHistory ruleDataValue : ruleDataValues )
        {
            values.add( ruleDataValue.getValue() );
        }
        return Collections.unmodifiableList( values );
    }

    public static String getLastUpdateDateForPrevious( List<RuleDataValueHistory> ruleDataValues,
        RuleEvent ruleEvent )
    {
        List<Date> dates = new ArrayList<>();
        for ( RuleDataValueHistory date : ruleDataValues )
        {
            Date d = date.getEventDate();
            if ( d.before( ruleEvent.eventDate() ) ||
                    (ruleEvent.eventDate().equals(d) && ruleEvent.createdDate().after(date.getCreatedDate()))
            )
            {
                dates.add( d );
            }
        }

        return dateFormat.format( Collections.max( dates ) );
    }

    public static String getLastUpdateDateForHistory( List<RuleDataValueHistory> ruleDataValues )
    {
        List<Date> dates = new ArrayList<>();
        for ( RuleDataValueHistory date : ruleDataValues )
        {
            Date d = date.getEventDate();
            dates.add( d );
        }

        return dateFormat.format( Collections.max( dates ) );
    }

    public static String getLastUpdateDate( List<RuleDataValue> ruleDataValues )
    {
        List<Date> dates = new ArrayList<>();
        for ( RuleDataValue date : ruleDataValues )
        {
            Date d = date.eventDate();
            dates.add( d );
        }

        return dateFormat.format( Collections.max( dates ) );
    }

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
}
