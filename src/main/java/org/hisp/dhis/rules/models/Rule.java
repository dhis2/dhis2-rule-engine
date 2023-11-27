package org.hisp.dhis.rules.models;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

;

public record Rule(
    @CheckForNull
    String name,
    @CheckForNull
    String programStage,
    @CheckForNull
    Integer priority,
    @Nonnull
    String condition,
    @Nonnull
    List<RuleAction> actions,
    @Nonnull
    String uid) {

    public static final Rule MOCK = new Rule(null, null, null, "true", List.of(), null);

    public static Rule copy( @Nonnull Rule rule, @Nonnull List<RuleAction> actions )
    {
        return new Rule(rule.name(), rule.programStage(), rule.priority(), rule.condition(),
                List.copyOf(actions), rule.uid());
    }

    @Nonnull
    public static Rule create( @CheckForNull String programStage, @CheckForNull Integer priority,
        @Nonnull String condition, @Nonnull List<RuleAction> actions, @CheckForNull String name, @Nonnull String uid )
    {
        return new Rule(name, programStage, priority, condition, List.copyOf(actions), uid);
    }


}
