package org.hisp.dhis.rules;

import org.apache.commons.lang3.StringUtils;
import org.hisp.dhis.lib.expression.Expression;
import org.hisp.dhis.lib.expression.spi.ExpressionData;
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException;
import org.hisp.dhis.rules.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

public class RuleConditionEvaluator
{
    private static final Logger log = LoggerFactory.getLogger( RuleConditionEvaluator.class.getName() );

    public List<RuleEffect> getEvaluatedAndErrorRuleEffects( TrackerObjectType targetType, String targetUid, Map<String, RuleVariableValue> valueMap,
                                            Map<String, List<String>> supplementaryData, List<Rule> rules )
    {
        List<RuleEffect> ruleEffects = new ArrayList<>();
        for (RuleEvaluationResult ruleEvaluationResult : getRuleEvaluationResults( targetType, targetUid, valueMap, supplementaryData, rules)) {
                ruleEffects.addAll( ruleEvaluationResult.getRuleEffects() );
        }

        return ruleEffects;
    }

    public List<RuleEffect> getRuleEffects( TrackerObjectType targetType, String targetUid, Map<String, RuleVariableValue> valueMap,
                                            Map<String, List<String>> supplementaryData, List<Rule> rules )
    {
        List<RuleEffect> ruleEffects = new ArrayList<>();
        for (RuleEvaluationResult ruleEvaluationResult : getRuleEvaluationResults( targetType, targetUid, valueMap, supplementaryData, rules)) {

            if ( !ruleEvaluationResult.isError() ) {
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

            try {
                List<RuleEffect> ruleEffects = new ArrayList<>();

            if ( Boolean.parseBoolean( process( rule.condition(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_CONDITION) ) )
            {
                for ( RuleAction action : rule.actions() )
                {

                        try {
                            //Check if action is assigning value to calculated variable
                            if ( isAssignToCalculatedValue( action ) )
                            {
                                RuleActionAssign ruleActionAssign = (RuleActionAssign) action;
                                updateValueMap(
                                        Utils.unwrapVariableName(ruleActionAssign.content()),
                                        RuleVariableValue.create(process( ruleActionAssign.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION),
                                                RuleValueType.TEXT),
                                        valueMap
                                );
                            }
                            else
                            {
                                ruleEffects.add( create( rule, action, valueMap, supplementaryData ) );
                            }
                        } catch ( Exception e ) {
                            addRuleErrorResult( rule,action, e, targetType, targetUid, ruleEvaluationResults );
                        }
                    }

                    ruleEvaluationResults.add(RuleEvaluationResult.evaluatedResult(rule, ruleEffects));
                } else {
                    ruleEvaluationResults.add(RuleEvaluationResult.notEvaluatedResult(rule));
                }
            } catch ( Exception e ) {
                addRuleErrorResult(rule, null, e, targetType, targetUid, ruleEvaluationResults);
            }
        }

        for (RuleEvaluationResult ruleEvaluationResult : ruleEvaluationResults) {

            log.debug("Rule " + ruleEvaluationResult.getRule().name() + " with id " + ruleEvaluationResult.getRule().uid() +
                    " executed for " + targetType.getName() + "(" + targetUid + ")" +
                    " with condition (" + ruleEvaluationResult.getRule().condition() + ")" +
                    " was evaluated " + ruleEvaluationResult.isEvaluatedAs());
        }

        return ruleEvaluationResults;

    }

    private void addRuleErrorResult( Rule rule, RuleAction ruleAction, Exception e, TrackerObjectType targetType,
                                    String targetUid, List<RuleEvaluationResult> ruleEvaluationResults )
    {
        String errorMessage;
        if ( ruleAction != null && e instanceof IllegalExpressionException)
        {
            errorMessage = "Action " + ruleAction.getClass().getName() +
                    " from rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.getName() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an error: " + e.getMessage();
        }
        else if ( ruleAction != null )
        {
            errorMessage = "Action " + ruleAction.getClass().getName() +
                    " from rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.getName() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an unexpected exception: " + e.getMessage();
        }
        else if(e instanceof IllegalExpressionException)
        {
            errorMessage = "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.getName() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an error: " + e.getMessage();
        }
        else
        {
            errorMessage = "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.getName() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an unexpected exception: " + e.getMessage();
        }

        log.error(errorMessage);
        ruleEvaluationResults.add(RuleEvaluationResult.errorRule(rule, errorMessage));
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

    private String process(String condition, Map<String, RuleVariableValue> valueMap,
                           Map<String, List<String>> supplementaryData, Expression.Mode mode)
    {
        if ( condition.isEmpty() )
        {
            return "";
        }

        Expression expression = new Expression(condition, mode);

        ExpressionData build = ExpressionData.builder()
                .supplementaryValues(supplementaryData)
                .programRuleVariableValues(valueMap)
                .build();
        return convertInteger( expression.evaluate(name -> {
            throw new UnsupportedOperationException(name);
        }, build) ).toString();
    }

    private Object convertInteger( Object result )
    {
        if ( result instanceof Double && (Double) result % 1 == 0 )
        {
            return ((Double) result).intValue();
        }
        return result;
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
    private RuleEffect create( @Nonnull Rule rule,
                                @Nonnull RuleAction ruleAction,
                                Map<String, RuleVariableValue> valueMap,
                                Map<String, List<String>> supplementaryData )
    {
        if ( ruleAction instanceof RuleActionAssign )
        {
            RuleActionAssign ruleActionAssign = (RuleActionAssign) ruleAction;
            String data = process( ruleActionAssign.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION);
            updateValueMap( ruleActionAssign.field(), RuleVariableValue.create( data, RuleValueType.TEXT ), valueMap );
            if ( StringUtils.isEmpty( data ) )
            {
                return RuleEffect.create( rule.uid(), ruleAction, null );
            }
            else
            {
                return RuleEffect.create( rule.uid(), ruleAction, data );
            }
        }

        return RuleEffect.create( rule.uid(), ruleAction, process( ruleAction.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION) );
    }
}
