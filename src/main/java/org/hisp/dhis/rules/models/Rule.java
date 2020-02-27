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
    @Nonnull
    public static Rule create( @Nullable String programStage, @Nullable Integer priority,
        @Nonnull String condition, @Nonnull List<RuleAction> actions, @Nullable String name )
    {
        return new AutoValue_Rule( name, programStage, priority, condition,
            Collections.unmodifiableList( new ArrayList<>( actions ) ) );
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
}
