
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleVariablePreviousEvent extends RuleVariablePreviousEvent {

  private final String name;
  private final String dataElement;
  private final RuleValueType dataElementType;

  AutoValue_RuleVariablePreviousEvent(
      String name,
      String dataElement,
      RuleValueType dataElementType) {
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

  @Override
  public String toString() {
    return "RuleVariablePreviousEvent{"
        + "name=" + name + ", "
        + "dataElement=" + dataElement + ", "
        + "dataElementType=" + dataElementType
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleVariablePreviousEvent) {
      RuleVariablePreviousEvent that = (RuleVariablePreviousEvent) o;
      return (this.name.equals(that.name()))
           && (this.dataElement.equals(that.dataElement()))
           && (this.dataElementType.equals(that.dataElementType()));
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
    return h;
  }

}
