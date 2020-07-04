package org.hisp.dhis.rules.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionAssign
    extends RuleAction
{

    @Nonnull
    public static RuleActionAssign create( @Nullable String content,
        @Nonnull String data, @Nullable String field )
    {
        if ( content == null && field == null )
        {
            throw new IllegalArgumentException( "Either content or field " +
                "parameters must be not null." );
        }

        return new AutoValue_RuleActionAssign( data, content == null ? "" : content, field == null ? "" : field );
    }

    @Nonnull
    @JsonProperty( "content" )
    public abstract String content();

    @Nonnull
    @JsonProperty( "field" )
    public abstract String field();
}
