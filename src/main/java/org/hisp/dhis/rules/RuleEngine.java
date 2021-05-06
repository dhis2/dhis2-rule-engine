package org.hisp.dhis.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.antlr.Parser;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.utils.RuleEngineUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.Callable;

import static org.hisp.dhis.antlr.AntlrParserUtils.castClass;
import static org.hisp.dhis.rules.parser.expression.ParserUtils.FUNCTION_FOR_DESCRIPTION;

// ToDo: logging
public final class RuleEngine
{
    private static final Log log = LogFactory.getLog( RuleEngine.class );

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

        return new RuleEngineExecution( rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData() );
    }

    @Nonnull
    public Callable<List<RuleEffect>> evaluate( @Nonnull RuleEnrollment ruleEnrollment,
        @Nonnull List<Rule> rulesToEvaluate )
    {
        Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEnrollment )
            .ruleVariables( ruleEngineContext.ruleVariables() )
            .triggerEnvironment( triggerEnvironment )
            .ruleEvents( ruleEvents )
            .constantValueMap( ruleEngineContext.constantsValues() )
            .build();

        return new RuleEngineExecution( rulesToEvaluate, valueMap, ruleEngineContext.supplementaryData() );
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
        return getExpressionDescription( expression, Boolean.class );
    }

    @Nonnull
    public RuleValidationResult evaluateDataFieldExpression( String expression )
    {
        // Rule action data field field should be evaluated against all i.e Boolean, String, Date and Numerical value
        return getExpressionDescription( expression, null );
    }

    private RuleValidationResult getExpressionDescription( String expression, Class<?> klass )
    {
        Map<String, String> itemDescriptions = new HashMap<>();

        CommonExpressionVisitor visitor = CommonExpressionVisitor.newBuilder()
                .withIteamStore( ruleEngineContext.getDataItemStore() )
                .withFunctionMethod( FUNCTION_FOR_DESCRIPTION )
                .withFunctionMap( RuleEngineUtils.FUNCTIONS )
                .withItemDescriptions( itemDescriptions )
                .validateAndBuildForDescription();

        RuleValidationResult result;

        try
        {
            if ( klass == null )
            {
                Parser.visit( expression, visitor );
            }
            else
            {
                castClass( klass, Parser.visit( expression, visitor ) );
            }

            String description = expression;

            for ( Map.Entry<String, String> entry : itemDescriptions.entrySet() )
            {
                description = description.replace( entry.getKey(), entry.getValue() );
            }

            result = RuleValidationResult.builder().isValid( true ).description( description ).build();
        }
        catch ( IllegalStateException e )
        {
            result = RuleValidationResult.builder().isValid( false )
                    .errorMessage( e.getMessage() )
                    .exception( e )
                    .build();
            log.debug( e.getMessage(), e );
        }

        return result;
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
