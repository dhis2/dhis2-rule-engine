package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleAttributeValueTests
{

        @Test(expected = NullPointerException.class )
        public void shouldThrowOnNullTrackedEntityAttribute()
        {
                RuleAttributeValue.create( null, "test_value" );
        }

        @Test(expected = NullPointerException.class )
        public void shouldThrowOnNullValue()
        {
                RuleAttributeValue.create( "test_tracked_entity_attribute", null );
        }

        @Test
        public void propertiesShouldBePropagatedCorrectly()
        {
                RuleAttributeValue ruleAttributeValue = RuleAttributeValue
                    .create( "test_tracked_entity_attribute", "test_value" );

                assertThat( ruleAttributeValue.trackedEntityAttribute() ).isEqualTo( "test_tracked_entity_attribute" );
                assertThat( ruleAttributeValue.value() ).isEqualTo( "test_value" );
        }
}
