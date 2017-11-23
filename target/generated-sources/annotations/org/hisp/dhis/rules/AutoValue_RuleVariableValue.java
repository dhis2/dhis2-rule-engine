
package org.hisp.dhis.rules;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.hisp.dhis.rules.models.RuleValueType;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleVariableValue extends RuleVariableValue {

  private final String value;
  private final RuleValueType type;
  private final List<String> candidates;

  AutoValue_RuleVariableValue(
      @Nullable String value,
      RuleValueType type,
      List<String> candidates) {
    this.value = value;
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    if (candidates == null) {
      throw new NullPointerException("Null candidates");
    }
    this.candidates = candidates;
  }

  @Nullable
  @Override
  public String value() {
    return value;
  }

  @Nonnull
  @Override
  public RuleValueType type() {
    return type;
  }

  @Nonnull
  @Override
  public List<String> candidates() {
    return candidates;
  }

  @Override
  public String toString() {
    return "RuleVariableValue{"
        + "value=" + value + ", "
        + "type=" + type + ", "
        + "candidates=" + candidates
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleVariableValue) {
      RuleVariableValue that = (RuleVariableValue) o;
      return ((this.value == null) ? (that.value() == null) : this.value.equals(that.value()))
           && (this.type.equals(that.type()))
           && (this.candidates.equals(that.candidates()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= (value == null) ? 0 : this.value.hashCode();
    h *= 1000003;
    h ^= this.type.hashCode();
    h *= 1000003;
    h ^= this.candidates.hashCode();
    return h;
  }

}
