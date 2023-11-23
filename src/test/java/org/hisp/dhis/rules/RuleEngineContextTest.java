package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleVariable;
import org.hisp.dhis.rules.util.MockRuleVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleEngineContextTest
{
    private RuleVariable ruleVariable = new MockRuleVariable();

    private RuleVariable ruleVariableTwo = new MockRuleVariable();

    private Rule rule = Rule.MOCK;

    private Rule ruleTwo = Rule.MOCK;
    
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
        List<String> members = Arrays.asList( "one", "two" );
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

        assertThat( ruleEngineContext.ruleVariables().size() ).isEqualTo( 1 );
        assertThat( ruleEngineContext.ruleVariables().get( 0 ) ).isEqualTo( ruleVariable );

        assertThat( ruleEngineContext.supplementaryData().size() ).isEqualTo( 1 );
        assertThat( ruleEngineContext.supplementaryData().get( "text-key" ) ).isNotNull();
        assertThat( ruleEngineContext.supplementaryData().get( "text-key" ) ).isEqualTo( members );

        assertThat( ruleEngineContext.rules().size() ).isEqualTo( 1 );
        assertThat( ruleEngineContext.rules().get( 0 ) ).isEqualTo( rule );

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
            .ruleVariables( Arrays.asList( new MockRuleVariable() ) )
            .supplementaryData( new HashMap<String, List<String>>() )
            .rules( List.of(Rule.MOCK) )
            .build();

        RuleEngine.Builder ruleEngineBuilderOne = ruleEngineContext.toEngineBuilder();
        RuleEngine.Builder ruleEngineBuilderTwo = ruleEngineContext.toEngineBuilder();

        assertThat( ruleEngineBuilderOne ).isNotEqualTo( ruleEngineBuilderTwo );
    }
}
