package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.UtilsKt.getLastUpdateDate;
import static org.hisp.dhis.rules.UtilsKt.values;

public record RuleVariableNewestEvent(
        @Nonnull String name,
        boolean useCodeForOptionSet,
        @Nonnull List<Option> options,
        @Nonnull String dataElement,
        @Nonnull RuleValueType dataElementType
) implements RuleVariableDataElement {

    @Nonnull
    public static RuleVariableNewestEvent create(@Nonnull String name,
                                                 @Nonnull String dataElement, @Nonnull RuleValueType dataElementValueType, boolean useCodeForOptionSet, List<Option> options) {
        return new RuleVariableNewestEvent(name, useCodeForOptionSet, options, dataElement, dataElementValueType);
    }

    @Override
    public Map<String, RuleVariableValue> createValues(RuleVariableValueMapBuilder builder,
                                                       Map<String, List<RuleDataValue>> allEventValues,
                                                       Map<String, RuleAttributeValue> currentEnrollmentValues,
                                                       Map<String, RuleDataValue> currentEventValues) {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();
        List<RuleDataValue> ruleDataValues = allEventValues.get(this.dataElement());

        if (ruleDataValues == null || ruleDataValues.isEmpty()) {
            valueMap.put(this.name(), RuleVariableValue.create(this.dataElementType()));
        } else {
            RuleVariableValue variableValue;

            RuleDataValue value = ruleDataValues.get(0);

            String optionValue = this.useCodeForOptionSet() ? value.value() : getOptionName(value.value());

            variableValue = RuleVariableValue.create(optionValue,
                    this.dataElementType(), values(ruleDataValues), getLastUpdateDate(ruleDataValues));


            valueMap.put(this.name(), variableValue);
        }

        return valueMap;
    }
}
