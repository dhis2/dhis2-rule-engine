package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableNewestStageEventTests
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullName()
    {
        RuleVariableNewestStageEvent
            .create( null, "test_dataelement", "test_programstage", RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElement()
    {
        RuleVariableNewestStageEvent
            .create( "test_variable", null, "test_programstage", RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullProgramStage()
    {
        RuleVariableNewestStageEvent
            .create( "test_variable", "test_dataelement", null, RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElementType()
    {
        RuleVariableNewestStageEvent
            .create( "test_variable", "test_dataelement", "test_programstage", null );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableNewestStageEvent ruleVariablePreviousEvent = RuleVariableNewestStageEvent.create(
            "test_variable", "test_dataelement", "test_programstage", RuleValueType.NUMERIC );

        assertThat( ruleVariablePreviousEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariablePreviousEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariablePreviousEvent.programStage() ).isEqualTo( "test_programstage" );
        assertThat( ruleVariablePreviousEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
