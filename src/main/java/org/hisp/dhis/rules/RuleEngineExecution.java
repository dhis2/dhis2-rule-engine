package org.hisp.dhis.rules;

import org.apache.commons.jexl2.JexlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.rules.functions.RuleFunction;
import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionAssign;
import org.hisp.dhis.rules.models.RuleActionCreateEvent;
import org.hisp.dhis.rules.models.RuleActionDisplayKeyValuePair;
import org.hisp.dhis.rules.models.RuleActionDisplayText;
import org.hisp.dhis.rules.models.RuleActionErrorOnCompletion;
import org.hisp.dhis.rules.models.RuleActionScheduleMessage;
import org.hisp.dhis.rules.models.RuleActionSendMessage;
import org.hisp.dhis.rules.models.RuleActionShowError;
import org.hisp.dhis.rules.models.RuleActionShowWarning;
import org.hisp.dhis.rules.models.RuleActionWarningOnCompletion;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

class RuleEngineExecution
        implements Callable<List<RuleEffect>> {
    private static final String D2_FUNCTION_PREFIX = "d2:";

    private static final Log log = LogFactory.getLog(RuleEngineExecution.class);

    private static final String REGEX = "[a-zA-Z0-9]+(?:[\\w -]*[a-zA-Z0-9]+)*";

    private static final Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

    @Nonnull
    private final RuleExpressionEvaluator expressionEvaluator;

    @Nonnull
    private Map<String, RuleVariableValue> valueMap;

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    RuleEngineExecution(@Nonnull RuleExpressionEvaluator expressionEvaluator,
                        @Nonnull List<Rule> rules, @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData) {
        this.expressionEvaluator = expressionEvaluator;
        this.valueMap = new HashMap<>(valueMap);
        this.rules = rules;
        this.supplementaryData = supplementaryData;
    }

    @Override
    public List<RuleEffect> call() {
        List<RuleEffect> ruleEffects = new ArrayList<>();

        List<Rule> ruleList = new ArrayList<>(rules);

        Collections.sort(ruleList, (rule1, rule2) -> {
            Integer priority1 = rule1.priority();
            Integer priority2 = rule2.priority();
            if (priority1 != null && priority2 != null)
                return priority1.compareTo(priority2);
            else if (priority1 != null)
                return -1;
            else if (priority2 != null)
                return 1;
            else
                return 0;
        });
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < ruleList.size(); i++) {
            Rule rule = ruleList.get(i);
            try {
                log.debug("Evaluating programrule: " + rule.name());
                // send expression to evaluator
                if (Boolean.valueOf(process(rule.condition()))) {
                    // process each action for this rule
                    for (int j = 0; j < rule.actions().size(); j++) {
                        RuleEffect ruleEffect = create(rule.actions().get(j));
                        //Check if action is assigning value to calculated variable
                        if (isAssignToCalculatedValue(rule.actions().get(j)))
                            updateValueMapForCalculatedValue((RuleActionAssign) rule.actions().get(j),
                                    RuleVariableValue.create(ruleEffect.data(), RuleValueType.TEXT));
                        else
                            ruleEffects.add(create(rule.actions().get(j)));
                    }
                }
            } catch (JexlException jexlException) {
                log.error("Parser exception in " + rule.name() + ": " + jexlException.getMessage());
            } catch (Exception e) {
                log.error("Exception in " + rule.name() + ": " + e.getMessage());
            }
        }
        log.debug("Rules took " + String.valueOf(System.currentTimeMillis() - currentTime));
        log.info("Rules took " + String.valueOf(System.currentTimeMillis() - currentTime));
        log.error(ruleList.size() + " rules took " + String.valueOf(System.currentTimeMillis() - currentTime));
        return ruleEffects;
    }

    private Boolean isAssignToCalculatedValue(RuleAction ruleAction) {
        return ruleAction instanceof RuleActionAssign && ((RuleActionAssign) ruleAction).field().isEmpty();
    }

    private void updateValueMapForCalculatedValue(RuleActionAssign ruleActionAssign, RuleVariableValue value) {
        valueMap.put(RuleExpression.unwrapVariableName(ruleActionAssign.content()),
                value);
    }

    @Nonnull
    private RuleEffect create(@Nonnull RuleAction ruleAction) {
        // Only certain types of actions might
        // contain code to execute.
        if (ruleAction instanceof RuleActionAssign) {
            String data = process(((RuleActionAssign) ruleAction).data());
            RuleVariableValue variableValue = RuleVariableValue.create(data, RuleValueType.TEXT, Arrays.asList(data), new Date().toString());
            String field = ((RuleActionAssign) ruleAction).field();
            Matcher matcher = pattern.matcher(field);
            while (matcher.find()) {
                field = matcher.group(0).trim();
                valueMap.put(field, variableValue);
            }

            return RuleEffect.create(ruleAction, data);
        } else if (ruleAction instanceof RuleActionSendMessage) {
            return RuleEffect.create(ruleAction, process(
                    ((RuleActionSendMessage) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionScheduleMessage) {
            return RuleEffect.create(ruleAction, process(
                    ((RuleActionScheduleMessage) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionCreateEvent) {
            return RuleEffect.create(ruleAction, process(
                    ((RuleActionCreateEvent) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionDisplayKeyValuePair) {
            return RuleEffect.create(ruleAction, process(
                    ((RuleActionDisplayKeyValuePair) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionDisplayText) {
            return RuleEffect.create(ruleAction, process(
                    ((RuleActionDisplayText) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionErrorOnCompletion) {
            return RuleEffect.create(ruleAction, process(
                    ((RuleActionErrorOnCompletion) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionShowError) {
            return RuleEffect.create(ruleAction,
                    process(((RuleActionShowError) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionShowWarning) {
            return RuleEffect.create(ruleAction,
                    process(((RuleActionShowWarning) ruleAction).data()));
        } else if (ruleAction instanceof RuleActionWarningOnCompletion) {
            return RuleEffect.create(ruleAction,
                    process(((RuleActionWarningOnCompletion) ruleAction).data()));
        }

        return RuleEffect.create(ruleAction);
    }

    @Nonnull
    private String process(@Nonnull String expression) {
        // we don't want to run empty expression
        if (!expression.trim().isEmpty()) {
            String expressionWithVariableValues = bindVariableValues(expression);
            String expressionWithFunctionValues = bindFunctionValues(expressionWithVariableValues);

            log.debug("Evaluating expression: " + expressionWithFunctionValues);
            return expressionEvaluator.evaluate(expressionWithFunctionValues);
        }

        return "";
    }

    @Nonnull
    private String bindVariableValues(@Nonnull String expression) {
        RuleExpression ruleExpression = RuleExpression.from(expression);
        RuleExpressionBinder ruleExpressionBinder = RuleExpressionBinder.from(ruleExpression);

        // substitute variable values
        for (String variable : ruleExpression.variables()) {
            RuleVariableValue variableValue = valueMap.get(
                    RuleExpression.unwrapVariableName(variable));

            if (variableValue != null) {
                ruleExpressionBinder.bindVariable(variable, variableValue.value() == null ?
                        variableValue.type().defaultValue() : variableValue.value());
            }
        }

        return ruleExpressionBinder.build();
    }

    @Nonnull
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private String bindFunctionValues(@Nonnull String expression) {

        RuleExpression ruleExpression = RuleExpression.from(expression);
        RuleExpressionBinder ruleExpressionBinder = RuleExpressionBinder.from(ruleExpression);

        for (String function : ruleExpression.functions()) {
            RuleFunctionCall ruleFunctionCall = RuleFunctionCall.from(function);

            List<String> arguments = new ArrayList<>(ruleFunctionCall.arguments());
            for (int j = 0; j < arguments.size(); j++) {
                arguments.set(j, process(arguments.get(j)));
            }

            ruleExpressionBinder.bindFunction(ruleFunctionCall.functionCall(), RuleFunction
                    .create(ruleFunctionCall.functionName()).evaluate(arguments, valueMap, supplementaryData));
        }

        String processedExpression = ruleExpressionBinder.build();

        // In case if there are functions which
        // are not processed completely.
        if (processedExpression.contains(D2_FUNCTION_PREFIX)) {
            Matcher functionMatcher = RuleExpression.FUNCTION_PATTERN_COMPILED.matcher(processedExpression);

            if (functionMatcher.find()) {
                // Another recursive call to process rest of
                // the d2 function calls.
                processedExpression = bindFunctionValues(processedExpression);
            }
        }

        return processedExpression;
    }
}
