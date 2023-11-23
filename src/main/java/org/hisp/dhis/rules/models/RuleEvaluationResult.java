package org.hisp.dhis.rules.models;

import java.util.ArrayList;
import java.util.List;

public class RuleEvaluationResult
{

    private Rule rule;

    private List<RuleEffect> ruleEffects;

    private boolean evaluatedAs;

    private boolean error;

    public static RuleEvaluationResult evaluatedResult(Rule rule, List<RuleEffect> ruleEffects) {
        return new RuleEvaluationResult( rule, ruleEffects, true, false );
    }

    public static RuleEvaluationResult notEvaluatedResult(Rule rule) {
        return new RuleEvaluationResult( rule, new ArrayList<RuleEffect>(), false, false );
    }

    public static RuleEvaluationResult errorRule( Rule rule, String errorMessage ) {
        ArrayList<RuleEffect> effects = new ArrayList<>();

        effects.add(new RuleEffect(rule.uid(), RuleActionError.create(errorMessage), errorMessage));
        return new RuleEvaluationResult( rule, effects, false, true );
    }

    private RuleEvaluationResult( Rule rule, List<RuleEffect> ruleEffects, boolean evaluatedAs, boolean error ) {
        this.rule = rule;
        this.ruleEffects = ruleEffects;
        this.evaluatedAs = evaluatedAs;
        this.error = error;
    }

    public Rule getRule() {
        return rule;
    }

    public List<RuleEffect> getRuleEffects() {
        return ruleEffects;
    }

    public boolean isEvaluatedAs() {
        return evaluatedAs;
    }

    public boolean isError() {
        return error;
    }
}
