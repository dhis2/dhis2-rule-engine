package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record RuleEvaluationResult(
        @Nonnull Rule rule,
        @Nonnull List<RuleEffect> ruleEffects,
        boolean evaluatedAs,
        boolean error)
{

    public static RuleEvaluationResult evaluatedResult(@Nonnull Rule rule, @Nonnull List<RuleEffect> ruleEffects) {
        return new RuleEvaluationResult( rule, ruleEffects, true, false );
    }

    public static RuleEvaluationResult notEvaluatedResult(@Nonnull Rule rule) {
        return new RuleEvaluationResult( rule, new ArrayList<>(), false, false );
    }

    public static RuleEvaluationResult errorRule( @Nonnull Rule rule, @Nonnull String errorMessage ) {
        ArrayList<RuleEffect> effects = new ArrayList<>();

        effects.add(new RuleEffect(rule.uid(), RuleActionError.create(errorMessage), errorMessage));
        return new RuleEvaluationResult( rule, effects, false, true );
    }
}
