package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleVariableNewestEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableNewestEvent ruleVariableNewestEvent = RuleVariableNewestEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertEquals( "test_variable" , ruleVariableNewestEvent.name() );
        assertEquals( "test_dataelement" , ruleVariableNewestEvent.dataElement() );
        assertEquals( RuleValueType.NUMERIC , ruleVariableNewestEvent.dataElementType() );
    }
}
