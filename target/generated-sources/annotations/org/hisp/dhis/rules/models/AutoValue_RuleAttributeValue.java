
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleAttributeValue extends RuleAttributeValue {

  private final String trackedEntityAttribute;
  private final String value;

  AutoValue_RuleAttributeValue(
      String trackedEntityAttribute,
      String value) {
    if (trackedEntityAttribute == null) {
      throw new NullPointerException("Null trackedEntityAttribute");
    }
    this.trackedEntityAttribute = trackedEntityAttribute;
    if (value == null) {
      throw new NullPointerException("Null value");
    }
    this.value = value;
  }

  @Nonnull
  @Override
  public String trackedEntityAttribute() {
    return trackedEntityAttribute;
  }

  @Nonnull
  @Override
  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return "RuleAttributeValue{"
        + "trackedEntityAttribute=" + trackedEntityAttribute + ", "
        + "value=" + value
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleAttributeValue) {
      RuleAttributeValue that = (RuleAttributeValue) o;
      return (this.trackedEntityAttribute.equals(that.trackedEntityAttribute()))
           && (this.value.equals(that.value()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.trackedEntityAttribute.hashCode();
    h *= 1000003;
    h ^= this.value.hashCode();
    return h;
  }

}
