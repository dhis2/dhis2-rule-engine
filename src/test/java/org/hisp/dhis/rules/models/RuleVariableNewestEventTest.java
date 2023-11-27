package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableNewestEventTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableNewestEvent ruleVariableNewestEvent = RuleVariableNewestEvent.create(
            "test_variable", "test_dataelement", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertThat( ruleVariableNewestEvent.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariableNewestEvent.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleVariableNewestEvent.dataElementType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
