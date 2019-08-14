package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.RuleVariableAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableAttributeTests
{

        @Test
        public void createShouldThrowOnNullName()
        {
                try
                {
                        RuleVariableAttribute.Companion.create( null, "test_attribute", RuleValueType.TEXT );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( Exception exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullTrackedEntityAttribute()
        {
                try
                {
                        RuleVariableAttribute.Companion.create( "test_variable", null, RuleValueType.TEXT );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( Exception exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullTrackedEntityAttributeType()
        {
                try
                {
                        RuleVariableAttribute.Companion.create( "test_variable", "test_attribute", null );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( Exception exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldPropagatePropertiesCorrectly()
        {
                RuleVariableAttribute ruleVariableAttribute = RuleVariableAttribute.Companion.create(
                    "test_variable", "test_attribute", RuleValueType.NUMERIC );

                assertThat( ruleVariableAttribute.getName() ).isEqualTo( "test_variable" );
                assertThat( ruleVariableAttribute.getTrackedEntityAttribute() ).isEqualTo( "test_attribute" );
                assertThat( ruleVariableAttribute.getTrackedEntityAttributeType() ).isEqualTo( RuleValueType.NUMERIC );
        }
}
