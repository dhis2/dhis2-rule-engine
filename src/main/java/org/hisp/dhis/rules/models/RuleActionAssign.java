package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.CheckForNull;;
import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionAssign
    extends RuleActionAttribute
{

    @Nonnull
    public static RuleActionAssign create( @CheckForNull String content,
        @Nonnull String data, @CheckForNull String field, @CheckForNull AttributeType attributeType )
    {
        if ( content == null && field == null )
        {
            throw new IllegalArgumentException( "Either content or field " +
                "parameters must be not null." );
        }

        return new AutoValue_RuleActionAssign( data, attributeType, content == null ? "" : content,
            field == null ? "" : field );
    }

    @Nonnull
    public static RuleActionAssign create( @CheckForNull String content, @Nonnull String data, @CheckForNull String field )
    {
        return create( content, data, field, UNKNOWN );
    }

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String field();
}
