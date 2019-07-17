package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionShowOptionGroup
        extends RuleAction
{

        /**
         * @return a message to show to user
         * when a option group is shown.
         */
        @Nonnull
        public abstract String content();

        /**
         * @return uid of the target option group to show.
         *
         */
        @Nonnull
        public abstract String optionGroup();

        @Nonnull
        public static RuleActionShowOptionGroup create(
                @Nullable String content, @Nonnull String optionGroup )
        {
            return new AutoValue_RuleActionShowOptionGroup( content == null ? "" : content, optionGroup );
        }
}
