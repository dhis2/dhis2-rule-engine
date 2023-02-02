package org.hisp.dhis.rules.util;

import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;
import org.hisp.dhis.rules.models.RuleAttributeValue;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class MockRuleVariable extends RuleVariable {
    @Nonnull
    @Override
    public String name() {
        return null;
    }

    @Override
    public Map<String, RuleVariableValue> createValues(RuleVariableValueMapBuilder builder, Map<String, List<RuleDataValue>> allEventValues, Map<String, RuleAttributeValue> currentEnrollmentValues, Map<String, RuleDataValue> currentEventValues) {
        return Map.of();
    }
}
