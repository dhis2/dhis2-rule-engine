package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleVariablePreviousEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariablePreviousEvent ruleVariablePreviousEvent = RuleVariablePreviousEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertEquals( "test_variable" , ruleVariablePreviousEvent.name() );
        assertEquals( "test_dataelement" , ruleVariablePreviousEvent.dataElement() );
        assertEquals( RuleValueType.NUMERIC , ruleVariablePreviousEvent.dataElementType() );
    }
}
