package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleVariableCurrentEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableCurrentEvent ruleVariableCurrentEvent = RuleVariableCurrentEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertEquals( "test_variable" , ruleVariableCurrentEvent.name() );
        assertEquals( "test_dataelement" , ruleVariableCurrentEvent.dataElement() );
        assertEquals( RuleValueType.NUMERIC , ruleVariableCurrentEvent.dataElementType() );
    }
}
