package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.RuleVariableCurrentEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableCurrentEventTests
{

        @Test
        public void createShouldThrowOnNullName()
        {
                try
                {
                        RuleVariableCurrentEvent.create( null, "test_dataelement", RuleValueType.TEXT );
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
                        RuleVariableCurrentEvent.create( "test_variable", null, RuleValueType.TEXT );
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
                        RuleVariableCurrentEvent.create( "test_variable", "test_dataelement", null );
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
                RuleVariableCurrentEvent ruleVariableCurrentEvent = RuleVariableCurrentEvent.create(
                    "test_variable", "test_dataelement", RuleValueType.NUMERIC );

                assertThat( ruleVariableCurrentEvent.getName() ).isEqualTo( "test_variable" );
                assertThat( ruleVariableCurrentEvent.getDataElement() ).isEqualTo( "test_dataelement" );
                assertThat( ruleVariableCurrentEvent.getDataElementType() ).isEqualTo( RuleValueType.NUMERIC );
        }
}
