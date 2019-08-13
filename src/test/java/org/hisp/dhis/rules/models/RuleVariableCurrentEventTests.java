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
                        RuleVariableCurrentEvent.Companion.create( null, "test_dataelement", RuleValueType.TEXT );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( NullPointerException exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullDataElement()
        {
                try
                {
                        RuleVariableCurrentEvent.Companion.create( "test_variable", null, RuleValueType.TEXT );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( NullPointerException exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullDataElementType()
        {
                try
                {
                        RuleVariableCurrentEvent.Companion.create( "test_variable", "test_dataelement", null );
                        fail( "NullPointerException is expected, but nothing was thrown" );
                }
                catch ( NullPointerException exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldPropagatePropertiesCorrectly()
        {
                RuleVariableCurrentEvent ruleVariableCurrentEvent = RuleVariableCurrentEvent.Companion.create(
                    "test_variable", "test_dataelement", RuleValueType.NUMERIC );

                assertThat( ruleVariableCurrentEvent.name() ).isEqualTo( "test_variable" );
                assertThat( ruleVariableCurrentEvent.dataElement() ).isEqualTo( "test_dataelement" );
                assertThat( ruleVariableCurrentEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
        }
}
