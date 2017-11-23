
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleActionCreateEvent extends RuleActionCreateEvent {

  private final String content;
  private final String data;
  private final String programStage;

  AutoValue_RuleActionCreateEvent(
      String content,
      String data,
      String programStage) {
    if (content == null) {
      throw new NullPointerException("Null content");
    }
    this.content = content;
    if (data == null) {
      throw new NullPointerException("Null data");
    }
    this.data = data;
    if (programStage == null) {
      throw new NullPointerException("Null programStage");
    }
    this.programStage = programStage;
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
  public String programStage() {
    return programStage;
  }

  @Override
  public String toString() {
    return "RuleActionCreateEvent{"
        + "content=" + content + ", "
        + "data=" + data + ", "
        + "programStage=" + programStage
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleActionCreateEvent) {
      RuleActionCreateEvent that = (RuleActionCreateEvent) o;
      return (this.content.equals(that.content()))
           && (this.data.equals(that.data()))
           && (this.programStage.equals(that.programStage()));
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
    h ^= this.programStage.hashCode();
    return h;
  }

}
