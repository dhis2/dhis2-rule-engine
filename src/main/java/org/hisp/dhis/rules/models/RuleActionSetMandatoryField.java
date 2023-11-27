package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

public record RuleActionSetMandatoryField(
        @Nonnull String data,
        @Nonnull AttributeType attributeType,
        @Nonnull String field
) implements RuleActionAttribute {

    @Nonnull
    public static RuleActionSetMandatoryField create(@Nonnull String field, @CheckForNull AttributeType attributeType) {
        return new RuleActionSetMandatoryField("", attributeType == null ? UNKNOWN : attributeType, field);
    }

    @Nonnull
    public static RuleActionSetMandatoryField create(@Nonnull String field) {
        return create(field, UNKNOWN);
    }
}
