
package org.hisp.dhis.rules.models;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Rule extends Rule {

  private final String programStage;
  private final Integer priority;
  private final String condition;
  private final List<RuleAction> actions;

  AutoValue_Rule(
      @Nullable String programStage,
      @Nullable Integer priority,
      String condition,
      List<RuleAction> actions) {
    this.programStage = programStage;
    this.priority = priority;
    if (condition == null) {
      throw new NullPointerException("Null condition");
    }
    this.condition = condition;
    if (actions == null) {
      throw new NullPointerException("Null actions");
    }
    this.actions = actions;
  }

  @Nullable
  @Override
  public String programStage() {
    return programStage;
  }

  @Nullable
  @Override
  public Integer priority() {
    return priority;
  }

  @Nonnull
  @Override
  public String condition() {
    return condition;
  }

  @Nonnull
  @Override
  public List<RuleAction> actions() {
    return actions;
  }

  @Override
  public String toString() {
    return "Rule{"
        + "programStage=" + programStage + ", "
        + "priority=" + priority + ", "
        + "condition=" + condition + ", "
        + "actions=" + actions
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Rule) {
      Rule that = (Rule) o;
      return ((this.programStage == null) ? (that.programStage() == null) : this.programStage.equals(that.programStage()))
           && ((this.priority == null) ? (that.priority() == null) : this.priority.equals(that.priority()))
           && (this.condition.equals(that.condition()))
           && (this.actions.equals(that.actions()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (programStage == null) ? 0 : this.programStage.hashCode();
    h *= 1000003;
    h ^= (priority == null) ? 0 : this.priority.hashCode();
    h *= 1000003;
    h ^= this.condition.hashCode();
    h *= 1000003;
    h ^= this.actions.hashCode();
    return h;
  }

}
