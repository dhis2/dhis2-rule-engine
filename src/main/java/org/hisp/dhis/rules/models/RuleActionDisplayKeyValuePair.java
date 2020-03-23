package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionDisplayKeyValuePair
    extends RuleActionText
{

    @Nonnull
    public static RuleActionDisplayKeyValuePair createForFeedback(
        @Nullable String content, @Nullable String data )
    {
        if ( content == null && data == null )
        {
            throw new IllegalArgumentException( "Both content and data must not be null" );
        }

        return new AutoValue_RuleActionDisplayKeyValuePair( data == null ? "" : data, content == null ? "" : content,
            LOCATION_FEEDBACK_WIDGET );
    }

    @Nonnull
    public static RuleActionDisplayKeyValuePair createForIndicators(
        @Nullable String content, @Nullable String data )
    {
        if ( content == null && data == null )
        {
            throw new IllegalArgumentException( "Both content and data must not be null" );
        }

        return new AutoValue_RuleActionDisplayKeyValuePair( data == null ? "" : data, content == null ? "" : content,
            LOCATION_INDICATOR_WIDGET );
    }
}
