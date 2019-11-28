package org.hisp.dhis.rules;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.ExprFunction;
import org.hisp.dhis.parser.expression.ExprItem;
import org.hisp.dhis.parser.expression.Parser;
import org.hisp.dhis.parser.expression.function.VectorAvg;
import org.hisp.dhis.parser.expression.function.VectorCount;
import org.hisp.dhis.parser.expression.function.VectorMax;
import org.hisp.dhis.parser.expression.function.VectorMin;
import org.hisp.dhis.parser.expression.function.VectorStddevSamp;
import org.hisp.dhis.parser.expression.function.VectorSum;
import org.hisp.dhis.parser.expression.function.VectorVariance;
import org.hisp.dhis.parser.expression.item.ItemConstant;
import org.hisp.dhis.rules.models.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hisp.dhis.parser.expression.ParserUtils.COMMON_EXPRESSION_FUNCTIONS;
import static org.hisp.dhis.parser.expression.ParserUtils.FUNCTION_EVALUATE;
import static org.hisp.dhis.parser.expression.ParserUtils.ITEM_EVALUATE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.AVG;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.COUNT;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.C_BRACE;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.MAX;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.MIN;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.STDDEV;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.SUM;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.VARIANCE;

class RuleEngineExecution
        implements Callable<List<RuleEffect>> {
    public final static ImmutableMap<Integer, ExprItem> PROGRAM_INDICATOR_ITEMS = ImmutableMap.<Integer, ExprItem>builder()

        .put( C_BRACE, new ItemConstant() )

        .build();

    public final static ImmutableMap<Integer, ExprFunction> FUNCTIONS = ImmutableMap.<Integer, ExprFunction>builder()

        // Common functions

        .putAll( COMMON_EXPRESSION_FUNCTIONS )

        // Program functions for custom aggregation

        .put( AVG, new VectorAvg() )
        .put( COUNT, new VectorCount() )
        .put( MAX, new VectorMax() )
        .put( MIN, new VectorMin() )
        .put( STDDEV, new VectorStddevSamp() )
        .put( SUM, new VectorSum() )
        .put( VARIANCE, new VectorVariance() )

        .build();

    private static final String TODAY = new Date().toString();

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

        Collections.sort(ruleList, new Comparator<Rule>()
        {
            @Override
            public int compare( Rule rule1, Rule rule2 )
            {
                Integer priority1 = rule1.priority();
                Integer priority2 = rule2.priority();
                if ( priority1 != null && priority2 != null )
                    return priority1.compareTo( priority2 );
                else if ( priority1 != null )
                    return -1;
                else if ( priority2 != null )
                    return 1;
                else
                    return 0;
            }
        } );
        for (int i = 0; i < ruleList.size(); i++) {
            Rule rule = ruleList.get(i);
            try {
                log.debug("Evaluating programrule: " + rule.name());
                // send org.hisp.dhis.parser.expression to evaluator
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
                e.printStackTrace();
                log.error("Exception in " + rule.name() + ": " + e.getMessage());
            }
        }
        return ruleEffects;
    }

    private String process( String condition )
    {
        CommonExpressionVisitor commonExpressionVisitor = CommonExpressionVisitor.newBuilder()
            .withFunctionMap( FUNCTIONS )
            .withItemMap( PROGRAM_INDICATOR_ITEMS )
            .withVariablesMap(valueMap)
            .withFunctionMethod( FUNCTION_EVALUATE )
            .withItemMethod( ITEM_EVALUATE )
            .withSupplementaryData( supplementaryData )
            .validateCommonProperties();

        return Parser.visit( condition, commonExpressionVisitor ).toString();
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
            RuleVariableValue variableValue = RuleVariableValue.create(data, RuleValueType.TEXT, Arrays.asList(data),
                TODAY );
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
}
