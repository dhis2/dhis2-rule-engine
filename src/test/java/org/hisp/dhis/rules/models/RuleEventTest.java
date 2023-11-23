package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.util.MockRuleDataValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleEventTest
{
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Test( expected = NullPointerException.class )
    public void createShouldThrowExceptionIfListOfDataValuesIsNull()
    {
        RuleEvent.create( "test_event", "test_programstage", RuleEvent.Status.ACTIVE, new Date(), new Date(),
            null, null, null, "", null);

    }

    @Test( expected = UnsupportedOperationException.class )
    public void createShouldPropagateImmutableList()
    {
        RuleDataValue ruleDataValue = new MockRuleDataValue();

        List<RuleDataValue> ruleDataValues = new ArrayList<>();
        ruleDataValues.add( ruleDataValue );

        RuleEvent ruleEvent = RuleEvent.create( "test_event_uid", "test_stage_uid",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", "", ruleDataValues, "", null);

        // add another data value
        ruleDataValues.add( ruleDataValue );

        assertThat( ruleEvent.dataValues().size() ).isEqualTo( 1 );
        assertThat( ruleEvent.dataValues().get( 0 ) ).isEqualTo( ruleDataValue );

        ruleEvent.dataValues().add( ruleDataValue );
    }

    @Test
    public void createShouldPropagateValuesCorrectly()
    {
        RuleDataValue ruleDataValue = new MockRuleDataValue();

        List<RuleDataValue> ruleDataValues = new ArrayList<>();
        ruleDataValues.add( ruleDataValue );

        Date eventDate = new Date();
        Date dueDate = new Date();

        RuleEvent ruleEvent = RuleEvent.create( "test_event_uid", "test_stage_uid",
            RuleEvent.Status.ACTIVE, eventDate, dueDate, "", "", ruleDataValues, "", null);

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
                dateFormat.parse( "2014-02-11" ), dateFormat.parse( "2014-02-11" ), "", null,
                new ArrayList<RuleDataValue>(), "", null),
            RuleEvent.create( "test_event_two", "test_program_stage_two", RuleEvent.Status.ACTIVE,
                dateFormat.parse( "2017-03-22" ), dateFormat.parse( "2017-03-22" ), "", null,
                new ArrayList<RuleDataValue>(), "", null) );

        Collections.sort( ruleEvents, RuleEvent.EVENT_DATE_COMPARATOR );

        assertThat( ruleEvents.get( 0 ).event() ).isEqualTo( "test_event_two" );
        assertThat( ruleEvents.get( 1 ).event() ).isEqualTo( "test_event_one" );
    }
}
