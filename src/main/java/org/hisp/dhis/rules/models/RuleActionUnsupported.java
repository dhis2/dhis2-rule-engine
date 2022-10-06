package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RuleActionUnsupported
        extends RuleAction
{

    @Nonnull
    public static RuleActionUnsupported create( @Nonnull String content, @Nonnull String actionValueType )
    {
        return new AutoValue_RuleActionUnsupported( "", content, actionValueType );
    }

    /**
     * @return a message to show to user
     * when an actionType is not supported
     */
    @Nonnull
    public abstract String content();

    /**
     * @return name of the unsupported action.
     */
    @Nonnull
    public abstract String actionValueType();
}
