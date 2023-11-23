package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionHideOptionGroup
    extends RuleActionAttribute
{

    @Nonnull
    public static RuleActionHideOptionGroup create(
        @CheckForNull String content, @Nonnull String optionGroup, @Nonnull String field,
        @CheckForNull AttributeType attributeType )
    {
        return new AutoValue_RuleActionHideOptionGroup( "", attributeType, content == null ? "" : content, optionGroup,
            field );
    }

    @Nonnull
    public static RuleActionHideOptionGroup create( @CheckForNull String content, @Nonnull String optionGroup,
        @Nonnull String field )
    {
        return create( content, optionGroup, field, UNKNOWN );
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

    /**
     * @return uid of the target field to hide options.
     */
    @Nonnull
    public abstract String field();
}
