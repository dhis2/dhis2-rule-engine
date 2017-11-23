
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleActionHideField extends RuleActionHideField {

  private final String content;
  private final String field;

  AutoValue_RuleActionHideField(
      String content,
      String field) {
    if (content == null) {
      throw new NullPointerException("Null content");
    }
    this.content = content;
    if (field == null) {
      throw new NullPointerException("Null field");
    }
    this.field = field;
  }

  @Nonnull
  @Override
  public String content() {
    return content;
  }

  @Nonnull
  @Override
  public String field() {
    return field;
  }

  @Override
  public String toString() {
    return "RuleActionHideField{"
        + "content=" + content + ", "
        + "field=" + field
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleActionHideField) {
      RuleActionHideField that = (RuleActionHideField) o;
      return (this.content.equals(that.content()))
           && (this.field.equals(that.field()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.content.hashCode();
    h *= 1000003;
    h ^= this.field.hashCode();
    return h;
  }

}
