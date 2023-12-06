package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.TrackerObjectType;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

record RuleEngineExecution(
        @CheckForNull RuleEvent event,
        @CheckForNull RuleEnrollment enrollment,
        @Nonnull Map<String, List<String>> supplementaryData,
        @Nonnull List<Rule> rules,
        @Nonnull Map<String, RuleVariableValue> valueMap,
        @Nonnull RuleConditionEvaluator ruleConditionEvaluator
) implements Callable<List<RuleEffect>> {

    RuleEngineExecution(@Nonnull RuleEvent event, @Nonnull List<Rule> rules,
                        @Nonnull Map<String, RuleVariableValue> valueMap, @Nonnull Map<String, List<String>> supplementaryData) {
        this(event, null, supplementaryData, rules, new HashMap<>(valueMap), new RuleConditionEvaluator());
    }

    RuleEngineExecution(@Nonnull RuleEnrollment enrollment, @Nonnull List<Rule> rules,
                        @Nonnull Map<String, RuleVariableValue> valueMap, @Nonnull Map<String, List<String>> supplementaryData) {
        this(null, enrollment, supplementaryData, rules, new HashMap<>(valueMap), new RuleConditionEvaluator());
    }

    @Override
    public List<RuleEffect> call() {
        if (event != null) {
            return ruleConditionEvaluator.getRuleEffects(TrackerObjectType.EVENT, event.event(), valueMap,
                    supplementaryData, this.rules);
        }
        if (enrollment != null) {
            return ruleConditionEvaluator.getRuleEffects(TrackerObjectType.ENROLLMENT, enrollment.getEnrollment(), valueMap,
                    supplementaryData, this.rules);
        }
        throw new IllegalStateException("event and enrollment were null");
    }
}
