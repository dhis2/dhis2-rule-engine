package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionCreateEvent
    extends RuleActionData
{

    @Nonnull
    public static RuleActionCreateEvent create( @Nullable String content,
        @Nullable String data, @Nonnull String programStage )
    {
        return new AutoValue_RuleActionCreateEvent( data == null ? "" : data, content == null ? "" : content,
            programStage );
    }

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String programStage();
}
