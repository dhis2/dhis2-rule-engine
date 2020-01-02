package org.hisp.dhis.rules;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.ExprFunction;
import org.hisp.dhis.parser.expression.function.SimpleNoSqlFunction;
import org.hisp.dhis.parser.expression.Parser;
import org.hisp.dhis.rules.functions.*;
import org.hisp.dhis.rules.models.*;
import org.hisp.dhis.rules.variables.ProgramStageNameVariable;

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
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.*;

class RuleEngineExecution
        implements Callable<List<RuleEffect>> {
    public final static ImmutableMap<Integer, ExprFunction> FUNCTIONS = ImmutableMap.<Integer, ExprFunction>builder()

        .put( D2_CEIL, new RuleFunctionCeil() )
        .put( D2_ADD_DAYS, new RuleFunctionAddDays() )
        .put( D2_CONCATENATE, new RuleFunctionConcatenate() )
        .put( D2_FLOOR, new RuleFunctionFloor() )
        .put( D2_SUBSTRING, new RuleFunctionSubString() )
        .put( D2_LENGTH, new RuleFunctionLength() )
        .put( D2_LEFT, new RuleFunctionLeft() )
        .put( D2_RIGHT, new RuleFunctionRight() )
        .put( D2_MODULUS, new RuleFunctionModulus() )
        .put( D2_ROUND, new RuleFunctionRound() )
        .put( D2_YEARS_BETWEEN, new RuleFunctionYearsBetween() )
        .put( D2_MONTHS_BETWEEN, new RuleFunctionMonthsBetween() )
        .put( D2_WEEKS_BETWEEN, new RuleFunctionWeeksBetween() )
        .put( D2_DAYS_BETWEEN, new RuleFunctionDaysBetween() )
        .put( D2_ZSCOREWFH, new RuleFunctionZScoreWFH() )
        .put( D2_ZSCOREWFA, new RuleFunctionZScoreWFA() )
        .put( D2_ZSCOREHFA, new RuleFunctionZScoreHFA() )
        .put( D2_SPLIT, new RuleFunctionSplit() )
        .put( D2_OIZP, new RuleFunctionOizp() )
        .put( D2_ZING, new RuleFunctionZing() )
        .put( D2_ZPVC, new RuleFunctionZpvc() )
        .put( D2_VALIDATE_PATTERN, new RuleFunctionValidatePattern() )
        .put( D2_MAX_VALUE, new RuleFunctionMaxValue() )
        .put( D2_MIN_VALUE, new RuleFunctionMinValue() )
        .put( D2_COUNT, new RuleFunctionCount() )
        .put( D2_COUNT_IF_VALUE, new RuleFunctionCountIfValue() )
        .put( D2_HAS_USER_ROLE, new RuleFunctionHasUserRole() )
        .put( D2_HAS_VALUE, new RuleFunctionHasValue() )
        .put( D2_IN_ORG_UNIT_GROUP, new RuleFunctionInOrgUnitGroup() )
        .put( D2_LAST_EVENT_DATE, new RuleFunctionLastEventDate() )
        .put( D2_COUNT_IF_ZERO_POS, new RuleFunctionCountIfZeroPos() )

        .put( V_PROGRAM_STAGE_NAME, new ProgramStageNameVariable() )
        .put( V_EVENT_STATUS, new ProgramStageNameVariable() )
        .put( V_ENVIRONMENT, new ProgramStageNameVariable() )

        .putAll( COMMON_EXPRESSION_FUNCTIONS )

        .build();

    private static final String TODAY = new Date().toString();

    private static final Log log = LogFactory.getLog(RuleEngineExecution.class);

    private static final String REGEX = "[a-zA-Z0-9]+(?:[\\w -]*[a-zA-Z0-9]+)*";

    private static final Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

    @Nonnull
    private Map<String, RuleVariableValue> valueMap;

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    RuleEngineExecution( @Nonnull List<Rule> rules,
                        @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData) {
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
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Exception in " + rule.name() + ": " + e.getMessage());
            }
        }
        return ruleEffects;
    }

    public static String validate( String condition )
    {
        return Parser.validate( condition );
    }

    private String process( String condition )
    {
        CommonExpressionVisitor commonExpressionVisitor = CommonExpressionVisitor.newBuilder()
            .withFunctionMap( FUNCTIONS )
            .withVariablesMap( valueMap )
            .withFunctionMethod( FUNCTION_EVALUATE )
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
