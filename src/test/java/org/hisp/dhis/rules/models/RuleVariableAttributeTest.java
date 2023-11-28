package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleVariableAttributeTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableAttribute ruleVariableAttribute = RuleVariableAttribute.create(
            "test_variable", "test_attribute", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertEquals( "test_variable" , ruleVariableAttribute.name() );
        assertEquals( "test_attribute" , ruleVariableAttribute.trackedEntityAttribute() );
        assertEquals( RuleValueType.NUMERIC , ruleVariableAttribute.trackedEntityAttributeType() );
    }
}
