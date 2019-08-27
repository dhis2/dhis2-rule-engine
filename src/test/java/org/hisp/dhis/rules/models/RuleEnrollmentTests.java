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
                thrown.expect( NullPointerException.class );
                RuleEnrollment.Companion.create( null, new Date(), new Date(),
                    RuleEnrollment.Status.ACTIVE, null,null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test
        public void createShouldThrowOnNullIncidentDate()
        {
                thrown.expect( NullPointerException.class );
                RuleEnrollment.Companion.create("test_enrollment", null, new Date(),
                        RuleEnrollment.Status.ACTIVE, null, null, new ArrayList<RuleAttributeValue>(), "");

        }

        @Test
        public void createShouldThrowOnNullEnrollmentDate()
        {
                thrown.expect( NullPointerException.class );
                RuleEnrollment.Companion.create( "test_enrollment", new Date(), null,
                        RuleEnrollment.Status.ACTIVE,null,null, new ArrayList<RuleAttributeValue>(), "");
        }

        @Test
        public void createShouldThrowOnNullStatus()
        {
                thrown.expect( NullPointerException.class );
                RuleEnrollment.Companion.create( "test_enrollment", new Date(), new Date(),
                    null, null,null,new ArrayList<RuleAttributeValue>(), "");
        }

        @Test
        public void createShouldThrowOnNullValueList()
        {
                thrown.expect( NullPointerException.class );
                RuleEnrollment.Companion.create( "test_enrollment", new Date(), new Date(),
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

                RuleEnrollment ruleEnrollment = RuleEnrollment.Companion.create( "test_enrollment",
                    incidentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", "",
                    Arrays.asList( ruleAttributeValueOne, ruleAttributeValueTwo, ruleAttributeValueThree ), "");

                assertThat( ruleEnrollment.getEnrollment() ).isEqualTo( "test_enrollment" );
                assertThat( ruleEnrollment.getIncidentDate() ).isEqualTo( incidentDate );
                assertThat( ruleEnrollment.getEnrollmentDate() ).isEqualTo( enrollmentDate );
                assertThat( ruleEnrollment.getStatus() ).isEqualTo( RuleEnrollment.Status.ACTIVE );
                assertThat( ruleEnrollment.getAttributeValues().size() ).isEqualTo( 3 );
                assertThat( ruleEnrollment.getAttributeValues().get( 0 ) ).isEqualTo( ruleAttributeValueOne );
                assertThat( ruleEnrollment.getAttributeValues().get( 1 ) ).isEqualTo( ruleAttributeValueTwo );
                assertThat( ruleEnrollment.getAttributeValues().get( 2 ) ).isEqualTo( ruleAttributeValueThree );
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

                RuleEnrollment ruleEnrollment = RuleEnrollment.Companion.create( "test_enrollment",
                    new Date(), new Date(), RuleEnrollment.Status.ACTIVE, "", null,attributeValues, "");

                // mutating source array
                attributeValues.add( ruleAttributeValueThree );

                assertThat( ruleEnrollment.getAttributeValues().size() ).isEqualTo( 3 );
                assertThat( ruleEnrollment.getAttributeValues().get( 0 ) ).isEqualTo( ruleAttributeValueOne );
                assertThat( ruleEnrollment.getAttributeValues().get( 1 ) ).isEqualTo( ruleAttributeValueTwo );

                try
                {
                        ruleEnrollment.getAttributeValues().clear();
                        fail( "UnsupportedOperationException was expected, but nothing was thrown." );
                }
                catch ( UnsupportedOperationException unsupportedOperationException )
                {
                        // noop
                }
        }
}
