package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Maps;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.getLastUpdateDate;

@AutoValue
public abstract class RuleVariableCurrentEvent
    extends RuleVariableDataElement
{

        @Nonnull
        public static RuleVariableCurrentEvent create( @Nonnull String name,
            @Nonnull String dataElement, @Nonnull RuleValueType dataElementValueType )
        {
                return new AutoValue_RuleVariableCurrentEvent( name, dataElement, dataElementValueType );
        }

        @Override
        public Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
            Map<String, List<RuleDataValue>> allEventValues,
            Map<String, RuleAttributeValue> currentEnrollmentValues,
            Map<String, RuleDataValue> currentEventValues ) {
                Map<String, RuleVariableValue> valueMap = Maps.newHashMap();
                if (builder.ruleEvent == null) {
                        return valueMap;
                }

                RuleVariableValue variableValue;

                if (currentEventValues.containsKey(this.dataElement())) {
                        RuleDataValue value = currentEventValues.get(this.dataElement());
                        variableValue = RuleVariableValue.create(value.value(), this.dataElementType(),
                            Arrays.asList(value.value()), getLastUpdateDate(Arrays.asList(value)));
                } else {
                        variableValue = RuleVariableValue.create(this.dataElementType());
                }

                valueMap.put(this.name(), variableValue);
                return valueMap;
        }
}
