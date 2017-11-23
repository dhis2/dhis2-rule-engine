
package org.hisp.dhis.rules.models;

import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleEvent extends RuleEvent {

  private final String event;
  private final String programStage;
  private final RuleEvent.Status status;
  private final Date eventDate;
  private final Date dueDate;
  private final List<RuleDataValue> dataValues;

  AutoValue_RuleEvent(
      String event,
      String programStage,
      RuleEvent.Status status,
      Date eventDate,
      Date dueDate,
      List<RuleDataValue> dataValues) {
    if (event == null) {
      throw new NullPointerException("Null event");
    }
    this.event = event;
    if (programStage == null) {
      throw new NullPointerException("Null programStage");
    }
    this.programStage = programStage;
    if (status == null) {
      throw new NullPointerException("Null status");
    }
    this.status = status;
    if (eventDate == null) {
      throw new NullPointerException("Null eventDate");
    }
    this.eventDate = eventDate;
    if (dueDate == null) {
      throw new NullPointerException("Null dueDate");
    }
    this.dueDate = dueDate;
    if (dataValues == null) {
      throw new NullPointerException("Null dataValues");
    }
    this.dataValues = dataValues;
  }

  @Nonnull
  @Override
  public String event() {
    return event;
  }

  @Nonnull
  @Override
  public String programStage() {
    return programStage;
  }

  @Nonnull
  @Override
  public RuleEvent.Status status() {
    return status;
  }

  @Nonnull
  @Override
  public Date eventDate() {
    return eventDate;
  }

  @Nonnull
  @Override
  public Date dueDate() {
    return dueDate;
  }

  @Nonnull
  @Override
  public List<RuleDataValue> dataValues() {
    return dataValues;
  }

  @Override
  public String toString() {
    return "RuleEvent{"
        + "event=" + event + ", "
        + "programStage=" + programStage + ", "
        + "status=" + status + ", "
        + "eventDate=" + eventDate + ", "
        + "dueDate=" + dueDate + ", "
        + "dataValues=" + dataValues
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleEvent) {
      RuleEvent that = (RuleEvent) o;
      return (this.event.equals(that.event()))
           && (this.programStage.equals(that.programStage()))
           && (this.status.equals(that.status()))
           && (this.eventDate.equals(that.eventDate()))
           && (this.dueDate.equals(that.dueDate()))
           && (this.dataValues.equals(that.dataValues()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.event.hashCode();
    h *= 1000003;
    h ^= this.programStage.hashCode();
    h *= 1000003;
    h ^= this.status.hashCode();
    h *= 1000003;
    h ^= this.eventDate.hashCode();
    h *= 1000003;
    h ^= this.dueDate.hashCode();
    h *= 1000003;
    h ^= this.dataValues.hashCode();
    return h;
  }

}
