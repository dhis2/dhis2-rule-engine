package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.util.MockRuleVariable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( JUnit4.class )
public class RuleEngineTest
{
    private RuleEngineContext ruleEngineContext;

    @Before
    public void setUp()
    {
        ruleEngineContext = RuleEngineContext.builder()
            .ruleVariables(List.of(new MockRuleVariable()))
            .rules( List.of(Rule.MOCK) )
            .build();
    }

    @Test( expected = IllegalArgumentException.class )
    public void builderShouldThrowOnNullEnrollment()
    {
        ruleEngineContext.toEngineBuilder()
            .enrollment( null )
            .events( new ArrayList<RuleEvent>() )
            .build();
    }

    @Test( expected = IllegalArgumentException.class )
    public void builderShouldThrowOnNullEvents()
    {
        ruleEngineContext.toEngineBuilder()
            .enrollment( null )
            .events( null )
            .build();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void builderShouldPropagateImmutableEventsList()
    {
        RuleEvent ruleEventOne = RuleEvent.MOCK;
        RuleEvent ruleEventTwo = RuleEvent.MOCK;

        List<RuleEvent> ruleEvents = new ArrayList<>();
        ruleEvents.add( ruleEventOne );

        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder()
            .events( ruleEvents )
            .build();

        ruleEvents.add( ruleEventTwo );

        assertEquals( 1 , ruleEngine.events().size() );
        assertEquals( ruleEventOne , ruleEngine.events().get( 0 ) );

        ruleEngine.events().clear();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void builderShouldPropagateImmutableEmptyListIfNoEventsProvided()
    {
        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder().build();

        assertEquals( 0 , ruleEngine.events().size() );

        ruleEngine.events().clear();
    }

    @Test
    public void builderShouldPropagateRuleEngineContext()
    {
        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder().build();
        assertEquals( ruleEngineContext , ruleEngine.executionContext() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void evaluateShouldThrowOnNullEvent()
    {

        RuleEvent ruleEvent = null;
        ruleEngineContext.toEngineBuilder().build().evaluate( ruleEvent );
    }

    @Test( expected = IllegalStateException.class )
    public void evaluateShouldThrowIfEventIsAlreadyInContext()
    {
        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_programstage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(), "", null);

        List<RuleEvent> ruleEvents = new ArrayList<>();
        ruleEvents.add( ruleEvent );

        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder()
            .events( ruleEvents )
            .build();

        ruleEngine.evaluate( ruleEvent );
    }

    @Test( expected = IllegalArgumentException.class )
    public void evaluateShouldThrowOnNullEnrollment()
    {
        RuleEnrollment ruleEnrollment = null;
        ruleEngineContext.toEngineBuilder().build().evaluate( ruleEnrollment );
    }

    @Test
    public void concurrentIterationOverRulesListShouldNotFail()
        throws InterruptedException
    {
        final RuleEngine ruleEngine = RuleEngineContext.builder()
            .rules( List.of(Rule.MOCK, Rule.MOCK) )
            .build().toEngineBuilder().build();

        final CountDownLatch threadOneLatch = new CountDownLatch( 1 );
        final CountDownLatch threadTwoLatch = new CountDownLatch( 1 );

        new Thread()
        {
            @Override
            public void run()
            {
                for ( Rule rule : ruleEngine.executionContext().rules() )
                {

                    try
                    {
                        threadTwoLatch.await();
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }

                    threadOneLatch.countDown();
                }
            }
        }.start();

        new Thread()
        {
            @Override
            public void run()
            {
                for ( Rule rule : ruleEngine.executionContext().rules() )
                {
                    threadTwoLatch.countDown();

                    try
                    {
                        threadOneLatch.await();
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        assertTrue( threadOneLatch.await( 4, TimeUnit.SECONDS ) );
        assertTrue( threadTwoLatch.await( 4, TimeUnit.SECONDS ) );
    }
}
