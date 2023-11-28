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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public record RuleEngine(
        @Nonnull RuleEngineContext executionContext,
        @Nonnull List<RuleEvent> events,
        @CheckForNull RuleEnrollment enrollment,
        @CheckForNull TriggerEnvironment triggerEnvironment
) {
    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEvent ruleEvent )
    {
        return evaluate( ruleEvent, executionContext.rules() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @CheckForNull RuleEvent ruleEvent, @Nonnull List<Rule> rulesToEvaluate )
    {
        if ( ruleEvent == null )
        {
            throw new IllegalArgumentException( "ruleEvent == null" );
        }

        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEvent )
                .ruleVariables( executionContext.ruleVariables() )
                .ruleEnrollment(enrollment)
                .triggerEnvironment( triggerEnvironment )
                .ruleEvents(events)
                .constantValueMap( executionContext.constantsValues() )
                .build();

        return new RuleEngineExecution( ruleEvent, rulesToEvaluate, valueMap, executionContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @CheckForNull RuleEnrollment ruleEnrollment,
                                                @Nonnull List<Rule> rulesToEvaluate )
    {
        if ( ruleEnrollment == null )
        {
            throw new IllegalArgumentException( "enrollment == null" );
        }

        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEnrollment )
                .ruleVariables( executionContext.ruleVariables() )
                .triggerEnvironment( triggerEnvironment )
                .ruleEvents(events)
                .constantValueMap( executionContext.constantsValues() )
                .build();

        return new RuleEngineExecution( ruleEnrollment, rulesToEvaluate, valueMap, executionContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffects>> evaluate()
    {
        RuleVariableValueMap valueMap = RuleVariableValueMapBuilder.target()
                .ruleVariables( executionContext.ruleVariables() )
                .ruleEnrollment(enrollment)
                .triggerEnvironment( triggerEnvironment )
                .ruleEvents(events)
                .constantValueMap( executionContext.constantsValues() )
                .multipleBuild();

        return new RuleEngineMultipleExecution( executionContext.rules(), valueMap,
                executionContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEnrollment ruleEnrollment )
    {
        return evaluate( ruleEnrollment, executionContext.rules() );
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
            for (Map.Entry<String, DataItem> e : executionContext.dataItemStore().entrySet()) {
                validationMap.put(e.getKey(), e.getValue().valueType().toValueType());
            }
            new Expression(expression, mode).validate( validationMap );

            Map<String, String> displayNames = new HashMap<>();
            for (Map.Entry<String, DataItem> e : executionContext.dataItemStore().entrySet()) {
                displayNames.put(e.getKey(), e.getValue().displayName());
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

        @CheckForNull
        private List<RuleEvent> ruleEvents;

        @CheckForNull
        private RuleEnrollment ruleEnrollment;

        @CheckForNull
        private TriggerEnvironment triggerEnvironment;

        Builder( @Nonnull RuleEngineContext ruleEngineContext )
        {
            this.ruleEngineContext = ruleEngineContext;
        }

        @Nonnull
        public Builder events( @CheckForNull List<RuleEvent> ruleEvents )
        {
            if ( ruleEvents == null )
            {
                throw new IllegalArgumentException( "events == null" );
            }

            this.ruleEvents = Collections.unmodifiableList( new ArrayList<>( ruleEvents ) );
            return this;
        }

        @Nonnull
        public Builder enrollment( @CheckForNull RuleEnrollment ruleEnrollment )
        {
            if ( ruleEnrollment == null )
            {
                throw new IllegalArgumentException( "enrollment == null" );
            }

            this.ruleEnrollment = ruleEnrollment;
            return this;
        }

        @Nonnull
        public Builder triggerEnvironment( @CheckForNull TriggerEnvironment triggerEnvironment )
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
