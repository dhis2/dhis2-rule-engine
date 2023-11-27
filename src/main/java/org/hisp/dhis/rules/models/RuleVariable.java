package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public interface RuleVariable
{
    /**
     * @return Name of the variable. Something what users refer to
     * when building program rules.
     */
    @Nonnull
    String name();

    boolean useCodeForOptionSet();

    @Nonnull
    List<Option> options();

    Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
        Map<String, List<RuleDataValue>> allEventValues,
        Map<String, RuleAttributeValue> currentEnrollmentValues,
        Map<String, RuleDataValue> currentEventValues );

    default String getOptionName( String value )
    {
        // if no option found then existing value in the context will be used
        if ( options() == null || options().isEmpty() )
        {
            return value;
        }
        return options().stream()
                .filter( op -> Objects.equals( value, op.code() ) )
                .map( Option::name)
                .findAny()
                .orElse( value );
    }
}
