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

class RuleEngineMultipleExecution
    implements Callable<List<RuleEffects>>
{
    private static final Log log = LogFactory.getLog( RuleEngineMultipleExecution.class );

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    @Nonnull
    private RuleVariableValueMap ruleVariableValueMap;

    @Nonnull
    private RuleConditionEvaluator ruleConditionEvaluator;

    RuleEngineMultipleExecution( @Nonnull List<Rule> rules,
        @Nonnull RuleVariableValueMap ruleVariableValueMap, Map<String, List<String>> supplementaryData )
    {
        this.ruleVariableValueMap = ruleVariableValueMap;
        this.rules = rules;
        this.supplementaryData = supplementaryData;
        this.ruleConditionEvaluator = new RuleConditionEvaluator();
    }

    @Override
    public List<RuleEffects> call()
    {
        List<RuleEffects> ruleEffects = new ArrayList<>();

        for ( Map.Entry<RuleEnrollment, Map<String, RuleVariableValue>> enrollments : ruleVariableValueMap
            .getEnrollmentMap().entrySet() )
        {
            List<RuleEffect> enrollmentRuleEffects = ruleConditionEvaluator
                .getRuleEffects( enrollments.getValue(), supplementaryData,
                    RuleEngineFilter.filterRules( rules, enrollments.getKey() ) );
            ruleEffects.add( new RuleEffects( TrackerObjectType.ENROLLMENT, enrollments.getKey().enrollment(),
                enrollmentRuleEffects ) );
        }

        for ( Map.Entry<RuleEvent, Map<String, RuleVariableValue>> events : ruleVariableValueMap
            .getEventMap().entrySet() )
        {
            ruleEffects.add( new RuleEffects( TrackerObjectType.EVENT, events.getKey().event(),
                ruleConditionEvaluator.getRuleEffects( events.getValue(), supplementaryData,
                    RuleEngineFilter.filterRules( rules, events.getKey() ) ) ) );
        }

        return ruleEffects;
    }
}
