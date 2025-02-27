package org.hisp.dhis.rules;

import org.hisp.dhis.lib.expression.Expression;
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException;
import org.hisp.dhis.lib.expression.spi.ParseException;
import org.hisp.dhis.lib.expression.spi.ValueType;
import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEffects;
import org.hisp.dhis.rules.models.RuleEngineValidationException;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.RuleValidationResult;
import org.hisp.dhis.rules.models.TriggerEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

// ToDo: logging
public final class RuleEngine
{
    @Nonnull
    private final RuleEngineContext ruleEngineContext;

    @Nonnull
    private final List<RuleEvent> ruleEvents;

    @Nullable
    private final RuleEnrollment ruleEnrollment;

    @Nullable
    private TriggerEnvironment triggerEnvironment;

    RuleEngine( @Nonnull RuleEngineContext ruleEngineContext,
        @Nonnull List<RuleEvent> ruleEvents,
        @Nullable RuleEnrollment ruleEnrollment, @Nullable TriggerEnvironment triggerEnvironment )
    {
        this.ruleEngineContext = ruleEngineContext;
        this.ruleEvents = ruleEvents;
        this.ruleEnrollment = ruleEnrollment;
        this.triggerEnvironment = triggerEnvironment;
    }

    @Nonnull
    public List<RuleEvent> events()
    {
        return ruleEvents;
    }

    @Nullable
    public RuleEnrollment enrollment()
    {
        return ruleEnrollment;
    }

    @Nullable
    public TriggerEnvironment triggerEnvironment()
    {
        return triggerEnvironment;
    }

    @Nonnull
    public RuleEngineContext executionContext()
    {
        return ruleEngineContext;
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEvent ruleEvent )
    {
        return evaluate( ruleEvent, ruleEngineContext.rules() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEvent ruleEvent, @Nonnull List<Rule> rulesToEvaluate )
    {
        if ( ruleEvent == null )
        {
            throw new IllegalArgumentException( "ruleEvent == null" );
        }

        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEvent )
            .ruleVariables( ruleEngineContext.ruleVariables() )
            .ruleEnrollment( ruleEnrollment )
            .triggerEnvironment( triggerEnvironment )
            .ruleEvents( ruleEvents )
            .constantValueMap( ruleEngineContext.constantsValues() )
            .build();

        return new RuleEngineExecution( ruleEvent, rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEnrollment ruleEnrollment,
        @Nonnull List<Rule> rulesToEvaluate )
    {
        if ( ruleEnrollment == null )
        {
            throw new IllegalArgumentException( "ruleEnrollment == null" );
        }

        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEnrollment )
            .ruleVariables( ruleEngineContext.ruleVariables() )
            .triggerEnvironment( triggerEnvironment )
            .ruleEvents( ruleEvents )
            .constantValueMap( ruleEngineContext.constantsValues() )
            .build();

        return new RuleEngineExecution( ruleEnrollment, rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffects>> evaluate()
    {
        RuleVariableValueMap valueMap = RuleVariableValueMapBuilder.target()
            .ruleVariables( ruleEngineContext.ruleVariables() )
            .ruleEnrollment( ruleEnrollment )
            .triggerEnvironment( triggerEnvironment )
            .ruleEvents( ruleEvents )
            .constantValueMap( ruleEngineContext.constantsValues() )
            .multipleBuild();

        return new RuleEngineMultipleExecution( ruleEngineContext.rules(), valueMap,
            ruleEngineContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEnrollment ruleEnrollment )
    {
        return evaluate( ruleEnrollment, ruleEngineContext.rules() );
    }

    @Nonnull
    public RuleValidationResult evaluate( String expression )
    {
        // Rule condition expression should be evaluated against Boolean
        return getExpressionDescription(expression, Expression.Mode.RULE_ENGINE_CONDITION);
    }

    @Nonnull
    public RuleValidationResult evaluateDataFieldExpression( String expression )
    {
        // Rule action data field field should be evaluated against all i.e Boolean, String, Date and Numerical value
        return getExpressionDescription( expression, Expression.Mode.RULE_ENGINE_ACTION);
    }

    private RuleValidationResult getExpressionDescription(String expression, Expression.Mode mode)
    {
        try {
            Map<String, ValueType> validationMap = new HashMap<>();
            for (Map.Entry<String, DataItem> e : ruleEngineContext.getDataItemStore().entrySet()) {
                validationMap.put(e.getKey(), e.getValue().getValueType().toValueType());
            }
            new Expression(expression, mode).validate( validationMap );

            Map<String, String> displayNames = new HashMap<>();
            for (Map.Entry<String, DataItem> e : ruleEngineContext.getDataItemStore().entrySet()) {
                displayNames.put(e.getKey(), e.getValue().getDisplayName());
            }
            String description = new Expression(expression, mode).describe(displayNames);
            return RuleValidationResult.builder().isValid( true ).description(description).build();
        } catch (IllegalExpressionException | ParseException ex) {
            return RuleValidationResult.builder()
                    .isValid(false)
                    .exception(new RuleEngineValidationException(ex.getMessage()))
                    .errorMessage(ex.getMessage())
                    .build();
        }
    }

    public static class Builder
    {
        @Nonnull
        private final RuleEngineContext ruleEngineContext;

        @Nullable
        private List<RuleEvent> ruleEvents;

        @Nullable
        private RuleEnrollment ruleEnrollment;

        @Nullable
        private TriggerEnvironment triggerEnvironment;

        Builder( @Nonnull RuleEngineContext ruleEngineContext )
        {
            this.ruleEngineContext = ruleEngineContext;
        }

        @Nonnull
        public Builder events( @Nonnull List<RuleEvent> ruleEvents )
        {
            if ( ruleEvents == null )
            {
                throw new IllegalArgumentException( "ruleEvents == null" );
            }

            this.ruleEvents = Collections.unmodifiableList( new ArrayList<>( ruleEvents ) );
            return this;
        }

        @Nonnull
        public Builder enrollment( @Nonnull RuleEnrollment ruleEnrollment )
        {
            if ( ruleEnrollment == null )
            {
                throw new IllegalArgumentException( "ruleEnrollment == null" );
            }

            this.ruleEnrollment = ruleEnrollment;
            return this;
        }

        @Nonnull
        public Builder triggerEnvironment( @Nonnull TriggerEnvironment triggerEnvironment )
        {
            if ( triggerEnvironment == null )
            {
                throw new IllegalArgumentException( "triggerEnvironment == null" );
            }

            this.triggerEnvironment = triggerEnvironment;
            return this;
        }

        @Nonnull
        public RuleEngine build()
        {
            if ( ruleEvents == null )
            {
                ruleEvents = Collections.unmodifiableList( new ArrayList<RuleEvent>() );
            }

            return new RuleEngine( ruleEngineContext, ruleEvents, ruleEnrollment, triggerEnvironment );
        }
    }
}
