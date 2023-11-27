package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.getLastUpdateDate;

public record RuleVariableCurrentEvent(
        @Nonnull String name,
        boolean useCodeForOptionSet,
        @Nonnull List<Option> options,
        @Nonnull String dataElement,
        @Nonnull RuleValueType dataElementType
) implements RuleVariableDataElement {

    @Nonnull
    public static RuleVariableCurrentEvent create(@Nonnull String name,
                                                  @Nonnull String dataElement, @Nonnull RuleValueType dataElementValueType, boolean useCodeForOptionSet, List<Option> options) {
        return new RuleVariableCurrentEvent(name, useCodeForOptionSet, options, dataElement, dataElementValueType);
    }

    @Override
    public Map<String, RuleVariableValue> createValues(RuleVariableValueMapBuilder builder,
                                                       Map<String, List<RuleDataValue>> allEventValues,
                                                       Map<String, RuleAttributeValue> currentEnrollmentValues,
                                                       Map<String, RuleDataValue> currentEventValues) {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();

        RuleVariableValue variableValue;

        if (currentEventValues.containsKey(this.dataElement())) {
            RuleDataValue value = currentEventValues.get(this.dataElement());

            String optionValue = this.useCodeForOptionSet() ? value.value() : getOptionName(value.value());

            variableValue = RuleVariableValue.create(optionValue, this.dataElementType(),
                    List.of(optionValue), getLastUpdateDate(List.of(value)));
        } else {
            variableValue = RuleVariableValue.create(this.dataElementType());
        }

        valueMap.put(this.name(), variableValue);
        return valueMap;
    }
}
