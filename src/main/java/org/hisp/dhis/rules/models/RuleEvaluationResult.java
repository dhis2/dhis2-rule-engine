package org.hisp.dhis.rules.models;

import java.util.ArrayList;
import java.util.List;

public class RuleEvaluationResult
{

    private Rule rule;

    private List<RuleEffect> ruleEffects;

    private boolean isEvaluated;

    public static RuleEvaluationResult evaluatedResult(Rule rule, List<RuleEffect> ruleEffects) {
        return new RuleEvaluationResult( rule, ruleEffects, true );
    }

    public static RuleEvaluationResult notEvaluatedResult(Rule rule) {
        return new RuleEvaluationResult( rule, new ArrayList<RuleEffect>(), false );
    }

    private RuleEvaluationResult( Rule rule, List<RuleEffect> ruleEffects, boolean isEvaluated ) {
        this.rule = rule;
        this.ruleEffects = ruleEffects;
        this.isEvaluated = isEvaluated;
    }

    public Rule getRule() {
        return rule;
    }

    public List<RuleEffect> getRuleEffects() {
        return ruleEffects;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }
}
