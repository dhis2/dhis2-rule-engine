package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariablePreviousEventTests
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullName()
    {
        RuleVariablePreviousEvent.create( null, "test_dataelement", RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElement()
    {
        RuleVariablePreviousEvent.create( "test_variable", null, RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElementType()
    {
        RuleVariablePreviousEvent.create( "test_variable", "test_dataelement", null );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariablePreviousEvent ruleVariablePreviousEvent = RuleVariablePreviousEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC );

        assertThat( ruleVariablePreviousEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariablePreviousEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariablePreviousEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
