
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleVariableAttribute extends RuleVariableAttribute {

  private final String name;
  private final String trackedEntityAttribute;
  private final RuleValueType trackedEntityAttributeType;

  AutoValue_RuleVariableAttribute(
      String name,
      String trackedEntityAttribute,
      RuleValueType trackedEntityAttributeType) {
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
    if (trackedEntityAttribute == null) {
      throw new NullPointerException("Null trackedEntityAttribute");
    }
    this.trackedEntityAttribute = trackedEntityAttribute;
    if (trackedEntityAttributeType == null) {
      throw new NullPointerException("Null trackedEntityAttributeType");
    }
    this.trackedEntityAttributeType = trackedEntityAttributeType;
  }

  @Nonnull
  @Override
  public String name() {
    return name;
  }

  @Nonnull
  @Override
  public String trackedEntityAttribute() {
    return trackedEntityAttribute;
  }

  @Nonnull
  @Override
  public RuleValueType trackedEntityAttributeType() {
    return trackedEntityAttributeType;
  }

  @Override
  public String toString() {
    return "RuleVariableAttribute{"
        + "name=" + name + ", "
        + "trackedEntityAttribute=" + trackedEntityAttribute + ", "
        + "trackedEntityAttributeType=" + trackedEntityAttributeType
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleVariableAttribute) {
      RuleVariableAttribute that = (RuleVariableAttribute) o;
      return (this.name.equals(that.name()))
           && (this.trackedEntityAttribute.equals(that.trackedEntityAttribute()))
           && (this.trackedEntityAttributeType.equals(that.trackedEntityAttributeType()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.name.hashCode();
    h *= 1000003;
    h ^= this.trackedEntityAttribute.hashCode();
    h *= 1000003;
    h ^= this.trackedEntityAttributeType.hashCode();
    return h;
  }

}
