package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.RuleVariableNewestEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableNewestEventTests
{

        @Test
        public void createShouldThrowOnNullName()
        {
                try
                {
                        RuleVariableNewestEvent.Companion.create( null, "test_dataelement", RuleValueType.TEXT );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( Exception exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullDataElement()
        {
                try
                {
                        RuleVariableNewestEvent.Companion.create( "test_variable", null, RuleValueType.TEXT );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( Exception exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullDataElementType()
        {
                try
                {
                        RuleVariableNewestEvent.Companion.create( "test_variable", "test_dataelement", null );
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
                RuleVariableNewestEvent ruleVariableNewestEvent = RuleVariableNewestEvent.Companion.create(
                    "test_variable", "test_dataelement", RuleValueType.NUMERIC );

                assertThat( ruleVariableNewestEvent.getName() ).isEqualTo( "test_variable" );
                assertThat( ruleVariableNewestEvent.getDataElement() ).isEqualTo( "test_dataelement" );
                assertThat( ruleVariableNewestEvent.getDataElementType() ).isEqualTo( RuleValueType.NUMERIC );
        }
}
