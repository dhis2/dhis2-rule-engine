package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleAttributeValueTest
{
    @Test
    public void propertiesShouldBePropagatedCorrectly()
    {
        RuleAttributeValue ruleAttributeValue = RuleAttributeValue
            .create( "test_tracked_entity_attribute", "test_value" );

        assertThat( ruleAttributeValue.trackedEntityAttribute() ).isEqualTo( "test_tracked_entity_attribute" );
        assertThat( ruleAttributeValue.value() ).isEqualTo( "test_value" );
    }
}
