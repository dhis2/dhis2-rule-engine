package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.TrackerObjectType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class RuleEngineExecution
    implements Callable<List<RuleEffect>>
{
    private final RuleEvent event;

    private final RuleEnrollment enrollment;

    @Nonnull
    private final Map<String, List<String>> supplementaryData;

    @Nonnull
    private final List<Rule> rules;

    @Nonnull
    private Map<String, RuleVariableValue> valueMap;

    @Nonnull
    private RuleConditionEvaluator ruleConditionEvaluator;

    RuleEngineExecution( @Nonnull RuleEvent event, @Nonnull List<Rule> rules,
        @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        this.event = event;
        this.enrollment = null;
        this.valueMap = new HashMap<>( valueMap );
        this.rules = rules;
        this.supplementaryData = supplementaryData;
        this.ruleConditionEvaluator = new RuleConditionEvaluator();
    }

    RuleEngineExecution( @Nonnull RuleEnrollment enrollment, @Nonnull List<Rule> rules,
                         @Nonnull Map<String, RuleVariableValue> valueMap, Map<String, List<String>> supplementaryData )
    {
        this.event = null;
        this.enrollment = enrollment;
        this.valueMap = new HashMap<>( valueMap );
        this.rules = rules;
        this.supplementaryData = supplementaryData;
        this.ruleConditionEvaluator = new RuleConditionEvaluator();
    }

    @Override
    public List<RuleEffect> call()
    {
        if (event != null) {
            return ruleConditionEvaluator.getRuleEffects(TrackerObjectType.EVENT, event.event(), valueMap,
                    supplementaryData, this.rules );
        } else {
            return ruleConditionEvaluator.getRuleEffects( TrackerObjectType.ENROLLMENT, enrollment.enrollment(), valueMap,
                    supplementaryData, this.rules );
        }
    }
}
