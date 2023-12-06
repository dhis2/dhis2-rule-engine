package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.UtilsKt.getLastUpdateDate;
import static org.hisp.dhis.rules.UtilsKt.values;

public record RuleVariableNewestStageEvent(
        @Nonnull String name,
        boolean useCodeForOptionSet,
        @Nonnull List<Option> options,
        @Nonnull String dataElement,
        @Nonnull RuleValueType dataElementType,
        @Nonnull String programStage
) implements RuleVariableDataElement {

    @Nonnull
    public static RuleVariableNewestStageEvent create(@Nonnull String name, @Nonnull String dataElement,
                                                      @Nonnull String programStage, @Nonnull RuleValueType dataElementType, boolean useCodeForOptionSet, List<Option> options) {
        return new RuleVariableNewestStageEvent(name, useCodeForOptionSet, options, dataElement,
                dataElementType, programStage);
    }

    @Override
    public Map<String, RuleVariableValue> createValues(RuleVariableValueMapBuilder builder,
                                                       Map<String, List<RuleDataValue>> allEventValues,
                                                       Map<String, RuleAttributeValue> currentEnrollmentValues,
                                                       Map<String, RuleDataValue> currentEventValues) {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();
        List<RuleDataValue> stageRuleDataValues = new ArrayList<>();
        List<RuleDataValue> sourceRuleDataValues = allEventValues.get(this.dataElement());
        if (sourceRuleDataValues != null && !sourceRuleDataValues.isEmpty()) {

            // filter data values based on program stage
            for (RuleDataValue ruleDataValue : sourceRuleDataValues) {
                if (this.programStage().equals(ruleDataValue.programStage())) {
                    stageRuleDataValues.add(ruleDataValue);
                }
            }
        }

        if (stageRuleDataValues.isEmpty()) {
            valueMap.put(this.name(), RuleVariableValue.create(this.dataElementType()));
        } else {
            RuleVariableValue variableValue;

            RuleDataValue value = stageRuleDataValues.get(0);

            String optionValue = this.useCodeForOptionSet() ? value.value() : getOptionName(value.value());

            variableValue = RuleVariableValue.create(optionValue,
                    this.dataElementType(), values(stageRuleDataValues),
                    getLastUpdateDate(stageRuleDataValues));

            valueMap.put(this.name(), variableValue);
        }

        return valueMap;
    }
}
