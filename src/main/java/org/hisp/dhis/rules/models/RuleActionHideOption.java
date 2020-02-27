package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionHideOption
    extends RuleAction
{

    @Nonnull
    public static RuleActionHideOption create(
        @Nullable String content, @Nonnull String option, @Nonnull String field )
    {
        return new AutoValue_RuleActionHideOption( content == null ? "" : content, option, field );
    }

    /**
     * @return a message to show to user
     * when a target option is hidden.
     */
    @Nonnull
    public abstract String content();

    /**
     * @return uid of the target option to hide.
     */
    @Nonnull
    public abstract String option();

    /**
     * @return uid of the target field to hide.
     */
    @Nonnull
    public abstract String field();
}
