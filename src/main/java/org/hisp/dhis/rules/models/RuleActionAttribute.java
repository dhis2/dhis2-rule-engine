package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public interface RuleActionAttribute extends RuleAction {
    @Nonnull
    AttributeType attributeType();
}
