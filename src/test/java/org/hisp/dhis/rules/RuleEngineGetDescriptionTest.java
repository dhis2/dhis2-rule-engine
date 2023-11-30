package org.hisp.dhis.rules;

/*
 * Copyright (c) 2004-2020, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionText;
import org.hisp.dhis.rules.models.RuleEngineValidationException;
import org.hisp.dhis.rules.models.RuleValidationResult;
import org.hisp.dhis.rules.models.TriggerEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Zubair Asghar
 */

@RunWith( JUnit4.class )
public class RuleEngineGetDescriptionTest
{
    private static final String test_var_one = "Variable_ONE";
    private static final String test_var_two = "Variable_TWO";
    private static final String test_var_three = "Variable_THREE";
    private static final String test_var_date_one = "2020-01-01";
    private static final String test_var_date_two = "2020-02-02";
    private static final String completionDate = "Completion date";
    private static final String currentDate = "Current date";
    private static final String constant = "PI";
    private static final String test_var_number = "9";

    private Map<String, DataItem> itemStore = new HashMap<>();

    private final RuleAction ruleAction = RuleActionText.createForFeedback("", "" );

    @Before
    public void setUp()
    {
        itemStore = new HashMap<>();

        DataItem var_1 = DataItem.builder().value( test_var_one ).valueType( ItemValueType.TEXT ).build();
        DataItem var_2 = DataItem.builder().value( test_var_two ).valueType( ItemValueType.TEXT ).build();
        DataItem var_8 = DataItem.builder().value( test_var_three ).valueType( ItemValueType.TEXT ).build();
        DataItem var_3 = DataItem.builder().value( test_var_date_one ).valueType( ItemValueType.DATE ).build();
        DataItem var_4 = DataItem.builder().value( test_var_date_two ).valueType( ItemValueType.DATE ).build();
        DataItem var_5 = DataItem.builder().value( completionDate ).valueType( ItemValueType.DATE ).build();
        DataItem var_6 = DataItem.builder().value( constant ).valueType( ItemValueType.TEXT ).build();
        DataItem var_7 = DataItem.builder().value( currentDate ).valueType( ItemValueType.DATE ).build();
        DataItem var_9 = DataItem.builder().value( test_var_number ).valueType( ItemValueType.NUMBER ).build();

        itemStore.put( "test_var_one", var_1 );
        itemStore.put( "test_var_two", var_2 );
        itemStore.put( "test_var_date_one", var_3 );
        itemStore.put( "test_var_date_two", var_4 );
        itemStore.put( "completed_date", var_5 );
        itemStore.put( "NAgjOfWMXg6", var_6 );
        itemStore.put( "current_date", var_7 );
        itemStore.put( "test_var_three", var_8 );
        itemStore.put( "test_var_number", var_9 );
    }

    @Test
    public void evaluateGetDescriptionWithIncorrectRules()
    {
        Rule incorrectRuleHasValue = Rule
            .create( null, null, "d2:hasValue(#{test_var_one} + 1)", List.of(ruleAction), "", "" );
        Rule incorrectSyntaxRule = Rule
            .create( null, null, "d2:daysBetween((#{test_var_date_one},#{test_var_date_two})",
                    List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( incorrectRuleHasValue.condition() );

        assertNotNull( result );
        assertFalse( result.valid() );

        ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        result = ruleEngine.evaluate( incorrectSyntaxRule.condition() );

        assertNotNull( result );
        assertFalse( result.valid() );
    }

    @Test
    public void evaluateGetDescriptionWithInvalidProgramRuleVariable()
    {
        Rule rule = Rule.create( null, null, "d2:hasValue(#{test_var_one1})", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( rule.condition() );

        assertNotNull( result );
        assertFalse( result.valid() );
    }

    @Test
    public void getDescriptionForLengthFunction()
    {
        Rule rule = Rule.create( null, null, "d2:length(#{test_var_one}) > 0", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( rule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );

        rule = Rule.create( null, null, "d2:length(#{test_var_date_one}) > 0 ", List.of(ruleAction), "", "" );

        result = ruleEngine.evaluate( rule.condition() );

        assertNotNull( result );
        assertFalse( result.valid() );

        rule = Rule.create( null, null, "d2:length(#{test_var_number}) > 0 ", List.of(ruleAction), "", "" );

        result = ruleEngine.evaluate( rule.condition() );

        assertNotNull( result );
        assertFalse( result.valid() );
    }

    @Test
    public void testGetDescriptionWithD2FunctionsAndLogicalAnd()
    {
        Rule correctMultipleD2FunctionRule = Rule
            .create( null, null, "d2:count(#{test_var_one}) > 0 && d2:hasValue(#{test_var_two})",
                    List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( correctMultipleD2FunctionRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithD2FunctionsTEA()
    {
        Rule conditionWithD2FunctionsTEA = Rule
            .create( null, null, "d2:hasValue('test_var_three')", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( conditionWithD2FunctionsTEA.condition() );

        assertNotNull( result );
        assertEquals( "d2:hasValue(Variable_THREE)", result.description() );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithPlainAttributeComparisonWithName()
    {
        Rule conditionWithD2FunctionsTEA = Rule
            .create( null, null, "'test_var_three' == 'email'", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( conditionWithD2FunctionsTEA.condition() );

        assertNotNull( result );
        assertEquals( "'test_var_three' == 'email'", result.description() );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithPlainAttributeComparison()
    {
        Rule conditionWithD2FunctionsTEA = Rule
            .create( null, null, "A{test_var_three} == 'email'", List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( conditionWithD2FunctionsTEA.condition() );

        assertNotNull( result );
        assertEquals( "Variable_THREE == 'email'", result.description() );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionStringLiterals()
    {
        String condition = " true && false || 1 > 3";
        Rule literalStringRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( literalStringRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );

    }

    @Test
    public void testGetDescriptionD2BetweenFunction()
    {
        String condition = "d2:daysBetween(#{test_var_date_one},#{test_var_date_two}) > 0";
        Rule correctD2betweenFunctionRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( correctD2betweenFunctionRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionD2BetweenFunctionWithEnvironmentVariables()
    {
        String condition = "d2:daysBetween(V{completed_date},V{current_date}) > 0";
        Rule correctD2betweenFunctionRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( correctD2betweenFunctionRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionD2FunctionAttribute()
    {
        String condition = "A{test_var_number} > 0";
        Rule withoutD2AttFunctionRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( withoutD2AttFunctionRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithD2FunctionDataElement()
    {
        String condition = "#{test_var_number} > 0";
        Rule withoutD2DEFunctionRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( withoutD2DEFunctionRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithConstant()
    {
        String condition = "C{NAgjOfWMXg6} == 0";
        Rule constantRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( constantRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithProgramEnvironmentVariable()
    {
        String condition = "d2:hasValue(V{completed_date})";
        Rule programEnvVariableRule = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( programEnvVariableRule.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithSingleD2Function()
    {
        String condition = "d2:hasValue(#{test_var_one})";
        Rule correctRuleHasValue = Rule.create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( correctRuleHasValue.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithMultipleD2FunctionsAndLogicalOROperator()
    {
        String condition = "d2:hasValue(#{test_var_two}) || d2:count(#{test_var_one}) > 0 ";

        Rule correctMultipleD2FunctionRuleWithOr = Rule
            .create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( correctMultipleD2FunctionRuleWithOr.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionWithMultipleD2FunctionsAndLogicalANDOperator()
    {
        String condition = "d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 ";

        Rule correctMultipleD2FunctionRuleWithAnd = Rule
            .create( null, null, condition, List.of(ruleAction), "", "" );

        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();
        RuleValidationResult result = ruleEngine.evaluate( correctMultipleD2FunctionRuleWithAnd.condition() );

        assertNotNull( result );
        assertTrue( result.valid() );
    }

    @Test
    public void testGetDescriptionForDataFieldExpression()
    {
        RuleEngine ruleEngine = getRuleEngineBuilderForDescription( itemStore ).build();

        RuleValidationResult result = ruleEngine.evaluateDataFieldExpression( "1 + 1" );
        assertNotNull( result );
        assertTrue( result.valid() );

        result = ruleEngine.evaluateDataFieldExpression( "d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 " );
        assertNotNull( result );
        assertTrue( result.valid() );

        result = ruleEngine.evaluateDataFieldExpression( "1 + 1 +" );
        assertNotNull( result );
        assertFalse( result.valid() );
        assertTrue(result.exception() instanceof RuleEngineValidationException);

        result = ruleEngine.evaluateDataFieldExpression( "d2:hasValue(#{test_var_two}) && d2:count(#{test_var_one}) > 0 (" );
        assertNotNull( result );
        assertFalse( result.valid() );
        assertTrue( result.exception() instanceof RuleEngineValidationException);
    }

    private RuleEngine.Builder getRuleEngineBuilderForDescription( Map<String, DataItem> itemStore )
    {
        return RuleEngineContext
            .builder()
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() ).itemStore( itemStore ).ruleEngineItent( RuleEngineIntent.DESCRIPTION )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }
}
