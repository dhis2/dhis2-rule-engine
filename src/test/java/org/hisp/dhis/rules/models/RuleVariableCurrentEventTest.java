package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableCurrentEventTest
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullName()
    {
        RuleVariableCurrentEvent.create( null, "test_dataelement", RuleValueType.TEXT, true, new ArrayList<>());
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElement()
    {
        RuleVariableCurrentEvent.create( "test_variable", null, RuleValueType.TEXT, true, new ArrayList<>());
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullDataElementType()
    {
        RuleVariableCurrentEvent.create( "test_variable", "test_dataelement", null, true, new ArrayList<>());
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableCurrentEvent ruleVariableCurrentEvent = RuleVariableCurrentEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertThat( ruleVariableCurrentEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariableCurrentEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariableCurrentEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
