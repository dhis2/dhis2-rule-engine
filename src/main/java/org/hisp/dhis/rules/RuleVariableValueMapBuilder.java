package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.utils.RuleEngineUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hisp.dhis.rules.RuleVariableValue.create;

@SuppressWarnings( "PMD.GodClass" )
public final class RuleVariableValueMapBuilder
{
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Nonnull
    public final SimpleDateFormat dateFormat;

    @Nonnull
    private final Map<String, String> allConstantValues;

    @Nonnull
    private final List<RuleVariable> ruleVariables;

    @Nonnull
    private final List<RuleEvent> ruleEvents;

    @Nullable
    public RuleEnrollment ruleEnrollment;

    @Nullable
    public RuleEvent ruleEvent;

    @Nullable
    private TriggerEnvironment triggerEnvironment;

    private RuleVariableValueMapBuilder()
    {
        this.dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );

        // collections used for construction of resulting variable value map
        this.ruleVariables = new ArrayList<>();
        this.ruleEvents = new ArrayList<>();
        this.allConstantValues = new HashMap<>();
    }

    private RuleVariableValueMapBuilder( @Nonnull RuleEnrollment ruleEnrollment )
    {
        this();

        // enrollment is the target
        this.ruleEnrollment = ruleEnrollment;
    }

    private RuleVariableValueMapBuilder( @Nonnull RuleEvent ruleEvent )
    {
        this();

        // event is the target
        this.ruleEvent = ruleEvent;
    }

    @Nonnull
    static RuleVariableValueMapBuilder target( @Nonnull RuleEnrollment ruleEnrollment )
    {
        return new RuleVariableValueMapBuilder( ruleEnrollment );
    }

    @Nonnull
    static RuleVariableValueMapBuilder target( @Nonnull RuleEvent ruleEvent )
    {
        return new RuleVariableValueMapBuilder( ruleEvent );
    }

    @Nonnull
    static RuleVariableValueMapBuilder target()
    {
        return new RuleVariableValueMapBuilder();
    }

    @Nonnull
    RuleVariableValueMapBuilder ruleVariables( @Nonnull List<RuleVariable> ruleVariables )
    {
        this.ruleVariables.addAll( ruleVariables );
        return this;
    }

    @Nonnull
    RuleVariableValueMapBuilder ruleEnrollment( @Nullable RuleEnrollment ruleEnrollment )
    {
        if ( this.ruleEnrollment != null )
        {
            throw new IllegalStateException( "It seems that enrollment has been set as target " +
                "already. It can't be used as a part of execution context." );
        }

        this.ruleEnrollment = ruleEnrollment;
        return this;
    }

    @Nonnull
    RuleVariableValueMapBuilder triggerEnvironment( @Nullable TriggerEnvironment triggerEnvironment )
    {
        if ( this.triggerEnvironment != null )
        {
            throw new IllegalStateException( "triggerEnvironment == null" );
        }

        this.triggerEnvironment = triggerEnvironment;
        return this;
    }

    @Nonnull
    RuleVariableValueMapBuilder ruleEvents( @Nonnull List<RuleEvent> ruleEvents )
    {
        if ( isEventInList( ruleEvents, ruleEvent ) )
        {
            throw new IllegalStateException( String.format( Locale.US, "ruleEvent %s is already set " +
                "as a target, but also present in the context: ruleEvents list", ruleEvent.event() ) );
        }

        this.ruleEvents.addAll( ruleEvents );
        return this;
    }

    @Nonnull
    RuleVariableValueMapBuilder constantValueMap( @Nonnull Map<String, String> constantValues )
    {
        this.allConstantValues.putAll( constantValues );
        return this;
    }

    @Nonnull
    Map<String, RuleVariableValue> build()
    {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();

        // set environment variables
        valueMap.putAll( buildEnvironmentVariables() );

        // set metadata variables
        valueMap.putAll( buildRuleVariableValues() );

        // set constants value map
        valueMap.putAll( buildConstantsValues() );

        // do not let outer world to alter variable value map
        return Collections.unmodifiableMap( valueMap );
    }

    @Nonnull
    RuleVariableValueMap multipleBuild()
    {
        Map<RuleEnrollment, Map<String, RuleVariableValue>> enrollmentMap = new HashMap<>();
        if ( ruleEnrollment != null )
        {
            enrollmentMap.put( ruleEnrollment, build() );
        }
        Map<RuleEvent, Map<String, RuleVariableValue>> eventMap = new HashMap<>();

        for ( RuleEvent event : ruleEvents )
        {
            this.ruleEvent = event;
            eventMap.put( ruleEvent, build() );
        }

        return new RuleVariableValueMap( enrollmentMap, eventMap );
    }

    private boolean isEventInList( @Nonnull List<RuleEvent> ruleEvents,
        @Nullable RuleEvent ruleEvent )
    {
        if ( ruleEvent != null )
        {
            for ( int i = 0; i < ruleEvents.size(); i++ )
            {
                RuleEvent event = ruleEvents.get( i );

                if ( event.event().equals( ruleEvent.event() ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    private Map<String, RuleDataValue> buildCurrentEventValues()
    {
        Map<String, RuleDataValue> currentEventValues = new HashMap<>();

        if ( ruleEvent != null )
        {
            for ( int index = 0; index < ruleEvent.dataValues().size(); index++ )
            {
                RuleDataValue ruleDataValue = ruleEvent.dataValues().get( index );
                currentEventValues.put( ruleDataValue.dataElement(), ruleDataValue );
            }
        }

        return currentEventValues;
    }

    private Map<String, RuleAttributeValue> buildCurrentEnrollmentValues()
    {
        Map<String, RuleAttributeValue> currentEnrollmentValues = new HashMap<>();
        if ( ruleEnrollment != null )
        {
            List<RuleAttributeValue> ruleAttributeValues = ruleEnrollment.attributeValues();
            for ( int index = 0; index < ruleAttributeValues.size(); index++ )
            {
                RuleAttributeValue attributeValue = ruleAttributeValues.get( index );
                currentEnrollmentValues.put( attributeValue.trackedEntityAttribute(), attributeValue );
            }
        }

        return currentEnrollmentValues;
    }

    private Map<String, List<RuleDataValueHistory>> buildAllEventValues()
    {
        Map<String, List<RuleDataValueHistory>> allEventsValues = new HashMap<>();
        List<RuleEvent> events = new ArrayList<>( ruleEvents );

        if ( ruleEvent != null )
        {
            // target event should be among the list of all
            // events in order to achieve correct behavior
            events.add( ruleEvent );
        }

        // sort list of events by eventDate:
        Collections.sort( events, RuleEvent.EVENT_DATE_COMPARATOR );

        // aggregating values by data element uid
        for ( int i = 0; i < events.size(); i++ )
        {
            RuleEvent ruleEvent = events.get( i );

            for ( int j = 0; j < ruleEvent.dataValues().size(); j++ )
            {
                RuleDataValue ruleDataValue = ruleEvent.dataValues().get(j);
                RuleDataValueHistory ruleDataValueHistory = new RuleDataValueHistory(
                        ruleDataValue.value(),
                        ruleEvent.eventDate(),
                        ruleEvent.createdDate(),
                        ruleEvent.programStage() );

                // push new list if it is not there for the given data element
                if ( !allEventsValues.containsKey( ruleDataValue.dataElement() ) )
                {
                    allEventsValues.put( ruleDataValue.dataElement(),
                        new ArrayList<RuleDataValueHistory>( events.size() ) ); //NOPMD
                }

                // append data value to the list
                allEventsValues.get( ruleDataValue.dataElement() ).add( ruleDataValueHistory );
            }
        }

        return allEventsValues;
    }

    private Map<String, RuleVariableValue> buildConstantsValues()
    {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();

        for ( Map.Entry<String, String> entrySet : allConstantValues.entrySet() )
        {
            valueMap.put( entrySet.getKey(), create( entrySet.getValue(), RuleValueType.NUMERIC ) );
        }
        return valueMap;
    }

    private Map<String, RuleVariableValue> buildEnvironmentVariables()
    {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();
        String currentDate = dateFormat.format( new Date() );

        valueMap.put( RuleEngineUtils.ENV_VAR_CURRENT_DATE,
            create( currentDate, RuleValueType.DATE, Arrays.asList( currentDate ), currentDate ) );

        if ( triggerEnvironment != null )
        {
            String environment = triggerEnvironment.getClientName();
            valueMap.put( RuleEngineUtils.ENV_VAR_ENVIRONMENT,
                create( environment, RuleValueType.TEXT, Arrays.asList( environment ), currentDate ) );
        }

        if ( !ruleEvents.isEmpty() )
        {
            valueMap.put( RuleEngineUtils.ENV_VAR_EVENT_COUNT, create( String.valueOf( ruleEvents.size() ),
                RuleValueType.NUMERIC, Arrays.asList( String.valueOf( ruleEvents.size() ) ), currentDate ) );
        }

        if ( ruleEnrollment != null )
        {
            valueMap.put( RuleEngineUtils.ENV_VAR_ENROLLMENT_ID, create( ruleEnrollment.enrollment(),
                RuleValueType.TEXT, Arrays.asList( ruleEnrollment.enrollment() ), currentDate ) );
            valueMap.put( RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT, create( "1",
                RuleValueType.NUMERIC, Arrays.asList( "1" ), currentDate ) );
            valueMap.put( RuleEngineUtils.ENV_VAR_TEI_COUNT, create( "1",
                RuleValueType.NUMERIC, Arrays.asList( "1" ), currentDate ) );

            String enrollmentDate = dateFormat.format( ruleEnrollment.enrollmentDate() );
            valueMap.put( RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE, create( enrollmentDate,
                RuleValueType.DATE, Arrays.asList( enrollmentDate ), currentDate ) );

            String incidentDate = dateFormat.format( ruleEnrollment.incidentDate() );
            valueMap.put( RuleEngineUtils.ENV_VAR_INCIDENT_DATE, create( incidentDate,
                RuleValueType.DATE, Arrays.asList( incidentDate ), currentDate ) );

            String status = ruleEnrollment.status().toString();
            valueMap.put( RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS, create( status,
                RuleValueType.TEXT, Arrays.asList( status ), currentDate ) );

            String organisationUnit = ruleEnrollment.organisationUnit();
            valueMap.put( RuleEngineUtils.ENV_VAR_OU, create( organisationUnit, RuleValueType.TEXT ) );

            String programName = ruleEnrollment.programName();
            valueMap.put( RuleEngineUtils.ENV_VAR_PROGRAM_NAME, create( programName, RuleValueType.TEXT ) );

            String organisationUnitCode = ruleEnrollment.organisationUnitCode();
            valueMap.put( RuleEngineUtils.ENV_VAR_OU_CODE, create( organisationUnitCode, RuleValueType.TEXT ) );
        }

        if ( ruleEvent != null )
        {
            String eventDate = dateFormat.format( ruleEvent.eventDate() );
            valueMap.put( RuleEngineUtils.ENV_VAR_EVENT_DATE, create( eventDate, RuleValueType.DATE,
                Arrays.asList( eventDate ), currentDate ) );

            if ( ruleEvent.dueDate() != null ) {
                String dueDate = dateFormat.format(ruleEvent.dueDate());
                valueMap.put(RuleEngineUtils.ENV_VAR_DUE_DATE, create(dueDate, RuleValueType.DATE,
                        Arrays.asList(dueDate), currentDate));
            }
            if ( ruleEvent.completedDate() != null )
            {
                String completedDate = dateFormat.format( ruleEvent.completedDate() );
                valueMap.put( RuleEngineUtils.ENV_VAR_COMPLETED_DATE, create( completedDate, RuleValueType.DATE,
                    Arrays.asList( completedDate ), currentDate ) );
            }

            // override value of event count
            String eventCount = String.valueOf( ruleEvents.size() + 1 );
            if ( ruleEvents.contains( ruleEvent ) )
            {
                eventCount = String.valueOf( ruleEvents.size() );
            }

            valueMap.put( RuleEngineUtils.ENV_VAR_EVENT_COUNT, create( eventCount,
                RuleValueType.NUMERIC, Arrays.asList( eventCount ), currentDate ) );
            valueMap.put( RuleEngineUtils.ENV_VAR_EVENT_ID, create( ruleEvent.event(),
                RuleValueType.TEXT, Arrays.asList( ruleEvent.event() ), currentDate ) );

            String status = ruleEvent.status().toString();
            valueMap.put( RuleEngineUtils.ENV_VAR_EVENT_STATUS, create( status,
                RuleValueType.TEXT, Arrays.asList( status ), currentDate ) );

            String organisationUnit = ruleEvent.organisationUnit();
            valueMap.put( RuleEngineUtils.ENV_VAR_OU, create( organisationUnit, RuleValueType.TEXT ) );

            String programStageId = ruleEvent.programStage();
            valueMap.put( RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_ID, create( programStageId, RuleValueType.TEXT ) );

            String programStageName = ruleEvent.programStageName();
            valueMap.put( RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_NAME, create( programStageName, RuleValueType.TEXT ) );

            String organisationUnitCode = ruleEvent.organisationUnitCode();
            valueMap.put( RuleEngineUtils.ENV_VAR_OU_CODE, create( organisationUnitCode, RuleValueType.TEXT ) );
        }

        return valueMap;
    }

    private Map<String, RuleVariableValue> buildRuleVariableValues()
    {
        Map<String, RuleVariableValue> valueMap = new HashMap<>();

        // map data values within all events to data elements
        Map<String, List<RuleDataValueHistory>> allEventValues = buildAllEventValues();

        // map tracked entity attributes to values from enrollment
        Map<String, RuleAttributeValue> currentEnrollmentValues = buildCurrentEnrollmentValues();

        // build a map of current event values
        Map<String, RuleDataValue> currentEventValues = buildCurrentEventValues();

        for ( RuleVariable ruleVariable : ruleVariables )
        {

            valueMap.putAll(
                ruleVariable.createValues( this, allEventValues, currentEnrollmentValues, currentEventValues ) );
        }

        return valueMap;
    }
}
