package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleActionSetMandatoryField
    extends RuleAction
{

        @Nonnull
        public abstract String field();

        @Nonnull
        public static RuleActionSetMandatoryField create( @Nonnull String field )
        {
                return new AutoValue_RuleActionSetMandatoryField( field );
        }
}
