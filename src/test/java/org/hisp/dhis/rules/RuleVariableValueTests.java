package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleValueType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleVariableValueTests
{
        private static final String DATE_PATTERN = "yyyy-MM-dd";

        @Mock
        private RuleDataValue ruleDataValue;

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        private SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );

        @Before
        public void setUp()
            throws Exception
        {
                MockitoAnnotations.initMocks( this );
        }

        @Test
        public void textValuesMostBeWrapped()
        {
                RuleVariableValue variableValue = RuleVariableValue.create(
                    "test_value", RuleValueType.TEXT, Arrays.asList(
                        "test_value_candidate_one", "test_value_candidate_two" ), dateFormat.format( new Date() ) );

                assertThat( variableValue.value() ).isEqualTo( "'test_value'" );
                assertThat( variableValue.type() ).isEqualTo( RuleValueType.TEXT );
                assertThat( variableValue.candidates().size() ).isEqualTo( 2 );
                assertThat( variableValue.candidates().get( 0 ) ).isEqualTo( "test_value_candidate_one" );
                assertThat( variableValue.candidates().get( 1 ) ).isEqualTo( "test_value_candidate_two" );
        }

        @Test
        public void wrappedTextValuesMustNotBeDoubleWrapped()
        {
                RuleVariableValue variableValue = RuleVariableValue.create(
                    "'test_value'", RuleValueType.TEXT, Arrays.asList(
                        "test_value_candidate_one", "test_value_candidate_two" ), dateFormat.format( new Date() ) );

                assertThat( variableValue.value() ).isEqualTo( "'test_value'" );
                assertThat( variableValue.type() ).isEqualTo( RuleValueType.TEXT );
                assertThat( variableValue.candidates().size() ).isEqualTo( 2 );
                assertThat( variableValue.candidates().get( 0 ) ).isEqualTo( "test_value_candidate_one" );
                assertThat( variableValue.candidates().get( 1 ) ).isEqualTo( "test_value_candidate_two" );
        }

        @Test
        public void numericValuesMostNotBeWrapped()
        {
                RuleVariableValue variableValue = RuleVariableValue.create(
                    "1", RuleValueType.NUMERIC, Arrays.asList( "2", "3" ), dateFormat.format( new Date() ) );

                assertThat( variableValue.value() ).isEqualTo( "1" );
                assertThat( variableValue.type() ).isEqualTo( RuleValueType.NUMERIC );
                assertThat( variableValue.candidates().size() ).isEqualTo( 2 );
                assertThat( variableValue.candidates().get( 0 ) ).isEqualTo( "2" );
                assertThat( variableValue.candidates().get( 1 ) ).isEqualTo( "3" );
        }

        @Test
        public void booleanValuesMostNotBeWrapped()
        {
                RuleVariableValue variableValue = RuleVariableValue.create(
                    "true", RuleValueType.BOOLEAN, Arrays.asList( "false", "false" ), dateFormat.format( new Date() ) );

                assertThat( variableValue.value() ).isEqualTo( "true" );
                assertThat( variableValue.type() ).isEqualTo( RuleValueType.BOOLEAN );
                assertThat( variableValue.candidates().size() ).isEqualTo( 2 );
                assertThat( variableValue.candidates().get( 0 ) ).isEqualTo( "false" );
                assertThat( variableValue.candidates().get( 1 ) ).isEqualTo( "false" );
        }

        @Test
        public void createShouldThrowOnNullValueType()
        {
                try
                {
                        thrown.expect( IllegalArgumentException.class );
                        RuleVariableValue.create( "test_value", null );
                }
                catch ( NullPointerException exception )
                {
                        // noop
                }
        }

        @Test
        public void createShouldThrowOnNullCandidateList()
        {
                try
                {
                        thrown.expect( IllegalArgumentException.class );
                        RuleVariableValue.create( "test_value", RuleValueType.TEXT, null, dateFormat.format( new Date() ) );
                }
                catch ( NullPointerException exception )
                {
                        // noop
                }
        }
}
