package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleVariableNewestStageEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableNewestStageEvent ruleVariablePreviousEvent = RuleVariableNewestStageEvent.create(
            "test_variable", "test_dataelement", "test_programstage", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertEquals( "test_variable" , ruleVariablePreviousEvent.name() );
        assertEquals( "test_dataelement" , ruleVariablePreviousEvent.dataElement() );
        assertEquals( "test_programstage" , ruleVariablePreviousEvent.programStage() );
        assertEquals( RuleValueType.NUMERIC , ruleVariablePreviousEvent.dataElementType() );
    }
}
