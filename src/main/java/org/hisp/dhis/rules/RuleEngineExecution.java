package org.hisp.dhis.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.antlr.Parser;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.utils.RuleEngineUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.Callable;

import static org.hisp.dhis.rules.parser.expression.ParserUtils.FUNCTION_EVALUATE;

class RuleEngineExecution
    implements Callable<List<RuleEffect>>
{
    private static final Log log = LogFactory.getLog( RuleEngineExecution.class );

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    @Nonnull
    private Map<String, RuleVariableValue> valueMap;

    RuleEngineExecution( @Nonnull List<Rule> rules,
        @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        this.valueMap = new HashMap<>( valueMap );
        this.rules = rules;
        this.supplementaryData = supplementaryData;
    }

    @Override
    public List<RuleEffect> call()
    {
        List<RuleEffect> ruleEffects = new ArrayList<>();

        List<Rule> ruleList = new ArrayList<>( rules );

        Collections.sort( ruleList, new Comparator<Rule>()
        {
            @Override
            public int compare( Rule rule1, Rule rule2 )
            {
                Integer priority1 = rule1.priority();
                Integer priority2 = rule2.priority();
                if ( priority1 != null && priority2 != null )
                {
                    return priority1.compareTo( priority2 );
                }
                else if ( priority1 != null )
                {
                    return -1;
                }
                else if ( priority2 != null )
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        } );

        for ( Rule rule : ruleList )
        {
            log.debug( "Evaluating programrule: " + rule.name() );

            try
            {
                if ( Boolean.valueOf( process( rule.condition() ) ) )
                {
                    for ( RuleAction action : rule.actions() )
                    {

                        //Check if action is assigning value to calculated variable
                        if ( isAssignToCalculatedValue( action ) )
                        {
                            RuleActionAssign ruleActionAssign = (RuleActionAssign) action;
                            updateValueMap( ruleActionAssign.content(), RuleVariableValue.create( process( ruleActionAssign.data() ), RuleValueType.TEXT ) );
                        }
                        else
                        {
                            ruleEffects.add( create( action ) );
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                log.error( "Exception in " + rule.name() + ": " + e.getMessage() );
            }
        }

        return ruleEffects;
    }

    private String process( String condition )
    {
        if ( condition.isEmpty() )
        {
            return "";
        }

        CommonExpressionVisitor commonExpressionVisitor = CommonExpressionVisitor.newBuilder()
            .withFunctionMap( RuleEngineUtils.FUNCTIONS )
            .withFunctionMethod( FUNCTION_EVALUATE )
            .withVariablesMap( valueMap )
            .withSupplementaryData( supplementaryData )
            .validateCommonProperties();

        return Parser.visit( condition, commonExpressionVisitor, !isOldAndroidVersion() ).toString();
    }

    private Boolean isOldAndroidVersion()
    {
        return valueMap.containsKey( "environment" ) &&
                Objects.equals( valueMap.get( "environment" ).value(), TriggerEnvironment.ANDROIDCLIENT.getClientName() ) &&
                supplementaryData.containsKey( "android_version" ) &&
                Integer.parseInt( supplementaryData.get( "android_version" ).get( 0 ) ) < 21;
    }

    private Boolean isAssignToCalculatedValue( RuleAction ruleAction )
    {
        return ruleAction instanceof RuleActionAssign && ((RuleActionAssign)ruleAction).field().isEmpty() ;
    }

    private void updateValueMap( String variable, RuleVariableValue value )
    {
        valueMap.put( variable, value );
    }

    @Nonnull
    private RuleEffect create( @Nonnull RuleAction ruleAction )
    {
        if ( ruleAction instanceof RuleActionAssign )
        {
            RuleActionAssign ruleActionAssign = (RuleActionAssign) ruleAction;
            String data = process( ruleActionAssign.data() );
            updateValueMap( ruleActionAssign.field(), RuleVariableValue.create( data, RuleValueType.TEXT ) );
            return RuleEffect.create( ruleAction, data );
        }

        return RuleEffect.create( ruleAction, process( ruleAction.data() ) );
    }
}
