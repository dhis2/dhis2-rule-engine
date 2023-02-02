package org.hisp.dhis.rules.util;

import org.hisp.dhis.rules.models.RuleAttributeValue;

import javax.annotation.Nonnull;

public class MockRuleAttributeValue extends RuleAttributeValue {
    @Nonnull
    @Override
    public String trackedEntityAttribute() {
        return null;
    }

    @Nonnull
    @Override
    public String value() {
        return null;
    }
}
