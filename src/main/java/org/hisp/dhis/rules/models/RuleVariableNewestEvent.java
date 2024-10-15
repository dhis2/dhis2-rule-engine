package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;
import org.hisp.dhis.rules.Utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.getLastUpdateDate;
import static org.hisp.dhis.rules.Utils.getLastUpdateDateForHistory;

@AutoValue
public abstract class RuleVariableNewestEvent
    extends RuleVariableDataElement
{

    @Nonnull
    public static RuleVariableNewestEvent create(@Nonnull String name,
                                                 @Nonnull String dataElement, @Nonnull RuleValueType dataElementValueType, boolean useCodeForOptionSet, List<Option> options)
    {
        return new AutoValue_RuleVariableNewestEvent( name, useCodeForOptionSet, options, dataElement, dataElementValueType );
    }

    @Override
    public Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
        Map<String, List<RuleDataValueHistory>> allEventValues,
        Map<String, RuleAttributeValue> currentEnrollmentValues,
        Map<String, RuleDataValue> currentEventValues )
    {
        Map<String, RuleVariableValue> valueMap = new HashMap();
        List<RuleDataValueHistory> ruleDataValues = allEventValues.get( this.dataElement() );

        if ( ruleDataValues == null || ruleDataValues.isEmpty() )
        {
            valueMap.put( this.name(), RuleVariableValue.create( this.dataElementType() ) );
        }
        else
        {
            RuleVariableValue variableValue;

            RuleDataValueHistory value = ruleDataValues.get( 0 );

            String optionValue = this.useCodeForOptionSet() ? value.getValue() : getOptionName( value.getValue() );

            variableValue = RuleVariableValue.create( optionValue,
                this.dataElementType(), Utils.valuesForHistory( ruleDataValues ), getLastUpdateDateForHistory( ruleDataValues ) );


            valueMap.put( this.name(), variableValue );
        }

        return valueMap;
    }
}
