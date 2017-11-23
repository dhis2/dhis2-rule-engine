
package org.hisp.dhis.rules.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleDataValue extends RuleDataValue {

  private final Date eventDate;
  private final String programStage;
  private final String dataElement;
  private final String value;

  AutoValue_RuleDataValue(
      Date eventDate,
      String programStage,
      String dataElement,
      String value) {
    if (eventDate == null) {
      throw new NullPointerException("Null eventDate");
    }
    this.eventDate = eventDate;
    if (programStage == null) {
      throw new NullPointerException("Null programStage");
    }
    this.programStage = programStage;
    if (dataElement == null) {
      throw new NullPointerException("Null dataElement");
    }
    this.dataElement = dataElement;
    if (value == null) {
      throw new NullPointerException("Null value");
    }
    this.value = value;
  }

  @Nonnull
  @Override
  public Date eventDate() {
    return eventDate;
  }

  @Nonnull
  @Override
  public String programStage() {
    return programStage;
  }

  @Nonnull
  @Override
  public String dataElement() {
    return dataElement;
  }

  @Nonnull
  @Override
  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return "RuleDataValue{"
        + "eventDate=" + eventDate + ", "
        + "programStage=" + programStage + ", "
        + "dataElement=" + dataElement + ", "
        + "value=" + value
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleDataValue) {
      RuleDataValue that = (RuleDataValue) o;
      return (this.eventDate.equals(that.eventDate()))
           && (this.programStage.equals(that.programStage()))
           && (this.dataElement.equals(that.dataElement()))
           && (this.value.equals(that.value()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.eventDate.hashCode();
    h *= 1000003;
    h ^= this.programStage.hashCode();
    h *= 1000003;
    h ^= this.dataElement.hashCode();
    h *= 1000003;
    h ^= this.value.hashCode();
    return h;
  }

}
