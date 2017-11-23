
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleActionDisplayText extends RuleActionDisplayText {

  private final String content;
  private final String data;
  private final String location;

  AutoValue_RuleActionDisplayText(
      String content,
      String data,
      String location) {
    if (content == null) {
      throw new NullPointerException("Null content");
    }
    this.content = content;
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
    if (location == null) {
      throw new NullPointerException("Null location");
    }
    this.location = location;
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
  public String location() {
    return location;
  }

  @Override
  public String toString() {
    return "RuleActionDisplayText{"
        + "content=" + content + ", "
        + "data=" + data + ", "
        + "location=" + location
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleActionDisplayText) {
      RuleActionDisplayText that = (RuleActionDisplayText) o;
      return (this.content.equals(that.content()))
           && (this.data.equals(that.data()))
           && (this.location.equals(that.location()));
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
    h ^= this.location.hashCode();
    return h;
  }

}
