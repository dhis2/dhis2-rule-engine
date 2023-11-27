package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

public record RuleActionMessage(
        @Nonnull String data,
        @Nonnull AttributeType attributeType,
        @Nonnull String content,
        @Nonnull String field,
        @Nonnull Type type
) implements RuleActionAttribute {

    public enum Type { WARNING_ON_COMPILATION, ERROR_ON_COMPILATION, SHOW_WARNING, SHOW_ERROR }

    /*
    On Completion
     */
    @Nonnull
    public static RuleActionMessage create( @CheckForNull String content,
                                                      @CheckForNull String data, @CheckForNull String field, @CheckForNull AttributeType attributeType, @Nonnull Type type )
    {
        if ( content == null && data == null && field == null )
        {
            throw new IllegalArgumentException( "Content, data and field" +
                    " must not be null at the same time" );
        }

        return new RuleActionMessage( data == null ? "" : data, attributeType == null ? UNKNOWN : attributeType,
                content == null ? "" : content, field == null ? "" : field, type );
    }

    @Nonnull
    public static RuleActionMessage create(@CheckForNull String content, @CheckForNull String data, @CheckForNull String field, @Nonnull Type type)
    {
        return create( content, data, field, UNKNOWN, type );
    }
}
