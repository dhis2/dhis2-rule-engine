package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleActionScheduleMessageTest
{
    @Test
    public void substitute_empty_strings_when_create_with_null_arguments()
    {
        RuleActionScheduleMessage ruleActionScheduleMessage = RuleActionScheduleMessage
            .create( "notification", "data" );
        RuleActionScheduleMessage ruleActionScheduleMessageNoData = RuleActionScheduleMessage
            .create( "notification", null );
        RuleActionScheduleMessage ruleActionScheduleMessageNoNotification = RuleActionScheduleMessage
            .create( null, "data" );

        assertEquals( "notification" , ruleActionScheduleMessage.notification() );
        assertEquals( "data" , ruleActionScheduleMessage.data() );

        assertEquals( "notification" , ruleActionScheduleMessageNoData.notification() );
        assertEquals( "" , ruleActionScheduleMessageNoData.data() );

        assertEquals( "" , ruleActionScheduleMessageNoNotification.notification() );
        assertEquals( "data" , ruleActionScheduleMessageNoNotification.data() );
    }
}
