package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

/**
 * @param data
 * @param attributeType
 * @param content       a message to show to user
 *                      when a target option is hidden.
 * @param optionGroup   uid of the target option group to hide.
 * @param field         uid of the target field to hide options.
 */
public record RuleActionHideOptionGroup(
        @Nonnull String data,
        @Nonnull AttributeType attributeType,
        @Nonnull String content,
        @Nonnull String optionGroup,
        @Nonnull String field
) implements RuleActionAttribute {

    @Nonnull
    public static RuleActionHideOptionGroup create(
            @CheckForNull String content, @Nonnull String optionGroup, @Nonnull String field,
            @CheckForNull AttributeType attributeType) {
        return new RuleActionHideOptionGroup("", attributeType == null ? UNKNOWN : attributeType, content == null ? "" : content, optionGroup,
                field);
    }

    @Nonnull
    public static RuleActionHideOptionGroup create(@CheckForNull String content, @Nonnull String optionGroup,
                                                   @Nonnull String field) {
        return create(content, optionGroup, field, UNKNOWN);
    }
}
