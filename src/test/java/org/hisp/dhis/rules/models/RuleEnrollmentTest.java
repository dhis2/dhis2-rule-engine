package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleEnrollmentTest
{
    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullValueList()
    {
        RuleEnrollment.create( "test_enrollment", new Date(), new Date(),
            RuleEnrollment.Status.ACTIVE, null, null, null, "" );
    }

    @Test
    public void createShouldPropagatePropertiesCorrectly()
    {
        RuleAttributeValue ruleAttributeValueOne = RuleAttributeValue.MOCK;
        RuleAttributeValue ruleAttributeValueTwo = RuleAttributeValue.MOCK;
        RuleAttributeValue ruleAttributeValueThree = RuleAttributeValue.MOCK;

        Date incidentDate = new Date();
        Date enrollmentDate = new Date();

        RuleEnrollment ruleEnrollment = RuleEnrollment.create( "test_enrollment",
            incidentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", "",
            Arrays.asList( ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree ), "" );

        assertEquals( "test_enrollment" , ruleEnrollment.enrollment() );
        assertEquals( incidentDate , ruleEnrollment.incidentDate() );
        assertEquals( enrollmentDate , ruleEnrollment.enrollmentDate() );
        assertEquals( RuleEnrollment.Status.ACTIVE , ruleEnrollment.status() );
        assertEquals( 3 , ruleEnrollment.attributeValues().size() );
        assertEquals( ruleAttributeValueOne , ruleEnrollment.attributeValues().get( 0 ) );
        assertEquals( ruleAttributeValueTwo , ruleEnrollment.attributeValues().get( 1 ) );
        assertEquals( ruleAttributeValueThree , ruleEnrollment.attributeValues().get( 2 ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void createShouldReturnImmutableList()
    {
        RuleAttributeValue ruleAttributeValueOne = RuleAttributeValue.MOCK;
        RuleAttributeValue ruleAttributeValueTwo = RuleAttributeValue.MOCK;
        RuleAttributeValue ruleAttributeValueThree = RuleAttributeValue.MOCK;

        List<RuleAttributeValue> attributeValues = new ArrayList<>();
        attributeValues.add( ruleAttributeValueOne );
        attributeValues.add( ruleAttributeValueTwo );

        RuleEnrollment ruleEnrollment = RuleEnrollment.create( "test_enrollment",
            new Date(), new Date(), RuleEnrollment.Status.ACTIVE, "", null, attributeValues, "" );

        // mutating source array
        attributeValues.add( ruleAttributeValueThree );

        assertEquals( 3 , ruleEnrollment.attributeValues().size() );
        assertEquals( ruleAttributeValueOne , ruleEnrollment.attributeValues().get( 0 ) );
        assertEquals( ruleAttributeValueTwo , ruleEnrollment.attributeValues().get( 1 ) );

        ruleEnrollment.attributeValues().clear();
    }
}
