package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hisp.dhis.rules.models.RuleActionDisplayText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith( JUnit4.class )
public class RuleActionDisplayTextTests
{

        @Test
        public void createForFeedbackMustSubstituteCorrectLocation()
        {
                RuleActionDisplayText displayTextAction = RuleActionDisplayText.Companion
                    .createForFeedback( "test_content", "test_data" );

                assertThat( displayTextAction.location() )
                    .isEqualTo(RuleActionDisplayText.Companion.getLOCATION_FEEDBACK_WIDGET());
                assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
                assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
        }

        @Test
        public void createForIndicatorsMustSubstituteCorrectLocation()
        {
                RuleActionDisplayText displayTextAction = RuleActionDisplayText.Companion
                    .createForIndicators( "test_content", "test_data" );

                assertThat( displayTextAction.location() )
                    .isEqualTo(RuleActionDisplayText.Companion.getLOCATION_INDICATOR_WIDGET());
                assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
                assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
        }

        @Test
        public void createForFeedbackMustThrowWhenBothArgumentsNull()
        {
                try
                {
                        RuleActionDisplayText.Companion.createForFeedback( null, null );
                        fail( "IllegalArgumentException was expected, but nothing was thrown." );
                }
                catch ( IllegalArgumentException illegalArgumentException )
                {
                        // noop
                }
        }

        @Test
        public void createForIndicatorsMustThrowWhenBothArgumentsNull()
        {
                try
                {
                        RuleActionDisplayText.Companion.createForIndicators( null, null );
                        fail( "IllegalArgumentException was expected, but nothing was thrown." );
                }
                catch ( IllegalArgumentException illegalArgumentException )
                {
                        // noop
                }
        }

        @Test
        public void createForFeedbackMustSubstituteEmptyStringsForNullArguments()
        {
                RuleActionDisplayText ruleActionNoContent = RuleActionDisplayText.Companion
                    .createForFeedback( null, "test_data" );
                RuleActionDisplayText ruleActionNoData = RuleActionDisplayText.Companion
                    .createForFeedback( "test_content", null );

                assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
                assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

                assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
                assertThat( ruleActionNoData.data() ).isEqualTo( "" );
        }

        @Test
        public void createForIndicatorsMustSubstituteEmptyStringsForNullArguments()
        {
                RuleActionDisplayText ruleActionNoContent = RuleActionDisplayText.Companion
                    .createForIndicators( null, "test_data" );
                RuleActionDisplayText ruleActionNoData = RuleActionDisplayText.Companion
                    .createForIndicators( "test_content", null );

                assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
                assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

                assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
                assertThat( ruleActionNoData.data() ).isEqualTo( "" );
        }

        @Test
        public void equalsAndHashcodeFunctionsMustConformToContract()
        {
                EqualsVerifier.forClass( RuleActionDisplayText.Companion.createForFeedback( "", "" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
