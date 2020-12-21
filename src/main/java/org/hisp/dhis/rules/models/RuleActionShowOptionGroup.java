package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionShowOptionGroup
    extends RuleActionAttribute
{

    @Nonnull
    public static RuleActionShowOptionGroup create( @Nullable String content, @Nonnull String optionGroup,
        @Nonnull String field, @Nullable AttributeType attributeType )
    {
        return new AutoValue_RuleActionShowOptionGroup( "", attributeType, content == null ? "" : content, optionGroup,
            field );
    }

    @Nonnull
    public static RuleActionShowOptionGroup create( @Nullable String content, @Nonnull String optionGroup,
        @Nonnull String field )
    {
        return create( content, optionGroup, field, UNKNOWN );
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
