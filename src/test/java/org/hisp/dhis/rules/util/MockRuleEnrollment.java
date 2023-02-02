package org.hisp.dhis.rules.util;

import org.hisp.dhis.rules.models.RuleAttributeValue;
import org.hisp.dhis.rules.models.RuleEnrollment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public class MockRuleEnrollment extends RuleEnrollment {
    @Nonnull
    @Override
    public String enrollment() {
        return null;
    }

    @Nonnull
    @Override
    public String programName() {
        return null;
    }

    @Nonnull
    @Override
    public Date incidentDate() {
        return null;
    }

    @Nonnull
    @Override
    public Date enrollmentDate() {
        return null;
    }

    @Nonnull
    @Override
    public Status status() {
        return null;
    }

    @Nonnull
    @Override
    public String organisationUnit() {
        return null;
    }

    @Nullable
    @Override
    public String organisationUnitCode() {
        return null;
    }

    @Nonnull
    @Override
    public List<RuleAttributeValue> attributeValues() {
        return null;
    }
}
