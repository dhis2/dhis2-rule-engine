package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleActionSendMessageTest
{
    @Test
    public void substitute_empty_strings_when_create_with_null_arguments()
    {
        RuleActionSendMessage ruleActionSendMessage = RuleActionSendMessage.create( "notification", "data" );
        RuleActionSendMessage ruleActionSendMessageNoData = RuleActionSendMessage
            .create( "notification", null );
        RuleActionSendMessage ruleActionSendMessageNoNotification = RuleActionSendMessage
            .create( null, "data" );

        assertEquals( "notification" , ruleActionSendMessage.notification() );
        assertEquals( "data" , ruleActionSendMessage.data() );

        assertEquals( "notification" , ruleActionSendMessageNoData.notification() );
        assertEquals( "" , ruleActionSendMessageNoData.data() );

        assertEquals( "" , ruleActionSendMessageNoNotification.notification() );
        assertEquals( "data" , ruleActionSendMessageNoNotification.data() );
    }
}
