package org.hisp.dhis.rules.util;

import org.hisp.dhis.rules.models.RuleDataValue;

import javax.annotation.Nonnull;
import java.util.Date;

public class MockRuleDataValue extends RuleDataValue {
    @Nonnull
    @Override
    public Date eventDate() {
        return null;
    }

    @Nonnull
    @Override
    public String programStage() {
        return null;
    }

    @Nonnull
    @Override
    public String dataElement() {
        return null;
    }

    @Nonnull
    @Override
    public String value() {
        return null;
    }
}
