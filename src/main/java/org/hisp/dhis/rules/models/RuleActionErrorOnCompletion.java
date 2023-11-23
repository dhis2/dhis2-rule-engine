package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.CheckForNull;;
import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionErrorOnCompletion
    extends RuleActionMessage
{

    @Nonnull
    public static RuleActionErrorOnCompletion create( @CheckForNull String content,
        @CheckForNull String data, @CheckForNull String field, @CheckForNull AttributeType attributeType )
    {
        if ( content == null && data == null && field == null )
        {
            throw new IllegalArgumentException( "Content, data and field" +
                " must not be null at the same time" );
        }

        return new AutoValue_RuleActionErrorOnCompletion( data == null ? "" : data, attributeType,
            content == null ? "" : content, field == null ? "" : field );
    }

    @Nonnull
    public static RuleActionErrorOnCompletion create( @CheckForNull String content, @CheckForNull String data,
        @CheckForNull String field )
    {
        return create( content, data, field, UNKNOWN );
    }
}
