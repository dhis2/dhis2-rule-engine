package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionScheduleMessageTests
{

        @Test
        public void substitute_empty_strings_when_create_with_null_arguments()
        {
                RuleActionScheduleMessage ruleActionScheduleMessage = RuleActionScheduleMessage.Companion
                    .create( "notification", "data" );
                RuleActionScheduleMessage ruleActionScheduleMessageNoData = RuleActionScheduleMessage.Companion
                    .create( "notification", null );
                RuleActionScheduleMessage ruleActionScheduleMessageNoNotification = RuleActionScheduleMessage.Companion
                    .create( null, "data" );

                assertThat( ruleActionScheduleMessage.getNotification() ).isEqualTo( "notification" );
                assertThat( ruleActionScheduleMessage.getData() ).isEqualTo( "data" );

                assertThat( ruleActionScheduleMessageNoData.getNotification() ).isEqualTo( "notification" );
                assertThat( ruleActionScheduleMessageNoData.getData() ).isEqualTo( "" );

                assertThat( ruleActionScheduleMessageNoNotification.getNotification() ).isEqualTo( "" );
                assertThat( ruleActionScheduleMessageNoNotification.getData() ).isEqualTo( "data" );
        }
}
