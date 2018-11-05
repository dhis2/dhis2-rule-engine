package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionAssign
        extends RuleAction {

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String data();

    @Nonnull
    public abstract String field();

    @Nonnull
    public abstract Boolean isAttribute();

    @Nonnull
    public static RuleActionAssign create(@Nullable String content,
                                          @Nonnull String data, @Nullable String field, @Nonnull Boolean isAttribute) {
        if (content == null && field == null) {
            throw new IllegalArgumentException("Either content or field " +
                    "parameters must be not null.");
        }

        return new AutoValue_RuleActionAssign(content == null ? "" : content,
                data, field == null ? "" : field, isAttribute);
    }
}
