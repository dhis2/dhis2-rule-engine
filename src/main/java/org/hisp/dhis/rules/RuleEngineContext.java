package org.hisp.dhis.rules;

import com.google.common.collect.Maps;
import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public final class RuleEngineContext
{
    @Nonnull
    private final List<Rule> rules;

    @Nonnull
    private final List<RuleVariable> ruleVariables;

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final Map<String, String> constantsValues;

    @Nullable
    private final Map<String, String> itemDescriptions;

    RuleEngineContext( @Nonnull List<Rule> rules, @Nonnull List<RuleVariable> ruleVariables,
        Map<String, List<String>> supplementaryData, Map<String, String> constantsValues, Map<String, String> itemDescriptions )
    {
        this.rules = rules;
        this.ruleVariables = ruleVariables;
        this.supplementaryData = supplementaryData;
        this.constantsValues = constantsValues;
        this.itemDescriptions = itemDescriptions;
    }

    RuleEngineContext( @Nonnull List<Rule> rules, @Nonnull List<RuleVariable> ruleVariables,
       Map<String, List<String>> supplementaryData, Map<String, String> constantsValues )
    {
        this.rules = rules;
        this.ruleVariables = ruleVariables;
        this.supplementaryData = supplementaryData;
        this.constantsValues = constantsValues;
        this.itemDescriptions = new HashMap<>();
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
    public List<Rule> rules()
    {
        return rules;
    }

    @Nonnull
    public List<RuleVariable> ruleVariables()
    {
        return ruleVariables;
    }

    @Nonnull
    public Map<String, List<String>> supplementaryData()
    {
        return supplementaryData;
    }

    @Nonnull
    public Map<String, String> constantsValues()
    {
        return constantsValues;
    }

    @Nonnull
    public RuleEngine.Builder toEngineBuilder()
    {
        return new RuleEngine.Builder( this );
    }

    public static class Builder
    {

        @Nullable
        private List<Rule> rules;

        @Nullable
        private List<RuleVariable> ruleVariables;

        @Nullable
        private Map<String, List<String>> supplementaryData;

        @Nullable
        private Map<String, String> constantsValues;

        @Nullable
        private Map<String, String> itemDescriptions;

        Builder( @Nonnull RuleExpressionEvaluator evaluator )
        {
        }

        Builder()
        {
        }

        @Nonnull
        public Builder rules( @Nonnull List<Rule> rules )
        {
            if ( rules == null )
            {
                throw new IllegalArgumentException( "rules == null" );
            }

            this.rules = unmodifiableList( new ArrayList<>( rules ) );
            return this;
        }

        @Nonnull
        public Builder ruleVariables( @Nonnull List<RuleVariable> ruleVariables )
        {
            if ( ruleVariables == null )
            {
                throw new IllegalArgumentException( "ruleVariables == null" );
            }

            this.ruleVariables = unmodifiableList( new ArrayList<>( ruleVariables ) );
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
        public Builder itemDescriptions( Map<String, String> itemDescriptions )
        {
            if ( itemDescriptions == null )
            {
                throw new IllegalArgumentException( "itemDescriptions == null" );
            }

            this.itemDescriptions = itemDescriptions;
            return this;
        }

        @Nonnull
        public RuleEngineContext build()
        {
            if ( rules == null )
            {
                rules = unmodifiableList( new ArrayList<Rule>() );
            }

            if ( ruleVariables == null )
            {
                ruleVariables = unmodifiableList( new ArrayList<RuleVariable>() );
            }

            return new RuleEngineContext( rules, ruleVariables, supplementaryData, constantsValues );
        }

        @Nonnull
        public RuleEngineContext buildForDescriptions()
        {
            if ( rules == null )
            {
                rules = unmodifiableList( new ArrayList<Rule>() );
            }

            if ( ruleVariables == null )
            {
                ruleVariables = unmodifiableList( new ArrayList<RuleVariable>() );
            }

            if ( itemDescriptions == null )
            {
                itemDescriptions = unmodifiableMap( new HashMap<String, String>() );
            }

            return new RuleEngineContext( rules, ruleVariables, supplementaryData, constantsValues, itemDescriptions );
        }
    }
}
