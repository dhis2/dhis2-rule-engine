package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Maps;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;
import org.hisp.dhis.rules.Utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.getLastUpdateDate;

@AutoValue
public abstract class RuleVariableNewestStageEvent
    extends RuleVariableDataElement
{

    @Nonnull
    public static RuleVariableNewestStageEvent create( @Nonnull String name, @Nonnull String dataElement,
        @Nonnull String programStage, @Nonnull RuleValueType dataElementType )
    {
        return new AutoValue_RuleVariableNewestStageEvent( name, dataElement,
            dataElementType, programStage );
    }

    @Nonnull
    public abstract String programStage();

    @Override
    public Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
        Map<String, List<RuleDataValue>> allEventValues,
        Map<String, RuleAttributeValue> currentEnrollmentValues,
        Map<String, RuleDataValue> currentEventValues )
    {
        Map<String, RuleVariableValue> valueMap = Maps.newHashMap();
        List<RuleDataValue> stageRuleDataValues = new ArrayList<>();
        List<RuleDataValue> sourceRuleDataValues = allEventValues.get( this.dataElement() );
        if ( sourceRuleDataValues != null && !sourceRuleDataValues.isEmpty() )
        {

            // filter data values based on program stage
            for ( int i = 0; i < sourceRuleDataValues.size(); i++ )
            {
                RuleDataValue ruleDataValue = sourceRuleDataValues.get( i );
                if ( this.programStage().equals( ruleDataValue.programStage() ) )
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
            valueMap.put( this.name(), RuleVariableValue.create( stageRuleDataValues.get( 0 ).value(),
                this.dataElementType(), Utils.values( stageRuleDataValues ),
                getLastUpdateDate( stageRuleDataValues ) ) );
        }

        return valueMap;
    }
}
