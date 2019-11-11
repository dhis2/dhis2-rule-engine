package org.hisp.dhis.rules.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleEnrollmentTests
{
        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Test
        public void createShouldThrowOnNullEnrollment()
        {
                thrown.expect( IllegalStateException.class );
                RuleEnrollment.create( null, new Date(), new Date(),
                    RuleEnrollment.Status.ACTIVE, null,null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test
        public void createShouldThrowOnNullIncidentDate()
        {
                thrown.expect( IllegalStateException.class );
                RuleEnrollment.create("test_enrollment", null, new Date(),
                        RuleEnrollment.Status.ACTIVE, null, null, new ArrayList<RuleAttributeValue>(), "");

        }

        @Test
        public void createShouldThrowOnNullEnrollmentDate()
        {
                thrown.expect( IllegalStateException.class );
                RuleEnrollment.create( "test_enrollment", new Date(), null,
                        RuleEnrollment.Status.ACTIVE,null,null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test
        public void createShouldThrowOnNullStatus()
        {
                thrown.expect( IllegalStateException.class );
                RuleEnrollment.create( "test_enrollment", new Date(), new Date(),
                    null, null,null,new ArrayList<RuleAttributeValue>(), "");
        }

        @Test
        public void createShouldThrowOnNullValueList()
        {
                thrown.expect( NullPointerException.class );
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

        @Test
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

                try
                {
                        ruleEnrollment.attributeValues().clear();
                        fail( "UnsupportedOperationException was expected, but nothing was thrown." );
                }
                catch ( UnsupportedOperationException unsupportedOperationException )
                {
                        // noop
                }
        }
}
