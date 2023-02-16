package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
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
        RuleActionDisplayKeyValuePair displayTextAction = RuleActionDisplayKeyValuePair
            .createForFeedback( "test_content", "test_data" );

        assertThat( displayTextAction.location() )
            .isEqualTo( RuleActionDisplayKeyValuePair.LOCATION_FEEDBACK_WIDGET );
        assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
        assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
    }

    @Test
    public void createForIndicatorsMustSubstituteCorrectLocation()
    {
        RuleActionDisplayKeyValuePair displayTextAction = RuleActionDisplayKeyValuePair
            .createForIndicators( "test_content", "test_data" );

        assertThat( displayTextAction.location() )
            .isEqualTo( RuleActionDisplayKeyValuePair.LOCATION_INDICATOR_WIDGET );
        assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
        assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createForFeedbackMustThrowWhenBothArgumentsNull()
    {
        RuleActionDisplayKeyValuePair.createForFeedback( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createForIndicatorsMustThrowWhenBothArgumentsNull()
    {
        RuleActionDisplayKeyValuePair.createForIndicators( null, null );
    }

    @Test
    public void createForFeedbackMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionDisplayKeyValuePair ruleActionNoContent = RuleActionDisplayKeyValuePair
            .createForFeedback( null, "test_data" );
        RuleActionDisplayKeyValuePair ruleActionNoData = RuleActionDisplayKeyValuePair
            .createForFeedback( "test_content", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
    }

    @Test
    public void createForIndicatorsMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionDisplayKeyValuePair ruleActionNoContent = RuleActionDisplayKeyValuePair
            .createForIndicators( null, "test_data" );
        RuleActionDisplayKeyValuePair ruleActionNoData = RuleActionDisplayKeyValuePair
            .createForIndicators( "test_content", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
    }

}
