package org.hisp.dhis.rules.models;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class Rule
{
    public static Rule copy( @Nonnull Rule rule, @Nonnull List<RuleAction> actions )
    {
        return new AutoValue_Rule( rule.name(), rule.programStage(), rule.priority(), rule.condition(),
            Collections.unmodifiableList( new ArrayList<>( actions ) ), rule.uid() );
    }

    @Nonnull
    public static Rule create( @Nullable String programStage, @Nullable Integer priority,
        @Nonnull String condition, @Nonnull List<RuleAction> actions, @Nullable String name, @Nonnull String uid )
    {
        return new AutoValue_Rule( name, programStage, priority, condition,
            Collections.unmodifiableList( new ArrayList<>( actions ) ), uid );
    }

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String programStage();

    @Nullable
    public abstract Integer priority();

    @Nonnull
    public abstract String condition();

    @Nonnull
    public abstract List<RuleAction> actions();

    @Nonnull
    public abstract String uid();
}
