package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleVariable;
import org.hisp.dhis.rules.util.MockRuleVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith( JUnit4.class )
public class RuleEngineContextTest
{
    private final RuleVariable ruleVariable = new MockRuleVariable();

    private final RuleVariable ruleVariableTwo = new MockRuleVariable();

    private final Rule rule = Rule.MOCK;

    private final Rule ruleTwo = Rule.MOCK;
    
    @Test( expected = IllegalArgumentException.class )
    public void builderShouldThrowOnNullVariableList()
    {
        RuleEngineContext.builder()
            .rules( new ArrayList<Rule>() )
            .ruleVariables( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void builderShouldThrowOnNullRulesList()
    {
        RuleEngineContext.builder()
            .ruleVariables( new ArrayList<RuleVariable>() )
            .ruleVariables( null );
    }

    @Test
    public void builderShouldContainImmutableCopyOfRules()
    {
        List<String> members = List.of( "one", "two" );
        Map<String, List<String>> supplementaryData = new HashMap<>();
        supplementaryData.put( "text-key", members );

        List<RuleVariable> ruleVariables = new ArrayList<>();
        List<Rule> rules = new ArrayList<>();

        ruleVariables.add( ruleVariable );
        rules.add( rule );

        RuleEngineContext ruleEngineContext = RuleEngineContext.builder()
            .ruleVariables( ruleVariables )
            .supplementaryData( supplementaryData )
            .rules( rules )
            .build();

        ruleVariables.add( ruleVariableTwo );
        rules.add( ruleTwo );

        assertEquals( 1 , ruleEngineContext.ruleVariables().size() );
        assertEquals( ruleVariable , ruleEngineContext.ruleVariables().get( 0 ) );

        assertEquals( 1 , ruleEngineContext.supplementaryData().size() );
        assertNotNull( ruleEngineContext.supplementaryData().get( "text-key" ) );
        assertEquals( members , ruleEngineContext.supplementaryData().get( "text-key" ) );

        assertEquals( 1 , ruleEngineContext.rules().size() );
        assertEquals( rule , ruleEngineContext.rules().get( 0 ) );

        try
        {
            ruleEngineContext.ruleVariables().clear();
            fail( "UnsupportedOperationException was expected, but nothing was thrown." );
        }
        catch ( UnsupportedOperationException unsupportedOperationException )
        {
            // noop
        }

        try
        {
            ruleEngineContext.rules().clear();
            fail( "UnsupportedOperationException was expected, but nothing was thrown." );
        }
        catch ( UnsupportedOperationException unsupportedOperationException )
        {
            // noop
        }
    }

    @Test
    public void toEngineBuilderShouldReturnNewInstances()
    {
        RuleEngineContext ruleEngineContext = RuleEngineContext.builder()
            .ruleVariables(List.of(new MockRuleVariable()))
            .supplementaryData( new HashMap<String, List<String>>() )
            .rules( List.of(Rule.MOCK) )
            .build();

        RuleEngine.Builder ruleEngineBuilderOne = ruleEngineContext.toEngineBuilder();
        RuleEngine.Builder ruleEngineBuilderTwo = ruleEngineContext.toEngineBuilder();

        assertNotEquals( ruleEngineBuilderOne, ruleEngineBuilderTwo );
    }
}
