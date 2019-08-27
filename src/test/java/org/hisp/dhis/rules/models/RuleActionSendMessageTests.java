package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionSendMessageTests
{

        @Test
        public void substitute_empty_strings_when_create_with_null_arguments()
        {
                RuleActionSendMessage ruleActionSendMessage = RuleActionSendMessage.create( "notification", "data" );
                RuleActionSendMessage ruleActionSendMessageNoData = RuleActionSendMessage
                    .create( "notification", null );
                RuleActionSendMessage ruleActionSendMessageNoNotification = RuleActionSendMessage
                    .create( null, "data" );

                assertThat( ruleActionSendMessage.getNotification() ).isEqualTo( "notification" );
                assertThat( ruleActionSendMessage.getData() ).isEqualTo( "data" );

                assertThat( ruleActionSendMessageNoData.getNotification() ).isEqualTo( "notification" );
                assertThat( ruleActionSendMessageNoData.getData() ).isEqualTo( "" );

                assertThat( ruleActionSendMessageNoNotification.getNotification() ).isEqualTo( "" );
                assertThat( ruleActionSendMessageNoNotification.getData() ).isEqualTo( "data" );
        }
}
