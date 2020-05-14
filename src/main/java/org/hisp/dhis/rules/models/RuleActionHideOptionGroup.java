package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionHideOptionGroup
    extends RuleAction
{

    @Nonnull
    public static RuleActionHideOptionGroup create(
        @Nullable String content, @Nonnull String optionGroup )
    {
        return new AutoValue_RuleActionHideOptionGroup( "", content == null ? "" : content, optionGroup );
    }

    /**
     * @return a message to show to user
     * when a target option is hidden.
     */
    @Nonnull
    public abstract String content();

    /**
     * @return uid of the target option group to hide.
     */
    @Nonnull
    public abstract String optionGroup();
}
