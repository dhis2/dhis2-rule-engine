package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionAssign
    extends RuleActionAttribute
{

    @Nonnull
    public static RuleActionAssign create( @Nullable String content,
        @Nonnull String data, @Nullable String field, @Nullable AttributeType attributeType )
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
    public static RuleActionAssign create( @Nullable String content, @Nonnull String data, @Nullable String field )
    {
        return create( content, data, field, UNKNOWN );
    }

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String field();
}
