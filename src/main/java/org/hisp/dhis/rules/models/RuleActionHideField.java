package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

/**
 * @param data
 * @param attributeType
 * @param content a message to show to user
 *      when a target field is hidden.
 * @param field uid of the target field to hide.
 *      It can be both dataElement and trackedEntityAttribute.
 */
public record RuleActionHideField(
        @Nonnull String data,
        @Nonnull AttributeType attributeType,
        @Nonnull String content,
        @Nonnull String field
) implements RuleActionAttribute {

    @Nonnull
    public static RuleActionHideField create(
            @CheckForNull String content, @Nonnull String field, @CheckForNull AttributeType attributeType )
    {
        return new RuleActionHideField( "", attributeType == null ? UNKNOWN : attributeType, content == null ? "" : content, field );
    }

    @Nonnull
    public static RuleActionHideField create( @CheckForNull String content, @Nonnull String field )
    {
        return create( content, field, UNKNOWN );
    }
}
