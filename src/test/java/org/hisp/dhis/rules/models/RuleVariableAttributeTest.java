package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableAttributeTest
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullName()
    {
        RuleVariableAttribute.create( null, "test_attribute", RuleValueType.TEXT, true, new ArrayList<>());
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullTrackedEntityAttribute()
    {
        RuleVariableAttribute.create( "test_variable", null, RuleValueType.TEXT, true, new ArrayList<>());
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullTrackedEntityAttributeType()
    {
        RuleVariableAttribute.create( "test_variable", "test_attribute", null, true, new ArrayList<>());
    }

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
