package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleAttributeValueTest
{
    @Test
    public void propertiesShouldBePropagatedCorrectly()
    {
        RuleAttributeValue ruleAttributeValue = RuleAttributeValue
            .create( "test_tracked_entity_attribute", "test_value" );

        assertEquals( "test_tracked_entity_attribute" , ruleAttributeValue.trackedEntityAttribute() );
        assertEquals( "test_value" , ruleAttributeValue.value() );
    }
}
