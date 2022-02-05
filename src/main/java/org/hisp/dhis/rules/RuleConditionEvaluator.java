package org.hisp.dhis.rules;

import org.apache.commons.lang3.StringUtils;
import org.hisp.dhis.antlr.Parser;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.utils.RuleEngineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

import static org.hisp.dhis.rules.parser.expression.ParserUtils.FUNCTION_EVALUATE;

public class RuleConditionEvaluator
{
    private static final Logger log = LoggerFactory.getLogger( RuleConditionEvaluator.class.getName() );

    public List<RuleEffect> getRuleEffects( TrackerObjectType targetType, String targetUid, Map<String, RuleVariableValue> valueMap,
                                            Map<String, List<String>> supplementaryData, List<Rule> rules )
    {
        List<RuleEffect> ruleEffects = new ArrayList<>();
        List<RuleEvaluationResult> ruleEvaluationResults = getRuleEvaluationResults( targetType, targetUid, valueMap, supplementaryData, rules);
        for (RuleEvaluationResult ruleEvaluationResult : ruleEvaluationResults) {

            log.info( "Rule " + ruleEvaluationResult.getRule().name() + " with id " + ruleEvaluationResult.getRule().uid() +
                        " executed for " + targetType.getName() +  "(" + targetUid +")" +
                        " with condition (" + ruleEvaluationResult.getRule().condition() +  ")" +
                        " was evaluated " + ruleEvaluationResult.isEvaluatedAs() );

            if (ruleEvaluationResult.isEvaluatedAs() ) {
                ruleEffects.addAll( ruleEvaluationResult.getRuleEffects() );
            }
        }

        return ruleEffects;
    }

    public List<RuleEvaluationResult> getRuleEvaluationResults( TrackerObjectType targetType, String targetUid,
                                                    Map<String, RuleVariableValue> valueMap,
                                                    Map<String, List<String>> supplementaryData, List<Rule> rules )
    {
        List<RuleEvaluationResult> ruleEvaluationResults = new ArrayList<>();

        rules = orderRules( rules );
        valueMap = new HashMap<>( valueMap );

        for ( Rule rule : orderRules( rules ) )
        {
            log.debug( "Evaluating programrule: " + rule.name() );

            List<RuleEffect> ruleEffects = new ArrayList<>();

            if ( Boolean.valueOf( process( targetType, targetUid, rule, rule.condition(), valueMap, supplementaryData ) ) )
            {
                for ( RuleAction action : rule.actions() )
                {

                    //Check if action is assigning value to calculated variable
                    if ( isAssignToCalculatedValue( action ) )
                    {
                        RuleActionAssign ruleActionAssign = (RuleActionAssign) action;
                        updateValueMap(
                            Utils.unwrapVariableName( ruleActionAssign.content() ),
                            RuleVariableValue.create( process( targetType, targetUid, rule, ruleActionAssign.data(), valueMap, supplementaryData ),
                                RuleValueType.TEXT ),
                            valueMap
                        );
                    }
                    else
                    {
                        ruleEffects.add( create( targetType, targetUid, rule, action, valueMap, supplementaryData ) );
                    }
                }

                ruleEvaluationResults.add( RuleEvaluationResult.evaluatedResult( rule, ruleEffects ) );
            } else {
                ruleEvaluationResults.add( RuleEvaluationResult.notEvaluatedResult( rule ) );
            }
        }

        return ruleEvaluationResults;

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

    private String process( TrackerObjectType targetType, String targetUid, Rule rule, String condition,
                            Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
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
            log.warn( "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.getName() +  "(" + targetUid +")" +
                    " with condition (" + condition +  ")" +
                    " raised an error: " + e.getMessage() );
            return "";
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            log.error( "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.getName() +  "(" + targetUid +")" +
                    " with condition (" + condition +  ")" +
                    " raised an unexpected exception: " + e.getMessage() );
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
    private RuleEffect create( TrackerObjectType targetType, String targetUid, @Nonnull Rule rule,
                                @Nonnull RuleAction ruleAction,
                                Map<String, RuleVariableValue> valueMap,
                                Map<String, List<String>> supplementaryData )
    {
        if ( ruleAction instanceof RuleActionAssign )
        {
            RuleActionAssign ruleActionAssign = (RuleActionAssign) ruleAction;
            String data = process( targetType, targetUid, rule, ruleActionAssign.data(), valueMap, supplementaryData );
            updateValueMap( ruleActionAssign.field(), RuleVariableValue.create( data, RuleValueType.TEXT ), valueMap );
            if ( StringUtils.isEmpty( data ) && StringUtils.isEmpty( ruleActionAssign.data() ) )
            {
                return RuleEffect.create( rule.uid(), ruleAction, ruleActionAssign.data() );
            }
            else
            {
                return RuleEffect.create( rule.uid(), ruleAction, data );
            }
        }

        return RuleEffect.create( rule.uid(), ruleAction, process( targetType, targetUid, rule, ruleAction.data(),
                valueMap, supplementaryData ) );
    }
}
