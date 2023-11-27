package org.hisp.dhis.rules.models;


import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

/**
 * @param data
 * @param attributeType
 * @param content       a message to show to user
 *                      when a target option is hidden.
 * @param option        uid of the target option to hide.
 * @param field         uid of the target field to hide.
 */
public record RuleActionHideOption(
        @Nonnull String data,
        @Nonnull AttributeType attributeType,
        @Nonnull String content,
        @Nonnull String option,
        @Nonnull String field
) implements RuleActionAttribute {

    @Nonnull
    public static RuleActionHideOption create(
            @CheckForNull String content, @Nonnull String option, @Nonnull String field, @CheckForNull AttributeType attributeType) {
        return new RuleActionHideOption("", attributeType == null ? UNKNOWN : attributeType, content == null ? "" : content, option, field);
    }

    @Nonnull
    public static RuleActionHideOption create(@CheckForNull String content, @Nonnull String option, @Nonnull String field) {
        return create(content, option, field, UNKNOWN);
    }
}
