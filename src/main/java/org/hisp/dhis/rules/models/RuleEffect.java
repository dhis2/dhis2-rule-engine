package org.hisp.dhis.rules.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RuleEffect
{

    @Nonnull
    @JsonCreator
    public static RuleEffect create( @Nonnull @JsonProperty( "ruleAction" ) RuleAction ruleAction,
                                     @Nonnull @JsonProperty( "data" ) String data )
    {
        return new AutoValue_RuleEffect( ruleAction, data );
    }

    @Nonnull
    public static RuleEffect create( @Nonnull RuleAction ruleAction )
    {
        return new AutoValue_RuleEffect( ruleAction, "" );
    }

    @Nonnull
    @JsonProperty( "ruleAction" )
    public abstract RuleAction ruleAction();

    @Nonnull
    @JsonProperty( "data" )
    public abstract String data();
}
