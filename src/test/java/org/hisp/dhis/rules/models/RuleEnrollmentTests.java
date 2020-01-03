package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleEnrollmentTests
{
        @Test(expected = IllegalStateException.class )
        public void createShouldThrowOnNullEnrollment()
        {
                RuleEnrollment.create( null, new Date(), new Date(),
                    RuleEnrollment.Status.ACTIVE, null,null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test(expected = IllegalStateException.class )
        public void createShouldThrowOnNullIncidentDate()
        {
                RuleEnrollment.create("test_enrollment", null, new Date(),
                        RuleEnrollment.Status.ACTIVE, null, null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test(expected = IllegalStateException.class )
        public void createShouldThrowOnNullEnrollmentDate()
        {
                RuleEnrollment.create( "test_enrollment", new Date(), null,
                        RuleEnrollment.Status.ACTIVE,null,null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test(expected = IllegalStateException.class )
        public void createShouldThrowOnNullStatus()
        {
                RuleEnrollment.create( "test_enrollment", new Date(), new Date(),
                    null, null,null,new ArrayList<RuleAttributeValue>(), "");
        }

        @Test(expected = NullPointerException.class )
        public void createShouldThrowOnNullValueList()
        {
                RuleEnrollment.create( "test_enrollment", new Date(), new Date(),
                    RuleEnrollment.Status.ACTIVE, null,null,null, "");
        }

        @Test
        public void createShouldPropagatePropertiesCorrectly()
        {
                RuleAttributeValue ruleAttributeValueOne = mock( RuleAttributeValue.class );
                RuleAttributeValue ruleAttributeValueTwo = mock( RuleAttributeValue.class );
                RuleAttributeValue ruleAttributeValueThree = mock( RuleAttributeValue.class );

                Date incidentDate = new Date();
                Date enrollmentDate = new Date();

                RuleEnrollment ruleEnrollment = RuleEnrollment.create( "test_enrollment",
                    incidentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", "",
                    Arrays.asList( ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree ), "");

                assertThat( ruleEnrollment.enrollment() ).isEqualTo( "test_enrollment" );
                assertThat( ruleEnrollment.incidentDate() ).isEqualTo( incidentDate );
                assertThat( ruleEnrollment.enrollmentDate() ).isEqualTo( enrollmentDate );
                assertThat( ruleEnrollment.status() ).isEqualTo( RuleEnrollment.Status.ACTIVE );
                assertThat( ruleEnrollment.attributeValues().size() ).isEqualTo( 3 );
                assertThat( ruleEnrollment.attributeValues().get( 0 ) ).isEqualTo( ruleAttributeValueOne );
                assertThat( ruleEnrollment.attributeValues().get( 1 ) ).isEqualTo( ruleAttributeValueTwo );
                assertThat( ruleEnrollment.attributeValues().get( 2 ) ).isEqualTo( ruleAttributeValueThree );
        }

        @Test(expected = UnsupportedOperationException.class )
        public void createShouldReturnImmutableList()
        {
                RuleAttributeValue ruleAttributeValueOne = mock( RuleAttributeValue.class );
                RuleAttributeValue ruleAttributeValueTwo = mock( RuleAttributeValue.class );
                RuleAttributeValue ruleAttributeValueThree = mock( RuleAttributeValue.class );

                List<RuleAttributeValue> attributeValues = new ArrayList<>();
                attributeValues.add( ruleAttributeValueOne );
                attributeValues.add( ruleAttributeValueTwo );

                RuleEnrollment ruleEnrollment = RuleEnrollment.create( "test_enrollment",
                    new Date(), new Date(), RuleEnrollment.Status.ACTIVE, "", null,attributeValues, "");

                // mutating source array
                attributeValues.add( ruleAttributeValueThree );

                assertThat( ruleEnrollment.attributeValues().size() ).isEqualTo( 3 );
                assertThat( ruleEnrollment.attributeValues().get( 0 ) ).isEqualTo( ruleAttributeValueOne );
                assertThat( ruleEnrollment.attributeValues().get( 1 ) ).isEqualTo( ruleAttributeValueTwo );

                ruleEnrollment.attributeValues().clear();
        }
}
