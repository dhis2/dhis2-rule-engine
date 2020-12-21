package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionHideOption
    extends RuleActionAttribute
{

    @Nonnull
    public static RuleActionHideOption create(
        @Nullable String content, @Nonnull String option, @Nonnull String field, @Nullable AttributeType attributeType )
    {
        return new AutoValue_RuleActionHideOption( "", attributeType, content == null ? "" : content, option, field );
    }

    @Nonnull
    public static RuleActionHideOption create( @Nullable String content, @Nonnull String option, @Nonnull String field )
    {
        return create( content, option, field, UNKNOWN );
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
