package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.RuleVariable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class RuleEngineTests
{
    private RuleEngineContext ruleEngineContext;

    @Before
    public void setUp()
    {
        ruleEngineContext = RuleEngineContext.builder()
            .ruleVariables( Arrays.asList( mock( RuleVariable.class ) ) )
            .rules( Arrays.asList( mock( Rule.class ) ) )
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
        RuleEvent ruleEventOne = mock( RuleEvent.class );
        RuleEvent ruleEventTwo = mock( RuleEvent.class );

        List<RuleEvent> ruleEvents = new ArrayList<>();
        ruleEvents.add( ruleEventOne );

        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder()
            .events( ruleEvents )
            .build();

        ruleEvents.add( ruleEventTwo );

        assertThat( ruleEngine.events().size() ).isEqualTo( 1 );
        assertThat( ruleEngine.events().get( 0 ) ).isEqualTo( ruleEventOne );

        ruleEngine.events().clear();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void builderShouldPropagateImmutableEmptyListIfNoEventsProvided()
    {
        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder().build();

        assertThat( ruleEngine.events().size() ).isEqualTo( 0 );

        ruleEngine.events().clear();
    }

    @Test
    public void builderShouldPropagateRuleEngineContext()
    {
        RuleEngine ruleEngine = ruleEngineContext.toEngineBuilder().build();
        assertThat( ruleEngine.executionContext() ).isEqualTo( ruleEngineContext );
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
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, new ArrayList<RuleDataValue>(), "" );

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
        final RuleEngine ruleEngine = RuleEngineContext.builder( mock( RuleExpressionEvaluator.class ) )
            .rules( Arrays.asList( mock( Rule.class ), mock( Rule.class ) ) )
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

        assertThat( threadOneLatch.await( 4, TimeUnit.SECONDS ) ).isTrue();
        assertThat( threadTwoLatch.await( 4, TimeUnit.SECONDS ) ).isTrue();
    }
}
