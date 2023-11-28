package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleValueType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleVariableValueTest
{
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_PATTERN, Locale.US );

    @Test
    public void textValuesMostBeWrapped()
    {
        RuleVariableValue variableValue = RuleVariableValue.create(
            "test_value", RuleValueType.TEXT, Arrays.asList(
                "test_value_candidate_one", "test_value_candidate_two" ), dateFormat.format( new Date() ) );

        assertEquals( "test_value" , variableValue.value() );
        assertEquals( RuleValueType.TEXT , variableValue.type() );
        assertEquals( 2 , variableValue.candidates().size() );
        assertEquals( "test_value_candidate_one" , variableValue.candidates().get( 0 ) );
        assertEquals( "test_value_candidate_two" , variableValue.candidates().get( 1 ) );
    }

    @Test
    public void wrappedTextValuesMustNotBeDoubleWrapped()
    {
        RuleVariableValue variableValue = RuleVariableValue.create(
            "test_value", RuleValueType.TEXT, Arrays.asList(
                "test_value_candidate_one", "test_value_candidate_two" ), dateFormat.format( new Date() ) );

        assertEquals( "test_value" , variableValue.value() );
        assertEquals( RuleValueType.TEXT , variableValue.type() );
        assertEquals( 2 , variableValue.candidates().size() );
        assertEquals( "test_value_candidate_one" , variableValue.candidates().get( 0 ) );
        assertEquals( "test_value_candidate_two" , variableValue.candidates().get( 1 ) );
    }

    @Test
    public void numericValuesMostNotBeWrapped()
    {
        RuleVariableValue variableValue = RuleVariableValue.create(
            "1", RuleValueType.NUMERIC, Arrays.asList( "2", "3" ), dateFormat.format( new Date() ) );

        assertEquals( "1" , variableValue.value() );
        assertEquals( RuleValueType.NUMERIC , variableValue.type() );
        assertEquals( 2 , variableValue.candidates().size() );
        assertEquals( "2" , variableValue.candidates().get( 0 ) );
        assertEquals( "3" , variableValue.candidates().get( 1 ) );
    }

    @Test
    public void booleanValuesMostNotBeWrapped()
    {
        RuleVariableValue variableValue = RuleVariableValue.create(
            "true", RuleValueType.BOOLEAN, Arrays.asList( "false", "false" ), dateFormat.format( new Date() ) );

        assertEquals( "true" , variableValue.value() );
        assertEquals( RuleValueType.BOOLEAN , variableValue.type() );
        assertEquals( 2 , variableValue.candidates().size() );
        assertEquals( "false" , variableValue.candidates().get( 0 ) );
        assertEquals( "false" , variableValue.candidates().get( 1 ) );
    }

    @Test( expected = NullPointerException.class )
    public void createShouldThrowOnNullCandidateList()
    {
        RuleVariableValue.create( "test_value", RuleValueType.TEXT, null, dateFormat.format( new Date() ) );
    }
}
