package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableCurrentEventTests
{

        @Test(expected = NullPointerException.class )
        public void createShouldThrowOnNullName()
        {
                RuleVariableCurrentEvent.create( null, "test_dataelement", RuleValueType.TEXT );
        }

        @Test(expected = NullPointerException.class )
        public void createShouldThrowOnNullDataElement()
        {
                RuleVariableCurrentEvent.create( "test_variable", null, RuleValueType.TEXT );
        }

        @Test(expected = NullPointerException.class )
        public void createShouldThrowOnNullDataElementType()
        {
                RuleVariableCurrentEvent.create( "test_variable", "test_dataelement", null );
        }

        @Test
        public void createShouldPropagatePropertiesCorrectly()
        {
                RuleVariableCurrentEvent ruleVariableCurrentEvent = RuleVariableCurrentEvent.create(
                    "test_variable", "test_dataelement", RuleValueType.NUMERIC );

                assertThat( ruleVariableCurrentEvent.name() ).isEqualTo( "test_variable" );
                assertThat( ruleVariableCurrentEvent.dataElement() ).isEqualTo( "test_dataelement" );
                assertThat( ruleVariableCurrentEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
        }
}
