package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleActionDisplayTextTest
{

    @Test
    public void createForFeedbackMustSubstituteCorrectLocation()
    {
        RuleActionDisplayText displayTextAction = RuleActionDisplayText
            .createForFeedback( "test_content", "test_data" );

        assertThat( displayTextAction.location() )
            .isEqualTo( RuleActionDisplayText.LOCATION_FEEDBACK_WIDGET );
        assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
        assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
    }

    @Test
    public void createForIndicatorsMustSubstituteCorrectLocation()
    {
        RuleActionDisplayText displayTextAction = RuleActionDisplayText
            .createForIndicators( "test_content", "test_data" );

        assertThat( displayTextAction.location() )
            .isEqualTo( RuleActionDisplayText.LOCATION_INDICATOR_WIDGET );
        assertThat( displayTextAction.content() ).isEqualTo( "test_content" );
        assertThat( displayTextAction.data() ).isEqualTo( "test_data" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createForFeedbackMustThrowWhenBothArgumentsNull()
    {
        RuleActionDisplayText.createForFeedback( null, null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void createForIndicatorsMustThrowWhenBothArgumentsNull()
    {
        RuleActionDisplayText.createForIndicators( null, null );
    }

    @Test
    public void createForFeedbackMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionDisplayText ruleActionNoContent = RuleActionDisplayText
            .createForFeedback( null, "test_data" );
        RuleActionDisplayText ruleActionNoData = RuleActionDisplayText
            .createForFeedback( "test_content", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
    }

    @Test
    public void createForIndicatorsMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionDisplayText ruleActionNoContent = RuleActionDisplayText
            .createForIndicators( null, "test_data" );
        RuleActionDisplayText ruleActionNoData = RuleActionDisplayText
            .createForIndicators( "test_content", null );

        assertThat( ruleActionNoContent.content() ).isEqualTo( "" );
        assertThat( ruleActionNoContent.data() ).isEqualTo( "test_data" );

        assertThat( ruleActionNoData.content() ).isEqualTo( "test_content" );
        assertThat( ruleActionNoData.data() ).isEqualTo( "" );
    }

    @Test
    public void equalsAndHashcodeFunctionsMustConformToContract()
    {
        EqualsVerifier.forClass( RuleActionDisplayText.createForFeedback( "", "" ).getClass() )
            .suppress( Warning.NULL_FIELDS )
            .verify();
    }
}
