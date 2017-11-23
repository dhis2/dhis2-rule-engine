
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleVariableNewestStageEvent extends RuleVariableNewestStageEvent {

  private final String name;
  private final String dataElement;
  private final RuleValueType dataElementType;
  private final String programStage;

  AutoValue_RuleVariableNewestStageEvent(
      String name,
      String dataElement,
      RuleValueType dataElementType,
      String programStage) {
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
    if (dataElement == null) {
      throw new NullPointerException("Null dataElement");
    }
    this.dataElement = dataElement;
    if (dataElementType == null) {
      throw new NullPointerException("Null dataElementType");
    }
    this.dataElementType = dataElementType;
    if (programStage == null) {
      throw new NullPointerException("Null programStage");
    }
    this.programStage = programStage;
  }

  @Nonnull
  @Override
  public String name() {
    return name;
  }

  @Nonnull
  @Override
  public String dataElement() {
    return dataElement;
  }

  @Nonnull
  @Override
  public RuleValueType dataElementType() {
    return dataElementType;
  }

  @Nonnull
  @Override
  public String programStage() {
    return programStage;
  }

  @Override
  public String toString() {
    return "RuleVariableNewestStageEvent{"
        + "name=" + name + ", "
        + "dataElement=" + dataElement + ", "
        + "dataElementType=" + dataElementType + ", "
        + "programStage=" + programStage
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleVariableNewestStageEvent) {
      RuleVariableNewestStageEvent that = (RuleVariableNewestStageEvent) o;
      return (this.name.equals(that.name()))
           && (this.dataElement.equals(that.dataElement()))
           && (this.dataElementType.equals(that.dataElementType()))
           && (this.programStage.equals(that.programStage()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.name.hashCode();
    h *= 1000003;
    h ^= this.dataElement.hashCode();
    h *= 1000003;
    h ^= this.dataElementType.hashCode();
    h *= 1000003;
    h ^= this.programStage.hashCode();
    return h;
  }

}
