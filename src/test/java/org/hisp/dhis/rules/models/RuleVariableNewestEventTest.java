package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableNewestEventTest
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullName()
    {
        RuleVariableNewestEvent.create( null, "test_dataelement", RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElement()
    {
        RuleVariableNewestEvent.create( "test_variable", null, RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElementType()
    {
        RuleVariableNewestEvent.create( "test_variable", "test_dataelement", null );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableNewestEvent ruleVariableNewestEvent = RuleVariableNewestEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC );

        assertThat( ruleVariableNewestEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariableNewestEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariableNewestEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
