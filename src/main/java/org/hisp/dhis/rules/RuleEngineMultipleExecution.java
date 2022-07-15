package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEffects;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.TrackerObjectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class RuleEngineMultipleExecution
    implements Callable<List<RuleEffects>>
{

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
            RuleEnrollment enrollment = enrollments.getKey();
            List<RuleEffect> enrollmentRuleEffects = ruleConditionEvaluator
                .getEvaluatedAndErrorRuleEffects( TrackerObjectType.ENROLLMENT, enrollment.enrollment(), enrollments.getValue(),
                        supplementaryData, RuleEngineFilter.filterRules( rules, enrollment) );
            ruleEffects.add( new RuleEffects( TrackerObjectType.ENROLLMENT, enrollment.enrollment(),
                enrollmentRuleEffects ) );
        }

        for ( Map.Entry<RuleEvent, Map<String, RuleVariableValue>> events : ruleVariableValueMap
            .getEventMap().entrySet() )
        {
            RuleEvent event = events.getKey();
            ruleEffects.add( new RuleEffects( TrackerObjectType.EVENT, event.event(),
                ruleConditionEvaluator.getEvaluatedAndErrorRuleEffects( TrackerObjectType.EVENT, event.event(), events.getValue(),
                        supplementaryData, RuleEngineFilter.filterRules( rules, event) ) ) );
        }

        return ruleEffects;
    }
}
