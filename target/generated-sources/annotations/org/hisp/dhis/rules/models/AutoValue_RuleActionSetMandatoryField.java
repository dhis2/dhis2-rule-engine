
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleActionSetMandatoryField extends RuleActionSetMandatoryField {

  private final String field;

  AutoValue_RuleActionSetMandatoryField(
      String field) {
    if (field == null) {
      throw new NullPointerException("Null field");
    }
    this.field = field;
  }

  @Nonnull
  @Override
  public String field() {
    return field;
  }

  @Override
  public String toString() {
    return "RuleActionSetMandatoryField{"
        + "field=" + field
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleActionSetMandatoryField) {
      RuleActionSetMandatoryField that = (RuleActionSetMandatoryField) o;
      return (this.field.equals(that.field()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.field.hashCode();
    return h;
  }

}
