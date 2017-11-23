
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleEffect extends RuleEffect {

  private final RuleAction ruleAction;
  private final String data;

  AutoValue_RuleEffect(
      RuleAction ruleAction,
      String data) {
    if (ruleAction == null) {
      throw new NullPointerException("Null ruleAction");
    }
    this.ruleAction = ruleAction;
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
  }

  @Nonnull
  @Override
  public RuleAction ruleAction() {
    return ruleAction;
  }

  @Nonnull
  @Override
  public String data() {
    return data;
  }

  @Override
  public String toString() {
    return "RuleEffect{"
        + "ruleAction=" + ruleAction + ", "
        + "data=" + data
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleEffect) {
      RuleEffect that = (RuleEffect) o;
      return (this.ruleAction.equals(that.ruleAction()))
           && (this.data.equals(that.data()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.ruleAction.hashCode();
    h *= 1000003;
    h ^= this.data.hashCode();
    return h;
  }

}
