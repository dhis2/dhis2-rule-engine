package org.hisp.dhis.rules.util;

import org.hisp.dhis.rules.models.RuleAction;

import javax.annotation.Nonnull;

public class MockRuleAction implements RuleAction {
    @Nonnull
    @Override
    public String data() {
        return null;
    }
}
