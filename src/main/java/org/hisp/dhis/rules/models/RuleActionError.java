package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionError
    extends RuleAction
{

    @Nonnull
    public static RuleActionError create( @Nonnull String data )
    {
        return new AutoValue_RuleActionError( data );
    }
}
