package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionWarningOnCompletion
    extends RuleActionMessage
{

    @Nonnull
    public static RuleActionWarningOnCompletion create( @Nullable String content,
        @Nullable String data, @Nullable String field, @Nullable AttributeType attributeType )
    {
        if ( content == null && data == null && field == null )
        {
            throw new IllegalArgumentException( "Content, data and field" +
                " must not be null at the same time" );
        }

        return new AutoValue_RuleActionWarningOnCompletion( data == null ? "" : data,
            attributeType == null ? UNKNOWN : attributeType, content == null ? "" : content,
            field == null ? "" : field );
    }

    @Nonnull
    public static RuleActionWarningOnCompletion create( @Nullable String content, @Nullable String data,
        @Nullable String field )
    {
        return create( content, data, field, UNKNOWN );
    }
}
