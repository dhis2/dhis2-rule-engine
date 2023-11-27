package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;
import org.hisp.dhis.rules.Utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.Utils.getLastUpdateDateForPrevious;

public record RuleVariablePreviousEvent(
        @Nonnull String name,
        boolean useCodeForOptionSet,
        @Nonnull List<Option> options,
        @Nonnull String dataElement,
        @Nonnull RuleValueType dataElementType
) implements RuleVariableDataElement {

    @Nonnull
    public static RuleVariablePreviousEvent create(@Nonnull String name,
                                                   @Nonnull String dataElement, @Nonnull RuleValueType valueType, boolean useCodeForOptionSet, List<Option> options) {
        return new RuleVariablePreviousEvent(name, useCodeForOptionSet, options, dataElement, valueType);
    }

    @Override
    public Map<String, RuleVariableValue> createValues(RuleVariableValueMapBuilder builder,
                                                       Map<String, List<RuleDataValue>> allEventValues,
                                                       Map<String, RuleAttributeValue> currentEnrollmentValues,
                                                       Map<String, RuleDataValue> currentEventValues) {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();

        RuleVariableValue variableValue = null;
        List<RuleDataValue> ruleDataValues = allEventValues.get(this.dataElement());
        if (builder.ruleEvent != null && ruleDataValues != null && !ruleDataValues.isEmpty()) {
            for (RuleDataValue ruleDataValue : ruleDataValues) {
                // We found preceding value to the current currentEventValues,
                // which is assumed to be best candidate.
                if (builder.ruleEvent.eventDate().compareTo(ruleDataValue.eventDate()) > 0) {
                    String optionValue = this.useCodeForOptionSet() ? ruleDataValue.value() : getOptionName(ruleDataValue.value());

                    variableValue = RuleVariableValue.create(optionValue, this.dataElementType(),
                            Utils.values(ruleDataValues),
                            getLastUpdateDateForPrevious(ruleDataValues, builder.ruleEvent));
                    break;
                }
            }
        }

        if (variableValue == null) {
            variableValue = RuleVariableValue.create(this.dataElementType());
        }

        valueMap.put(this.name(), variableValue);
        return valueMap;
    }
}
