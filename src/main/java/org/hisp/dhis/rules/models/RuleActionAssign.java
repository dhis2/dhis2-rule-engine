package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

;

public record RuleActionAssign(
        @Nonnull String data,
        @Nonnull AttributeType attributeType,
        @Nonnull String content,
        @Nonnull String field
) implements RuleActionAttribute {

    @Nonnull
    public static RuleActionAssign create(@CheckForNull String content,
                                          @Nonnull String data, @CheckForNull String field, @CheckForNull AttributeType attributeType) {
        if (content == null && field == null) {
            throw new IllegalArgumentException("Either content or field " +
                    "parameters must be not null.");
        }
        return new RuleActionAssign(data, attributeType == null ? UNKNOWN : attributeType, content == null ? "" : content,
                field == null ? "" : field);
    }

    @Nonnull
    public static RuleActionAssign create(@CheckForNull String content, @Nonnull String data, @CheckForNull String field) {
        return create(content, data, field, UNKNOWN);
    }
}
