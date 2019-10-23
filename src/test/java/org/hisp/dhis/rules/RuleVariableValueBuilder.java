package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleValueType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RuleVariableValueBuilder
{

        private String value;

        private RuleValueType type = RuleValueType.TEXT;

        private List<String> candidates = new ArrayList<>();

        private String eventDate;

        public static RuleVariableValueBuilder create()
        {
                return new RuleVariableValueBuilder();
        }

        public RuleVariableValueBuilder withValue( @Nonnull String value )
        {
                this.value = value;

                return this;
        }

        public RuleVariableValueBuilder withCandidates( @Nonnull List<String> candidates )
        {
                this.candidates = candidates;

                return this;
        }
        public RuleVariableValueBuilder withEventDate( @Nullable String eventDate )
        {
                this.eventDate = eventDate;

                return this;
        }
        public RuleVariableValueBuilder withType(@Nonnull RuleValueType valueType)
        {
                this.type = valueType;

                return this;
        }

        public RuleVariableValue build()
        {
                RuleVariableValue ruleVariableValue = RuleVariableValue.create( value, type, candidates, eventDate );

                return ruleVariableValue;
        }
}
