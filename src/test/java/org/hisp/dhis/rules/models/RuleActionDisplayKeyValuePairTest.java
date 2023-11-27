package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionDisplayKeyValuePairTest
{

    @Test
    public void createForFeedbackMustSubstituteCorrectLocation()
    {
        RuleActionText displayTextAction = RuleActionText
            .createForFeedback( "test_content", "test_data" );

        assertThat( displayTextAction.location() )
            .isEqualTo( RuleActionText.LOCATION_FEEDBACK_WIDGET );
        assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
        assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
    }

    @Test
    public void createForIndicatorsMustSubstituteCorrectLocation()
    {
        RuleActionText displayTextAction = RuleActionText
            .createForIndicators( "test_content", "test_data" );

        assertThat( displayTextAction.location() )
            .isEqualTo( RuleActionText.LOCATION_INDICATOR_WIDGET );
        assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
        assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createForFeedbackMustThrowWhenBothArgumentsNull()
    {
        RuleActionText.createForFeedback( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createForIndicatorsMustThrowWhenBothArgumentsNull()
    {
        RuleActionText.createForIndicators( null, null );
    }

    @Test
    public void createForFeedbackMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionText ruleActionNoContent = RuleActionText
            .createForFeedback( null, "test_data" );
        RuleActionText ruleActionNoData = RuleActionText
            .createForFeedback( "test_content", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
    }

    @Test
    public void createForIndicatorsMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionText ruleActionNoContent = RuleActionText
            .createForIndicators( null, "test_data" );
        RuleActionText ruleActionNoData = RuleActionText
            .createForIndicators( "test_content", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
    }

}
