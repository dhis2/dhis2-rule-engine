package org.hisp.dhis.rules;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.antlr.Parser;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.utils.RuleEngineUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.Callable;

import static org.hisp.dhis.rules.parser.expression.ParserUtils.FUNCTION_EVALUATE;

class RuleConditionEvaluator
{
    private static final Log log = LogFactory.getLog( RuleConditionEvaluator.class );

    public List<RuleEffect> getRuleEffects( Map<String, RuleVariableValue> valueMap,
        Map<String, List<String>> supplementaryData, List<Rule> rules )
    {
        List<RuleEffect> ruleEffects = new ArrayList<>();

        rules = orderRules( rules );
        valueMap = new HashMap<>( valueMap );

        for ( Rule rule : orderRules( rules ) )
        {
            log.debug( "Evaluating programrule: " + rule.name() );

            if ( Boolean.valueOf( process( rule.condition(), valueMap, supplementaryData ) ) )
            {
                for ( RuleAction action : rule.actions() )
                {

                    //Check if action is assigning value to calculated variable
                    if ( isAssignToCalculatedValue( action ) )
                    {
                        RuleActionAssign ruleActionAssign = (RuleActionAssign) action;
                        updateValueMap(
                            Utils.unwrapVariableName( ruleActionAssign.content() ),
                            RuleVariableValue.create( process( ruleActionAssign.data(), valueMap, supplementaryData ),
                                RuleValueType.TEXT ),
                            valueMap
                        );
                    }
                    else
                    {
                        ruleEffects.add( create( rule, action, valueMap, supplementaryData ) );
                    }
                }
            }
        }

        return ruleEffects;

    }

    private List<Rule> orderRules( List<Rule> rules )
    {
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

        return ruleList;
    }

    private String process( String condition, Map<String, RuleVariableValue> valueMap,
        Map<String, List<String>> supplementaryData )
    {
        if ( condition.isEmpty() )
        {
            return "";
        }
        try
        {
            CommonExpressionVisitor commonExpressionVisitor = CommonExpressionVisitor.newBuilder()
                .withFunctionMap( RuleEngineUtils.FUNCTIONS )
                .withFunctionMethod( FUNCTION_EVALUATE )
                .withVariablesMap( valueMap )
                .withSupplementaryData( supplementaryData )
                .validateCommonProperties();

            Object result = Parser.visit( condition, commonExpressionVisitor, !isOldAndroidVersion( valueMap, supplementaryData ) );
            return convertInteger( result ).toString();
        }
        catch ( ParserExceptionWithoutContext e )
        {
            log.warn( "Condition " + condition + " not executed: " + e.getMessage() );
            return "";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            log.error( "Unexpected exception while evaluating " + condition + ": " + e.getMessage() );
            return "";
        }
    }

    private Object convertInteger( Object result )
    {
        if ( result instanceof Double && (Double) result % 1 == 0 )
        {
            return ((Double) result).intValue();
        }
        return result;
    }

    private Boolean isOldAndroidVersion( Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        return valueMap.containsKey( "environment" ) &&
            Objects.equals( valueMap.get( "environment" ).value(), TriggerEnvironment.ANDROIDCLIENT.getClientName() ) &&
            supplementaryData.containsKey( "android_version" ) &&
            Integer.parseInt( supplementaryData.get( "android_version" ).get( 0 ) ) < 21;
    }

    private Boolean isAssignToCalculatedValue( RuleAction ruleAction )
    {
        return ruleAction instanceof RuleActionAssign && ((RuleActionAssign) ruleAction).field().isEmpty();
    }

    private void updateValueMap( String variable, RuleVariableValue value, Map<String, RuleVariableValue> valueMap )
    {
        valueMap.put( variable, value );
    }

    @Nonnull
    private RuleEffect create( @Nonnull Rule rule, @Nonnull RuleAction ruleAction,
        Map<String, RuleVariableValue> valueMap,
        Map<String, List<String>> supplementaryData )
    {
        if ( ruleAction instanceof RuleActionAssign )
        {
            RuleActionAssign ruleActionAssign = (RuleActionAssign) ruleAction;
            String data = process( ruleActionAssign.data(), valueMap, supplementaryData );
            updateValueMap( ruleActionAssign.field(), RuleVariableValue.create( data, RuleValueType.TEXT ), valueMap );
            return RuleEffect
                .create( rule.uid(), ruleAction, StringUtils.isEmpty( data ) ? ruleActionAssign.data() : data );
        }

        return RuleEffect.create( rule.uid(), ruleAction, process( ruleAction.data(), valueMap, supplementaryData ) );
    }
}
