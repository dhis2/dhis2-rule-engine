package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hisp.dhis.rules.RuleVariableValueAssert.assertThatVariable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith( JUnit4.class )
public class RuleVariableValueMapBuilderTests
{
        private static final String DATE_PATTERN = "yyyy-MM-dd";

        private SimpleDateFormat dateFormat;

        @Before
        public void setUp()
            throws Exception
        {
                dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );
        }

        @Test
        public void buildShouldReturnImmutableMap()
            throws ParseException
        {
                RuleEvent ruleEvent = mock( RuleEvent.class );
                when( ruleEvent.getEvent() ).thenReturn( "test_event_uid" );
                when( ruleEvent.getStatus() ).thenReturn( RuleEvent.Status.ACTIVE );
                when( ruleEvent.getEventDate() ).thenReturn( dateFormat.parse( "1994-02-03" ) );
                when( ruleEvent.getDueDate() ).thenReturn( dateFormat.parse( "1995-02-03" ) );
                when( ruleEvent.getProgramStageName() ).thenReturn( "" );
                when( ruleEvent.getProgramStage() ).thenReturn( "" );
                when( ruleEvent.getOrganisationUnit() ).thenReturn( "" );

                try
                {
                        RuleVariableValueMapBuilder.target( ruleEvent )
                            .ruleVariables( new ArrayList<RuleVariable>() )
                            .triggerEnvironment( TriggerEnvironment.SERVER )
                            .build().clear();
                        fail( "UnsupportedOperationException expected, but nothing was thrown" );
                }
                catch ( UnsupportedOperationException exception )
                {
                        // noop
                }
        }

        @Test
        public void ruleEnrollmentShouldThrowIfTargetEnrollmentIsAlreadySet()
        {
                try
                {
                        RuleEnrollment ruleEnrollment = mock( RuleEnrollment.class );
                        RuleVariableValueMapBuilder.target( ruleEnrollment )
                            .ruleEnrollment( ruleEnrollment )
                            .build();
                        fail( "IllegalStateException was expected, but nothing was thrown." );
                }
                catch ( IllegalStateException illegalStateException )
                {
                        // noop
                }
        }

        @Test
        public void currentEventVariableShouldContainValuesFromCurrentEvent()
            throws ParseException
        {
                RuleVariable ruleVariableOne = RuleVariableCurrentEvent.Companion.create(
                    "test_variable_one", "test_dataelement_one", RuleValueType.TEXT );
                RuleVariable ruleVariableTwo = RuleVariableCurrentEvent.Companion.create(
                    "test_variable_two", "test_dataelement_two", RuleValueType.TEXT );

                Date eventDate = dateFormat.parse( "2015-01-01" );
                Date dueDate = dateFormat.parse( "2016-01-01" );

                // values from context ruleEvents should be ignored
                RuleEvent contextEventOne = RuleEvent.Companion.create( "test_context_event_one", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null, Arrays.asList(
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_one", "test_context_value_one" ),
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_two", "test_context_value_two" ) ), "");
                RuleEvent contextEventTwo = RuleEvent.Companion.create( "test_context_event_two", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null,Arrays.asList(
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_one", "test_context_value_three" ),
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_two", "test_context_value_four" ) ), "");
                // values from current ruleEvent should be propagated to the variable values
                RuleEvent currentEvent = RuleEvent.Companion.create( "test_event_uid", "test_program_stage",
                    RuleEvent.Status.ACTIVE, eventDate, dueDate, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_one", "test_value_one" ),
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_two", "test_value_two" ) ), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( currentEvent )
                    .ruleVariables( Arrays.asList( ruleVariableOne, ruleVariableTwo ) )
                    .ruleEvents( Arrays.asList( contextEventOne, contextEventTwo ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 13 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) ).hasValue( wrap( dateFormat.format( eventDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( eventDate ) );

                assertThatVariable( valueMap.get( "event_status" ) ).hasValue( wrap(RuleEvent.Status.ACTIVE.toString()) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( RuleEvent.Status.ACTIVE.toString() );

                // event count variable should respect current event
                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "3" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "3" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid" );

                assertThatVariable( valueMap.get( "due_date" ) ).hasValue( wrap( dateFormat.format( dueDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dueDate ) );

                assertThatVariable( valueMap.get( "test_variable_one" ) ).hasValue( "'test_value_one'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_one" );

                assertThatVariable( valueMap.get( "test_variable_two" ) ).hasValue( "'test_value_two'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_two" );
        }

        @Test
        public void newestEventProgramVariableShouldContainValueFromNewestContextEvent()
            throws ParseException
        {
                RuleVariable ruleVariableOne = RuleVariableNewestEvent.Companion.create(
                    "test_variable_one", "test_dataelement_one", RuleValueType.TEXT );
                RuleVariable ruleVariableTwo = RuleVariableNewestEvent.Companion.create(
                    "test_variable_two", "test_dataelement_two", RuleValueType.TEXT );

                Date oldestEventDate = dateFormat.parse( "2013-01-01" );
                Date newestEventDate = dateFormat.parse( "2017-01-01" );
                Date currentEventDate = dateFormat.parse( "2015-01-01" );
                Date currentEventDueDate = dateFormat.parse( "2016-01-01" );

                RuleEvent oldestRuleEvent = RuleEvent.Companion.create( "test_event_uid_oldest", "test_program_stage",
                    RuleEvent.Status.ACTIVE, oldestEventDate, oldestEventDate, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( oldestEventDate, "test_program_stage",
                            "test_dataelement_one", "test_value_one_oldest" ),
                        RuleDataValue.Companion.create( oldestEventDate, "test_program_stage",
                            "test_dataelement_two", "test_value_two_oldest" ) ), "");
                RuleEvent newestRuleEvent = RuleEvent.Companion.create( "test_event_uid_newest", "test_program_stage",
                    RuleEvent.Status.ACTIVE, newestEventDate, newestEventDate, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( newestEventDate, "test_program_stage",
                            "test_dataelement_one", "test_value_one_newest" ),
                        RuleDataValue.Companion.create( newestEventDate, "test_program_stage",
                            "test_dataelement_two", "test_value_two_newest" ) ), "");
                RuleEvent currentEvent = RuleEvent.Companion.create( "test_event_uid_current", "test_program_stage",
                    RuleEvent.Status.ACTIVE, currentEventDate, currentEventDueDate, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( currentEventDate, "test_program_stage",
                            "test_dataelement_one", "test_value_one_current" ),
                        RuleDataValue.Companion.create( currentEventDate, "test_program_stage",
                            "test_dataelement_two", "test_value_two_current" ) ), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( currentEvent )
                    .ruleVariables( Arrays.asList( ruleVariableOne, ruleVariableTwo ) )
                    .ruleEvents( Arrays.asList( oldestRuleEvent, newestRuleEvent ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 13 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) )
                    .hasValue( wrap( dateFormat.format( currentEventDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( currentEventDate ) );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "3" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "3" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid_current'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid_current" );

                assertThatVariable( valueMap.get( "due_date" ) )
                    .hasValue( wrap( dateFormat.format( currentEventDueDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( currentEventDueDate ) );

                assertThatVariable( valueMap.get( "test_variable_one" ) ).hasValue( "'test_value_one_newest'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_one_newest",
                    "test_value_one_current", "test_value_one_oldest" );
                assertThatVariable( valueMap.get( "test_variable_two" ) ).hasValue( "'test_value_two_newest'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_two_newest",
                    "test_value_two_current", "test_value_two_oldest" );
        }

        @Test
        public void newestEventProgramVariableShouldReturnValuesFromCurrentEventWhenIfNewest()
            throws ParseException
        {
                RuleVariable ruleVariableOne = RuleVariableNewestEvent.Companion.create(
                    "test_variable_one", "test_dataelement_one", RuleValueType.TEXT );
                RuleVariable ruleVariableTwo = RuleVariableNewestEvent.Companion.create(
                    "test_variable_two", "test_dataelement_two", RuleValueType.TEXT );

                Date dateEventOne = dateFormat.parse( "2013-01-01" );
                Date dateEventTwo = dateFormat.parse( "2014-01-01" );
                Date dateEventCurrent = dateFormat.parse( "2015-01-01" );
                Date dateEventDueCurrent = dateFormat.parse( "2016-01-01" );

                RuleEvent firstRuleEvent = RuleEvent.Companion.create( "test_event_uid_one", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventOne, "test_program_stage",
                            "test_dataelement_one", "test_value_dataelement_one_first" ),
                        RuleDataValue.Companion.create( dateEventOne, "test_program_stage",
                            "test_dataelement_two", "test_value_dataelement_two_first" ) ), "");
                RuleEvent secondRuleEvent = RuleEvent.Companion.create( "test_event_uid_two", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventTwo, "test_program_stage",
                            "test_dataelement_one", "test_value_dataelement_one_second" ),
                        RuleDataValue.Companion.create( dateEventTwo, "test_program_stage",
                            "test_dataelement_two", "test_value_dataelement_two_second" ) ), "");
                RuleEvent currentEvent = RuleEvent.Companion.create( "test_event_uid_current", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventCurrent, "test_program_stage",
                            "test_dataelement_one", "test_value_dataelement_one_current" ),
                        RuleDataValue.Companion.create( dateEventCurrent, "test_program_stage",
                            "test_dataelement_two", "test_value_dataelement_two_current" ) ), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( currentEvent )
                    .ruleVariables( Arrays.asList( ruleVariableOne, ruleVariableTwo ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .ruleEvents( Arrays.asList( firstRuleEvent, secondRuleEvent ) )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 13 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) )
                    .hasValue( wrap( dateFormat.format( dateEventCurrent ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventCurrent ) );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "3" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "3" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid_current'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid_current" );

                assertThatVariable( valueMap.get( "due_date" ) )
                    .hasValue( wrap( dateFormat.format( dateEventDueCurrent ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventDueCurrent ) );

                assertThatVariable( valueMap.get( "test_variable_one" ) )
                    .hasValue( "'test_value_dataelement_one_current'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_dataelement_one_current",
                    "test_value_dataelement_one_second", "test_value_dataelement_one_first" );

                assertThatVariable( valueMap.get( "test_variable_two" ) )
                    .hasValue( "'test_value_dataelement_two_current'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_dataelement_two_current",
                    "test_value_dataelement_two_second", "test_value_dataelement_two_first" );
        }

        @Test
        public void newestEventProgramStageVariableShouldContainValueFromNewestContextEvent()
            throws ParseException
        {
                RuleVariable ruleVariable = RuleVariableNewestStageEvent.Companion.create( "test_variable",
                    "test_dataelement", "test_program_stage_one", RuleValueType.TEXT );

                Date dateEventOne = dateFormat.parse( "2014-02-03" );
                Date dateEventTwo = dateFormat.parse( "2014-03-03" );
                Date dateEventThree = dateFormat.parse( "2015-02-03" );
                Date dateEventCurrent = dateFormat.parse( "2011-02-03" );
                Date dateEventDueCurrent = dateFormat.parse( "2011-02-03" );

                RuleEvent eventOne = RuleEvent.Companion.create( "test_event_uid_one", "test_program_stage_one",
                    RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne,"",null, Arrays.asList(
                        RuleDataValue.Companion.create( dateEventOne, "test_program_stage_one",
                            "test_dataelement", "test_value_one" ) ), "");
                RuleEvent eventTwo = RuleEvent.Companion.create( "test_event_uid_two", "test_program_stage_two",
                    RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventTwo, "test_program_stage_two",
                            "test_dataelement", "test_value_two" ) ), "");
                RuleEvent eventThree = RuleEvent.Companion.create( "test_event_uid_three", "test_program_stage_two",
                    RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventThree, "test_program_stage_two",
                            "test_dataelement", "test_value_three" ) ), "");
                RuleEvent eventCurrent = RuleEvent.Companion.create( "test_event_uid_current", "test_program_stage_one",
                    RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventDueCurrent,"",null, Arrays.asList(
                        RuleDataValue.Companion.create( dateEventCurrent, "test_program_stage_one",
                            "test_dataelement", "test_value_current" ) ), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( eventCurrent )
                    .ruleVariables( Arrays.asList( ruleVariable ) )
                    .ruleEvents( Arrays.asList( eventOne, eventTwo, eventThree ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 12 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) )
                    .hasValue( wrap( dateFormat.format( dateEventCurrent ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventCurrent ) );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "4" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "4" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid_current'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid_current" );

                assertThatVariable( valueMap.get( "due_date" ) )
                    .hasValue( wrap( dateFormat.format( dateEventDueCurrent ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventDueCurrent ) );

                assertThatVariable( valueMap.get( "test_variable" ) ).hasValue( "'test_value_one'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_value_one", "test_value_current" );
        }

        @Test
        public void newestEventProgramStageVariableShouldNotContainAnyValues()
            throws ParseException
        {
                RuleVariable ruleVariable = RuleVariableNewestStageEvent.Companion.create( "test_variable",
                    "test_dataelement", "test_program_stage_one", RuleValueType.TEXT );

                Date dateEventOne = dateFormat.parse( "2014-03-03" );
                Date dateEventTwo = dateFormat.parse( "2015-02-03" );

                RuleEvent ruleEventOne = RuleEvent.Companion.create( "test_event_uid_one", "test_program_stage_two",
                    RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne,"", null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventOne, "test_program_stage_two",
                            "test_dataelement", "test_value_one" ) ), "");
                RuleEvent ruleEventTwo = RuleEvent.Companion.create( "test_event_uid_two", "test_program_stage_two",
                    RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo,"",null, Arrays.asList(
                        RuleDataValue.Companion.create( dateEventTwo, "test_program_stage_two",
                            "test_dataelement", "test_value_two" ) ), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEventTwo )
                    .ruleVariables( Arrays.asList( ruleVariable ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .ruleEvents( Arrays.asList( ruleEventOne ) )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 12 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) ).hasValue( wrap( dateFormat.format( dateEventTwo ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventTwo ) );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "2" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "2" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid_two'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid_two" );

                assertThatVariable( valueMap.get( "due_date" ) ).hasValue( wrap( dateFormat.format( dateEventTwo ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventTwo ) );

                assertThatVariable( valueMap.get( "test_variable" ) ).hasValue( null )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates();
        }

        @Test
        public void previousEventVariableShouldContainValuesFromPreviousEvent()
            throws ParseException
        {
                RuleVariable ruleVariable = RuleVariablePreviousEvent.Companion.create( "test_variable",
                    "test_dataelement", RuleValueType.TEXT );

                Date dateEventOne = dateFormat.parse( "2014-02-03" );
                Date dateEventTwo = dateFormat.parse( "2014-03-03" );
                Date dateEventThree = dateFormat.parse( "2015-02-03" );
                Date dateEventCurrent = dateFormat.parse( "2014-05-03" );

                RuleEvent ruleEventOne = RuleEvent.Companion.create( "test_event_uid_one", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventOne, dateEventOne,"",null, Arrays.asList(
                        RuleDataValue.Companion.create( dateEventOne, "test_program_stage_one",
                            "test_dataelement", "test_value_one" ) ), "");
                RuleEvent ruleEventTwo = RuleEvent.Companion.create( "test_event_uid_two", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventTwo, dateEventTwo,"",null, Arrays.asList(
                        RuleDataValue.Companion.create( dateEventTwo, "test_program_stage_two",
                            "test_dataelement", "test_value_two" ) ), "");
                RuleEvent ruleEventThree = RuleEvent.Companion.create( "test_event_uid_three", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventThree, dateEventThree, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventThree, "test_program_stage_two",
                            "test_dataelement", "test_value_three" ) ), "");
                RuleEvent ruleEventCurrent = RuleEvent.Companion.create( "test_event_uid_current", "test_program_stage",
                    RuleEvent.Status.ACTIVE, dateEventCurrent, dateEventCurrent, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( dateEventCurrent, "test_program_stage_one",
                            "test_dataelement", "test_value_current" ) ), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEventCurrent )
                    .ruleVariables( Arrays.asList( ruleVariable ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .ruleEvents( Arrays.asList( ruleEventOne, ruleEventTwo, ruleEventThree ) )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 12 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) )
                    .hasValue( wrap( dateFormat.format( dateEventCurrent ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventCurrent ) );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "4" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "4" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid_current'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid_current" );

                assertThatVariable( valueMap.get( "due_date" ) )
                    .hasValue( wrap( dateFormat.format( dateEventCurrent ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( dateEventCurrent ) );

                assertThatVariable( valueMap.get( "test_variable" ) ).hasValue( "'test_value_two'" )
                    .isTypeOf( RuleValueType.TEXT )
                    .hasCandidates( "test_value_three", "test_value_current", "test_value_two", "test_value_one" );
        }

        @Test
        public void attributeVariableShouldContainValuesFromContextEnrollment()
            throws ParseException
        {
                RuleVariable ruleVariableOne = RuleVariableAttribute.Companion.create( "test_variable_one",
                    "test_attribute_one", RuleValueType.TEXT );
                RuleVariable ruleVariableTwo = RuleVariableAttribute.Companion.create( "test_variable_two",
                    "test_attribute_two", RuleValueType.TEXT );

                Date eventDate = dateFormat.parse( "2015-01-01" );
                Date enrollmentDate = dateFormat.parse( "2014-03-01" );

                // values from ruleEnrollment should end up in ruleVariables
                RuleEnrollment ruleEnrollment = RuleEnrollment.Companion.create( "test_enrollment",
                    enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "",null,Arrays.asList(
                        RuleAttributeValue.Companion.create( "test_attribute_one", "test_attribute_value_one" ),
                        RuleAttributeValue.Companion.create( "test_attribute_two", "test_attribute_value_two" ) ), "");

                // values from context ruleEvents should be ignored
                RuleEvent contextEvent = RuleEvent.Companion.create( "test_context_event_one", "test_program_stage",
                    RuleEvent.Status.ACTIVE, eventDate, new Date(), "",null,Arrays.asList(
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_one", "test_context_value_one" ),
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_two", "test_context_value_two" ) ), "");
                RuleEvent currentEvent = RuleEvent.Companion.create( "test_event_uid", "test_program_stage",
                    RuleEvent.Status.ACTIVE, eventDate, eventDate, "",null,Arrays.asList(
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_one", "test_value_one" ),
                        RuleDataValue.Companion.create( eventDate, "test_program_stage",
                            "test_dataelement_two", "test_value_two" ) ), "");

                // here we will expect correct values to be returned
                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( currentEvent )
                    .ruleEnrollment( ruleEnrollment )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .ruleVariables( Arrays.asList( ruleVariableOne, ruleVariableTwo ) )
                    .ruleEvents( Arrays.asList( contextEvent ) )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 20 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( dateFormat.format( new Date() ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( new Date() ) );

                assertThatVariable( valueMap.get( "event_date" ) ).hasValue( wrap( dateFormat.format( eventDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( eventDate ) );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "2" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "2" );

                assertThatVariable( valueMap.get( "event_id" ) ).hasValue( "'test_event_uid'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_event_uid" );

                assertThatVariable( valueMap.get( "due_date" ) ).hasValue( wrap( dateFormat.format( eventDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( eventDate ) );

                assertThatVariable( valueMap.get( "enrollment_status" ) ).hasValue( wrap( RuleEnrollment.Status.ACTIVE.toString() ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( RuleEnrollment.Status.ACTIVE.toString() );

                assertThatVariable( valueMap.get( "enrollment_date" ) )
                    .hasValue( wrap( dateFormat.format( enrollmentDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( enrollmentDate ) );

                assertThatVariable( valueMap.get( "enrollment_id" ) ).hasValue( "'test_enrollment'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_enrollment" );

                assertThatVariable( valueMap.get( "enrollment_count" ) ).hasValue( "1" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "1" );

                assertThatVariable( valueMap.get( "incident_date" ) )
                    .hasValue( wrap( dateFormat.format( enrollmentDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( enrollmentDate ) );

                assertThatVariable( valueMap.get( "tei_count" ) ).hasValue( "1" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "1" );

                assertThatVariable( valueMap.get( "test_variable_one" ) ).hasValue( "'test_attribute_value_one'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_attribute_value_one" );
                assertThatVariable( valueMap.get( "test_variable_two" ) ).hasValue( "'test_attribute_value_two'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_attribute_value_two" );
        }

        @Test
        public void ruleEnrollmentValuesShouldBePropagatedToMapCorrectly()
            throws ParseException
        {
                RuleVariable ruleVariableOne = RuleVariableAttribute.Companion.create( "test_variable_one",
                    "test_attribute_one", RuleValueType.NUMERIC );
                RuleVariable ruleVariableTwo = RuleVariableAttribute.Companion.create( "test_variable_two",
                    "test_attribute_two", RuleValueType.TEXT );
                RuleVariable ruleVariableThree = RuleVariableCurrentEvent.Companion.create( "test_variable_three",
                    "test_dataelement_one", RuleValueType.BOOLEAN );

                String currentDate = dateFormat.format( new Date() );
                Date enrollmentDate = dateFormat.parse( "2017-02-02" );
                Date incidentDate = dateFormat.parse( "2017-04-02" );
                RuleEnrollment ruleEnrollment = RuleEnrollment.Companion.create( "test_enrollment", incidentDate,
                    enrollmentDate, RuleEnrollment.Status.ACTIVE, "",null,Arrays.asList(
                        RuleAttributeValue.Companion.create( "test_attribute_one", "test_attribute_value_one" ),
                        RuleAttributeValue.Companion.create( "test_attribute_two", "test_attribute_value_two" ),
                        RuleAttributeValue.Companion.create( "test_attribute_three", "test_attribute_value_three" ) ), "");

                RuleEvent ruleEventOne = RuleEvent.Companion.create( "test_event_one", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null,new ArrayList<RuleDataValue>(), "");
                RuleEvent ruleEventTwo = RuleEvent.Companion.create( "test_event_two", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null,new ArrayList<RuleDataValue>(), "");

                Map<String, RuleVariableValue> valueMap = RuleVariableValueMapBuilder.target( ruleEnrollment )
                    .ruleVariables( Arrays.asList( ruleVariableOne, ruleVariableTwo, ruleVariableThree ) )
                    .ruleEvents( Arrays.asList( ruleEventOne, ruleEventTwo ) )
                    .triggerEnvironment( TriggerEnvironment.SERVER )
                    .build();

                assertThat( valueMap.size() ).isEqualTo( 14 );

                assertThatVariable( valueMap.get( "current_date" ) ).hasValue( wrap( currentDate ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( currentDate );

                assertThatVariable( valueMap.get( "event_count" ) ).hasValue( "2" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "2" );

                assertThatVariable( valueMap.get( "enrollment_date" ) )
                    .hasValue( wrap( dateFormat.format( enrollmentDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( enrollmentDate ) );

                assertThatVariable( valueMap.get( "enrollment_id" ) ).hasValue( "'test_enrollment'" )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( "test_enrollment" );

                assertThatVariable( valueMap.get( "enrollment_count" ) ).hasValue( "1" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "1" );

                assertThatVariable( valueMap.get( "incident_date" ) )
                    .hasValue( wrap( dateFormat.format( incidentDate ) ) )
                    .isTypeOf( RuleValueType.TEXT ).hasCandidates( dateFormat.format( incidentDate ) );

                assertThatVariable( valueMap.get( "tei_count" ) ).hasValue( "1" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "1" );

                assertThatVariable( valueMap.get( "test_variable_one" ) ).hasValue( "test_attribute_value_one" )
                    .isTypeOf( RuleValueType.NUMERIC ).hasCandidates( "test_attribute_value_one" );

                assertThatVariable( valueMap.get( "test_variable_two" ) ).isTypeOf( RuleValueType.TEXT )
                    .hasValue( "'test_attribute_value_two'" ).hasCandidates( "test_attribute_value_two" );
        }

        @Test
        public void buildShouldThrowOnDuplicateEvent()
        {
                RuleEvent ruleEvent = RuleEvent.Companion.create( "test_event_two", "test_program_stage",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null,new ArrayList<RuleDataValue>(), "");

                try
                {
                        RuleVariableValueMapBuilder.target( ruleEvent )
                            .ruleVariables( new ArrayList<RuleVariable>() )
                            .ruleEvents( Arrays.asList( ruleEvent ) )
                            .build();

                        fail( "IllegalStateException was expected, but nothing was thrown." );
                }
                catch ( IllegalStateException illegalStateException )
                {
                        // noop
                }
        }

        @Nonnull
        private static String wrap( @Nonnull String source )
        {
                return String.format( Locale.US, "'%s'", source );
        }
}
