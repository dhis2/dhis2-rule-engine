package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

@AutoValue
public abstract class RuleActionCreateEvent
    extends RuleAction
{

    @Nonnull
    public static RuleActionCreateEvent create( @CheckForNull String content,
        @CheckForNull String data, @Nonnull String programStage )
    {
        return new AutoValue_RuleActionCreateEvent( data == null ? "" : data, content == null ? "" : content,
            programStage );
    }

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String programStage();
}
