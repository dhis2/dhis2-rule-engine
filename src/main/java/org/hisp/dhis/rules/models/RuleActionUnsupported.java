package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * @param data
 * @param content         a message to show to user
 *                        when an actionType is not supported
 * @param actionValueType name of the unsupported action.
 */
public record RuleActionUnsupported(
        @Nonnull String data,
        @Nonnull String content,
        @Nonnull String actionValueType
) implements RuleAction {

    @Nonnull
    public static RuleActionUnsupported create(@Nonnull String content, @Nonnull String actionValueType) {
        return new RuleActionUnsupported("", content, actionValueType);
    }
}
