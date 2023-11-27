package org.hisp.dhis.rules.models;

import java.util.ArrayList;
import java.util.List;

public record RuleEvaluationResult(
        Rule rule,
        List<RuleEffect> ruleEffects,
        boolean evaluatedAs,
        boolean error)
{

    public static RuleEvaluationResult evaluatedResult(Rule rule, List<RuleEffect> ruleEffects) {
        return new RuleEvaluationResult( rule, ruleEffects, true, false );
    }

    public static RuleEvaluationResult notEvaluatedResult(Rule rule) {
        return new RuleEvaluationResult( rule, new ArrayList<>(), false, false );
    }

    public static RuleEvaluationResult errorRule( Rule rule, String errorMessage ) {
        ArrayList<RuleEffect> effects = new ArrayList<>();

        effects.add(new RuleEffect(rule.uid(), RuleActionError.create(errorMessage), errorMessage));
        return new RuleEvaluationResult( rule, effects, false, true );
    }
}
