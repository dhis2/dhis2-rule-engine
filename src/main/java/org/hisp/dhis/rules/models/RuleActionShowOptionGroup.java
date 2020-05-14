package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionShowOptionGroup
    extends RuleAction
{

    @Nonnull
    public static RuleActionShowOptionGroup create(
        @Nullable String content, @Nonnull String optionGroup, @Nonnull String field )
    {
        return new AutoValue_RuleActionShowOptionGroup( "", content == null ? "" : content, optionGroup, field );
    }

    /**
     * @return a message to show to user
     * when a option group is shown.
     */
    @Nonnull
    public abstract String content();

    /**
     * @return uid of the target option group to show.
     */
    @Nonnull
    public abstract String optionGroup();

    /**
     * @return uid of the field of option group to show.
     */
    @Nonnull
    public abstract String field();
}
