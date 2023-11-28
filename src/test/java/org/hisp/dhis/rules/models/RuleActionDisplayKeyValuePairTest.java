package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleActionDisplayKeyValuePairTest
{
    @Test
    public void createForFeedbackMustSubstituteCorrectLocation()
    {
        RuleActionText displayTextAction = RuleActionText
            .createForFeedback( "test_content", "test_data" );

        assertEquals( RuleActionText.LOCATION_FEEDBACK_WIDGET , displayTextAction.location() );
        assertEquals( "test_content" , displayTextAction.content() );
        assertEquals( "test_data" , displayTextAction.data() );
    }

    @Test
    public void createForIndicatorsMustSubstituteCorrectLocation()
    {
        RuleActionText displayTextAction = RuleActionText
            .createForIndicators( "test_content", "test_data" );

        assertEquals( RuleActionText.LOCATION_INDICATOR_WIDGET , displayTextAction.location() );
        assertEquals( "test_content" , displayTextAction.content() );
        assertEquals( "test_data" , displayTextAction.data() );
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

        assertEquals( "" , ruleActionNoContent.content() );
        assertEquals( "test_data" , ruleActionNoContent.data() );

        assertEquals( "test_content" , ruleActionNoData.content() );
        assertEquals( "" , ruleActionNoData.data() );
    }

    @Test
    public void createForIndicatorsMustSubstituteEmptyStringsForNullArguments()
    {
        RuleActionText ruleActionNoContent = RuleActionText
            .createForIndicators( null, "test_data" );
        RuleActionText ruleActionNoData = RuleActionText
            .createForIndicators( "test_content", null );

        assertEquals( "" , ruleActionNoContent.content() );
        assertEquals( "test_data" , ruleActionNoContent.data() );

        assertEquals( "test_content" , ruleActionNoData.content() );
        assertEquals( "" , ruleActionNoData.data() );
    }

}
