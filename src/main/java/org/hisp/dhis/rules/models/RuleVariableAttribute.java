package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.dateFormat;

@AutoValue
public abstract class RuleVariableAttribute
    extends RuleVariable
{

    @Nonnull
    public static RuleVariableAttribute create(@Nonnull String name,
                                               @Nonnull String attribute, @Nonnull RuleValueType attributeType, boolean useCodeForOptionSet, List<Option> options)
    {
        return new AutoValue_RuleVariableAttribute( name, useCodeForOptionSet, options, attribute, attributeType );
    }

    @Nonnull
    public abstract String trackedEntityAttribute();

    @Nonnull
    public abstract RuleValueType trackedEntityAttributeType();

    @Override
    public Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
        Map<String, List<RuleDataValueHistory>> allEventValues,
        Map<String, RuleAttributeValue> currentEnrollmentValues,
        Map<String, RuleDataValue> currentEventValues )
    {
        Map<String, RuleVariableValue> valueMap = new HashMap();

        String currentDate = dateFormat.format( new Date() );

        RuleVariableValue variableValue;

        if ( currentEnrollmentValues.containsKey( this.trackedEntityAttribute() ) )
        {
            RuleAttributeValue value = currentEnrollmentValues
                .get( this.trackedEntityAttribute() );

            String optionValue = this.useCodeForOptionSet() ? value.value() : getOptionName( value.value() );

            variableValue = RuleVariableValue.create( optionValue, this.trackedEntityAttributeType(),
                    List.of(optionValue), currentDate );
        }
        else
        {
            variableValue = RuleVariableValue.create( this.trackedEntityAttributeType() );
        }

        valueMap.put( this.name(), variableValue );
        return valueMap;
    }
}
