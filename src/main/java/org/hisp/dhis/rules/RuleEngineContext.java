package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleVariable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RuleEngineContext(
    @Nonnull
    List<Rule> rules,
    @Nonnull
    List<RuleVariable> ruleVariables,
    @Nonnull
    Map<String, List<String>> supplementaryData,
    @Nonnull
    Map<String, String> constantsValues,
    @Nonnull
    RuleEngineIntent ruleEngineIntent,
    @CheckForNull
    Map<String, DataItem> dataItemStore) {

    RuleEngineContext(@Nonnull List<Rule> rules, @Nonnull List<RuleVariable> ruleVariables,
                      @Nonnull Map<String, List<String>> supplementaryData, @Nonnull Map<String, String> constantsValues) {
        this(rules, ruleVariables, supplementaryData, constantsValues, RuleEngineIntent.EVALUATION, new HashMap<>());
    }

    @Nonnull
    public static Builder builder()
    {
        return new Builder();
    }

    @Nonnull
    @Deprecated
    public static Builder builder( @Nonnull RuleExpressionEvaluator evaluator )
    {
        return new Builder( evaluator );
    }

    @Nonnull
    public RuleEngine.Builder toEngineBuilder()
    {
        return new RuleEngine.Builder( this );
    }

    public static class Builder
    {
        private RuleEngineIntent intent;

        @CheckForNull
        private List<Rule> rules;

        @CheckForNull
        private List<RuleVariable> ruleVariables;

        @CheckForNull
        private Map<String, List<String>> supplementaryData;

        @CheckForNull
        private Map<String, String> constantsValues;

        @CheckForNull
        private Map<String, DataItem> itemStore;

        Builder( @Nonnull RuleExpressionEvaluator evaluator )
        {
        }

        Builder()
        {
        }

        @Nonnull
        public Builder rules( List<Rule> rules )
        {
            if ( rules == null )
            {
                throw new IllegalArgumentException( "rules == null" );
            }

            this.rules = List.copyOf(rules);
            return this;
        }

        @Nonnull
        public Builder ruleVariables( List<RuleVariable> ruleVariables )
        {
            if ( ruleVariables == null )
            {
                throw new IllegalArgumentException( "ruleVariables == null" );
            }

            this.ruleVariables = List.copyOf(ruleVariables);
            return this;
        }

        @Nonnull
        public Builder ruleEngineItent( @CheckForNull RuleEngineIntent ruleEngineIntent )
        {
            this.intent = ruleEngineIntent;
            return this;
        }

        @Nonnull
        public Builder itemStore( @CheckForNull Map<String, DataItem> itemStore )
        {
            this.itemStore = itemStore;
            return this;
        }


        @Nonnull
        public Builder supplementaryData( Map<String, List<String>> supplementaryData )
        {
            if ( supplementaryData == null )
            {
                throw new IllegalArgumentException( "supplementaryData == null" );
            }
            this.supplementaryData = supplementaryData;
            return this;
        }

        @Deprecated
        public Builder calculatedValueMap( Map<String, Map<String, String>> calculatedValueMap )
        {
            return this;
        }

        @Nonnull
        public Builder constantsValue( Map<String, String> constantsValues )
        {
            if ( constantsValues == null )
            {
                throw new IllegalArgumentException( "constantsValue == null" );
            }
            this.constantsValues = constantsValues;
            return this;
        }

        @Nonnull
        public RuleEngineContext build()
        {
            if ( rules == null )
                rules = List.of();
            if ( ruleVariables == null )
                ruleVariables = List.of();
            if (supplementaryData == null) supplementaryData = Map.of();
            if (constantsValues == null) constantsValues = Map.of();
            if ( intent == null )
            {
                // For evaluation
                return new RuleEngineContext( rules, ruleVariables, supplementaryData, constantsValues );
            }
            else
            {
                // for description
                return new RuleEngineContext( rules, ruleVariables, supplementaryData, constantsValues,
                    intent, itemStore );
            }
        }
    }
}
