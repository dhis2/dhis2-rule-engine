package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.CheckForNull;

import static org.hisp.dhis.rules.models.AttributeType.UNKNOWN;

@AutoValue
public abstract class RuleActionSetMandatoryField
    extends RuleActionAttribute
{

    @Nonnull
    public static RuleActionSetMandatoryField create( @Nonnull String field, @CheckForNull AttributeType attributeType )
    {
        return new AutoValue_RuleActionSetMandatoryField( "", attributeType, field );
    }

    @Nonnull
    public static RuleActionSetMandatoryField create( @Nonnull String field )
    {
        return create( field, UNKNOWN );
    }

    @Nonnull
    public abstract String field();
}
