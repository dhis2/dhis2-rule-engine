
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleActionShowWarning extends RuleActionShowWarning {

  private final String content;
  private final String data;
  private final String field;

  AutoValue_RuleActionShowWarning(
      String content,
      String data,
      String field) {
    if (content == null) {
      throw new NullPointerException("Null content");
    }
    this.content = content;
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
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
  public String data() {
    return data;
  }

  @Nonnull
  @Override
  public String field() {
    return field;
  }

  @Override
  public String toString() {
    return "RuleActionShowWarning{"
        + "content=" + content + ", "
        + "data=" + data + ", "
        + "field=" + field
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleActionShowWarning) {
      RuleActionShowWarning that = (RuleActionShowWarning) o;
      return (this.content.equals(that.content()))
           && (this.data.equals(that.data()))
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
    h ^= this.data.hashCode();
    h *= 1000003;
    h ^= this.field.hashCode();
    return h;
  }

}
