package org.hisp.dhis.rules.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleEventTests
{
        private static final String DATE_PATTERN = "yyyy-MM-dd";

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void createShouldThrowExceptionIfEventIsNull()
        {
                thrown.expect( IllegalStateException.class );
                RuleEvent.create( null, "test_programstage", RuleEvent.Status.ACTIVE,
                        new Date(), new Date(), null,null,Arrays.<RuleDataValue>asList(), "", null);
        }

        @Test
        public void createShouldThrowExceptionIfProgramStageIsNull()
        {
                thrown.expect( IllegalStateException.class );
                RuleEvent.create( "test_event", null, RuleEvent.Status.ACTIVE,
                        new Date(), new Date(), null,null,Arrays.<RuleDataValue>asList(), "", null);
        }

        @Test
        public void createShouldThrowExceptionIfStatusIsNull()
        {
                thrown.expect( IllegalStateException.class );
                RuleEvent.create( "test_event", "test_programstage", null,
                        new Date(), new Date(), null,null,Arrays.<RuleDataValue>asList(), "", null);
        }

        @Test
        public void createShouldThrowExceptionIfEventDateIsNull()
        {
                thrown.expect( IllegalStateException.class );
                RuleEvent.create( "test_event", "test_programstage", RuleEvent.Status.ACTIVE,
                        null, new Date(), null,null,Arrays.<RuleDataValue>asList(), "", null);
        }

        @Test
        public void createShouldThrowExceptionIfDueDateIsNull()
        {
                thrown.expect( IllegalStateException.class );
                RuleEvent.create( "test_event", "test_programstage", RuleEvent.Status.ACTIVE,
                    new Date(), null, null,null,Arrays.<RuleDataValue>asList(), "", null);

        }

        @Test
        public void createShouldThrowExceptionIfListOfDataValuesIsNull()
        {
                thrown.expect( NullPointerException.class );
                RuleEvent.create( "test_event", "test_programstage", RuleEvent.Status.ACTIVE, new Date(), new Date(),
                        null, null,null, "", null);

        }

        @Test
        public void createShouldPropagateImmutableList()
        {
                RuleDataValue ruleDataValue = mock( RuleDataValue.class );

                List<RuleDataValue> ruleDataValues = new ArrayList<>();
                ruleDataValues.add( ruleDataValue );

                RuleEvent ruleEvent = RuleEvent.create( "test_event_uid", "test_stage_uid",
                    RuleEvent.Status.ACTIVE, new Date(), new Date(), "", "", ruleDataValues, "", null);

                // add another data value
                ruleDataValues.add( ruleDataValue );

                assertThat( ruleEvent.dataValues().size() ).isEqualTo( 1 );
                assertThat( ruleEvent.dataValues().get( 0 ) ).isEqualTo( ruleDataValue );

                try
                {
                        ruleEvent.dataValues().add( ruleDataValue );
                        fail( "UnsupportedOperationException was expected, but nothing was thrown" );
                }
                catch ( UnsupportedOperationException exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldPropagateValuesCorrectly()
        {
                RuleDataValue ruleDataValue = mock( RuleDataValue.class );

                List<RuleDataValue> ruleDataValues = new ArrayList<>();
                ruleDataValues.add( ruleDataValue );

                Date eventDate = new Date();
                Date dueDate = new Date();

                RuleEvent ruleEvent = RuleEvent.create( "test_event_uid", "test_stage_uid",
                    RuleEvent.Status.ACTIVE, eventDate, dueDate, "","",ruleDataValues, "", null);

                assertThat( ruleEvent.event() ).isEqualTo( "test_event_uid" );
                assertThat( ruleEvent.status() ).isEqualTo( RuleEvent.Status.ACTIVE );
                assertThat( ruleEvent.programStage() ).isEqualTo( "test_stage_uid" );
                assertThat( ruleEvent.eventDate() ).isEqualTo( eventDate );
                assertThat( ruleEvent.dueDate() ).isEqualTo( dueDate );

                assertThat( ruleEvent.dataValues().size() ).isEqualTo( 1 );
                assertThat( ruleEvent.dataValues().get( 0 ) ).isEqualTo( ruleDataValue );
        }

        @Test
        public void eventDateComparatorTest()
            throws ParseException
        {
                SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );
                List<RuleEvent> ruleEvents = Arrays.asList(
                    RuleEvent.create( "test_event_one", "test_program_stage_one", RuleEvent.Status.ACTIVE,
                        dateFormat.parse( "2014-02-11" ), dateFormat.parse( "2014-02-11" ),"",null,
                        new ArrayList<RuleDataValue>(), "", null),
                    RuleEvent.create( "test_event_two", "test_program_stage_two", RuleEvent.Status.ACTIVE,
                        dateFormat.parse( "2017-03-22" ), dateFormat.parse( "2017-03-22" ), "",null,
                        new ArrayList<RuleDataValue>(), "", null) );

                Collections.sort( ruleEvents, RuleEvent.EVENT_DATE_COMPARATOR );

                assertThat( ruleEvents.get( 0 ).event() ).isEqualTo( "test_event_two" );
                assertThat( ruleEvents.get( 1 ).event() ).isEqualTo( "test_event_one" );
        }
}
