package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableAttributeTest
{
    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableAttribute ruleVariableAttribute = RuleVariableAttribute.create(
            "test_variable", "test_attribute", RuleValueType.NUMERIC, true, new ArrayList<>());

        assertThat( ruleVariableAttribute.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariableAttribute.trackedEntityAttribute() ).isEqualTo( "test_attribute" );
        assertThat( ruleVariableAttribute.trackedEntityAttributeType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
