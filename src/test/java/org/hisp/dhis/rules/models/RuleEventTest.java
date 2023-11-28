package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

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
        RuleDataValue ruleDataValue = RuleDataValue.MOCK;

        List<RuleDataValue> ruleDataValues = new ArrayList<>();
        ruleDataValues.add( ruleDataValue );

        RuleEvent ruleEvent = RuleEvent.create( "test_event_uid", "test_stage_uid",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", "", ruleDataValues, "", null);

        // add another data value
        ruleDataValues.add( ruleDataValue );

        assertEquals( 1 , ruleEvent.dataValues().size() );
        assertEquals( ruleDataValue , ruleEvent.dataValues().get( 0 ) );

        ruleEvent.dataValues().add( ruleDataValue );
    }

    @Test
    public void createShouldPropagateValuesCorrectly()
    {
        RuleDataValue ruleDataValue = RuleDataValue.MOCK;

        List<RuleDataValue> ruleDataValues = new ArrayList<>();
        ruleDataValues.add( ruleDataValue );

        Date eventDate = new Date();
        Date dueDate = new Date();

        RuleEvent ruleEvent = RuleEvent.create( "test_event_uid", "test_stage_uid",
            RuleEvent.Status.ACTIVE, eventDate, dueDate, "", "", ruleDataValues, "", null);

        assertEquals( "test_event_uid" , ruleEvent.event() );
        assertEquals( RuleEvent.Status.ACTIVE , ruleEvent.status() );
        assertEquals( "test_stage_uid" , ruleEvent.programStage() );
        assertEquals( eventDate , ruleEvent.eventDate() );
        assertEquals( dueDate , ruleEvent.dueDate() );

        assertEquals( 1 , ruleEvent.dataValues().size() );
        assertEquals( ruleDataValue , ruleEvent.dataValues().get( 0 ) );
    }

    @Test
    public void eventDateComparatorTest()
        throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );
        List<RuleEvent> ruleEvents = new ArrayList<>(List.of(
            RuleEvent.create( "test_event_one", "test_program_stage_one", RuleEvent.Status.ACTIVE,
                dateFormat.parse( "2014-02-11" ), dateFormat.parse( "2014-02-11" ), "", null,
                    new ArrayList<>(), "", null),
            RuleEvent.create( "test_event_two", "test_program_stage_two", RuleEvent.Status.ACTIVE,
                dateFormat.parse( "2017-03-22" ), dateFormat.parse( "2017-03-22" ), "", null,
                    new ArrayList<>(), "", null) ));

        ruleEvents.sort(RuleEvent.EVENT_DATE_COMPARATOR);

        assertEquals( "test_event_two" , ruleEvents.get( 0 ).event() );
        assertEquals( "test_event_one" , ruleEvents.get( 1 ).event() );
    }
}
