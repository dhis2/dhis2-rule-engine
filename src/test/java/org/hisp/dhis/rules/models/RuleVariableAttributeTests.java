package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableAttributeTests
{

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullName()
    {
        RuleVariableAttribute.create( null, "test_attribute", RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullTrackedEntityAttribute()
    {
        RuleVariableAttribute.create( "test_variable", null, RuleValueType.TEXT );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullTrackedEntityAttributeType()
    {
        RuleVariableAttribute.create( "test_variable", "test_attribute", null );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleVariableAttribute ruleVariableAttribute = RuleVariableAttribute.create(
            "test_variable", "test_attribute", RuleValueType.NUMERIC );

        assertThat( ruleVariableAttribute.name() ).isEqualTo( "test_variable" );
        assertThat( ruleVariableAttribute.trackedEntityAttribute() ).isEqualTo( "test_attribute" );
        assertThat( ruleVariableAttribute.trackedEntityAttributeType() ).isEqualTo( RuleValueType.NUMERIC );
    }
}
