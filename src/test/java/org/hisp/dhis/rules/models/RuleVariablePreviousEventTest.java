package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariablePreviousEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariablePreviousEvent ruleVariablePreviousEvent = RuleVariablePreviousEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertThat( ruleVariablePreviousEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariablePreviousEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariablePreviousEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
