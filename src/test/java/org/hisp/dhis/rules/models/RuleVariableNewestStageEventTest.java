package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableNewestStageEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableNewestStageEvent ruleVariablePreviousEvent = RuleVariableNewestStageEvent.create(
            "test_variable", "test_dataelement", "test_programstage", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertThat( ruleVariablePreviousEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariablePreviousEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariablePreviousEvent.programStage() ).isEqualTo( "test_programstage" );
        assertThat( ruleVariablePreviousEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
