package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;
import org.hisp.dhis.rules.Utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.getLastUpdateDate;
import static org.hisp.dhis.rules.Utils.getLastUpdateDateForHistory;

@AutoValue
public abstract class RuleVariableNewestStageEvent
    extends RuleVariableDataElement
{

    @Nonnull
    public static RuleVariableNewestStageEvent create(@Nonnull String name, @Nonnull String dataElement,
                                                      @Nonnull String programStage, @Nonnull RuleValueType dataElementType, boolean useCodeForOptionSet, List<Option> options)
    {
        return new AutoValue_RuleVariableNewestStageEvent( name, useCodeForOptionSet, options, dataElement,
            dataElementType, programStage );
    }

    @Nonnull
    public abstract String programStage();

    @Override
    public Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
        Map<String, List<RuleDataValueHistory>> allEventValues,
        Map<String, RuleAttributeValue> currentEnrollmentValues,
        Map<String, RuleDataValue> currentEventValues )
    {
        Map<String, RuleVariableValue> valueMap = new HashMap();
        List<RuleDataValueHistory> stageRuleDataValues = new ArrayList<>();
        List<RuleDataValueHistory> sourceRuleDataValues = allEventValues.get( this.dataElement() );
        if ( sourceRuleDataValues != null && !sourceRuleDataValues.isEmpty() )
        {

            // filter data values based on program stage
            for ( int i = 0; i < sourceRuleDataValues.size(); i++ )
            {
                RuleDataValueHistory ruleDataValue = sourceRuleDataValues.get( i );
                if ( this.programStage().equals( ruleDataValue.getProgramStage() ) )
                {
                    stageRuleDataValues.add( ruleDataValue );
                }
            }
        }

        if ( stageRuleDataValues.isEmpty() )
        {
            valueMap.put( this.name(), RuleVariableValue.create( this.dataElementType() ) );
        }
        else
        {
            RuleVariableValue variableValue;

            RuleDataValueHistory value = stageRuleDataValues.get( 0 );

            String optionValue = this.useCodeForOptionSet() ? value.getValue() : getOptionName( value.getValue() );

            variableValue = RuleVariableValue.create( optionValue,
                        this.dataElementType(), Utils.valuesForHistory( stageRuleDataValues ),
                        getLastUpdateDateForHistory( stageRuleDataValues ) );

            valueMap.put( this.name(), variableValue );
        }

        return valueMap;
    }
}
