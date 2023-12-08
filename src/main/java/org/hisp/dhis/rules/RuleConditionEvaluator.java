package org.hisp.dhis.rules;

import org.hisp.dhis.lib.expression.Expression;
import org.hisp.dhis.lib.expression.spi.ExpressionData;
import org.hisp.dhis.lib.expression.spi.IllegalExpressionException;
import org.hisp.dhis.lib.expression.spi.VariableValue;
import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionAssign;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEvaluationResult;
import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.TrackerObjectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.hisp.dhis.rules.UtilsKt.unwrapVariableName;

public class RuleConditionEvaluator
{
    private static final Logger log = Logger.getLogger( RuleConditionEvaluator.class.getName() );

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

            if ( !ruleEvaluationResult.error() ) {
                ruleEffects.addAll( ruleEvaluationResult.ruleEffects() );
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
            log.fine( "Evaluating programrule: " + rule.name() );

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
                                        unwrapVariableName(ruleActionAssign.getContent()),
                                        new RuleVariableValue(RuleValueType.TEXT, process( ruleActionAssign.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION), List.of(), null),
                                        valueMap
                                );
                            }
                            else
                            {
                                ruleEffects.add( create( rule, action, valueMap, supplementaryData ) );
                            }
                        } catch ( Exception e ) {
                            addRuleErrorResult( rule, action, e, targetType, targetUid, ruleEvaluationResults );
                        }
                    }

                    ruleEvaluationResults.add(RuleEvaluationResult.Companion.evaluatedResult(rule, ruleEffects));
                } else {
                    ruleEvaluationResults.add(RuleEvaluationResult.Companion.notEvaluatedResult(rule));
                }
            } catch ( Exception e ) {
                addRuleErrorResult(rule, null, e, targetType, targetUid, ruleEvaluationResults);
            }
        }

        for (RuleEvaluationResult ruleEvaluationResult : ruleEvaluationResults) {

            log.fine("Rule " + ruleEvaluationResult.rule().name() + " with id " + ruleEvaluationResult.rule().uid() +
                    " executed for " + targetType.name() + "(" + targetUid + ")" +
                    " with condition (" + ruleEvaluationResult.rule().condition() + ")" +
                    " was evaluated " + ruleEvaluationResult.evaluatedAs());
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
                    " executed for " + targetType.name() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an error: " + e.getMessage();
        }
        else if ( ruleAction != null )
        {
            errorMessage = "Action " + ruleAction.getClass().getName() +
                    " from rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an unexpected exception: " + e.getMessage();
        }
        else if(e instanceof IllegalExpressionException)
        {
            errorMessage = "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an error: " + e.getMessage();
        }
        else
        {
            errorMessage = "Rule " + rule.name() + " with id " + rule.uid() +
                    " executed for " + targetType.name() + "(" + targetUid + ")" +
                    " with condition (" + rule.condition() + ")" +
                    " raised an unexpected exception: " + e.getMessage();
        }

        log.log(Level.SEVERE, errorMessage);
        ruleEvaluationResults.add(RuleEvaluationResult.Companion.errorRule(rule, errorMessage));
    }

    private List<Rule> orderRules( List<Rule> rules )
    {
        List<Rule> ruleList = new ArrayList<>( rules );

        ruleList.sort((rule1, rule2) -> {
            Integer priority1 = rule1.priority();
            Integer priority2 = rule2.priority();
            if (priority1 != null && priority2 != null) {
                return priority1.compareTo(priority2);
            } else if (priority1 != null) {
                return -1;
            } else if (priority2 != null) {
                return 1;
            } else {
                return 0;
            }
        });

        return ruleList;
    }

    private String process(String condition, Map<String, RuleVariableValue> valueMap,
                           Map<String, List<String>> supplementaryData, Expression.Mode mode)
    {
        if ( condition.isEmpty() )
        {
            return "";
        }

        Expression expression = new Expression(condition, mode, false);
        Map<String, VariableValue> programRuleVariableValues = valueMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toVariableValue()));
        ExpressionData build = new ExpressionData(programRuleVariableValues, Map.of(), supplementaryData, Map.of(), Map.of());
        return convertInteger( expression.evaluate(name -> {
            throw new UnsupportedOperationException("function not supported: "+name);
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
        return ruleAction instanceof RuleActionAssign && ((RuleActionAssign) ruleAction).getField().isEmpty();
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
        if (ruleAction instanceof RuleActionAssign ruleActionAssign)
        {
            String data = process( ruleActionAssign.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION);
            updateValueMap( ruleActionAssign.getField(), new RuleVariableValue( RuleValueType.TEXT, data, List.of(), null ), valueMap );
            if ( data == null || data.isEmpty() )
            {
                return new RuleEffect( rule.uid(), ruleAction, null );
            }
            else
            {
                return new RuleEffect( rule.uid(), ruleAction, data );
            }
        }

        return new RuleEffect( rule.uid(), ruleAction, process( ruleAction.data(), valueMap, supplementaryData, Expression.Mode.RULE_ENGINE_ACTION) );
    }
}
