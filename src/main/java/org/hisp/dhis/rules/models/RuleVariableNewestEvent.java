package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Maps;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;
import org.hisp.dhis.rules.Utils;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hisp.dhis.rules.RuleVariableValue.create;
import static org.hisp.dhis.rules.Utils.getLastUpdateDate;

@AutoValue
public abstract class RuleVariableNewestEvent
    extends RuleVariableDataElement
{

        @Nonnull
        public static RuleVariableNewestEvent create( @Nonnull String name,
            @Nonnull String dataElement, @Nonnull RuleValueType dataElementValueType )
        {
                return new AutoValue_RuleVariableNewestEvent( name, dataElement, dataElementValueType );
        }

        @Override
        public Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
            Map<String, List<RuleDataValue>> allEventValues,
            Map<String, RuleAttributeValue> currentEnrollmentValues,
            Map<String, RuleDataValue> currentEventValues ) {
                Map<String, RuleVariableValue> valueMap = Maps.newHashMap();
                List<RuleDataValue> ruleDataValues = allEventValues.get(this.dataElement());

                if (ruleDataValues == null || ruleDataValues.isEmpty()) {
                        valueMap.put(this.name(), RuleVariableValue.create(this.dataElementType()));
                } else {
                        valueMap.put(this.name(), RuleVariableValue.create(ruleDataValues.get(0).value(),
                            this.dataElementType(), Utils.values(ruleDataValues), getLastUpdateDate(ruleDataValues)));
                }
                return valueMap;
        }
}
