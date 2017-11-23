
package org.hisp.dhis.rules.models;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleActionHideSection extends RuleActionHideSection {

  private final String programStageSection;

  AutoValue_RuleActionHideSection(
      String programStageSection) {
    if (programStageSection == null) {
      throw new NullPointerException("Null programStageSection");
    }
    this.programStageSection = programStageSection;
  }

  @Nonnull
  @Override
  public String programStageSection() {
    return programStageSection;
  }

  @Override
  public String toString() {
    return "RuleActionHideSection{"
        + "programStageSection=" + programStageSection
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleActionHideSection) {
      RuleActionHideSection that = (RuleActionHideSection) o;
      return (this.programStageSection.equals(that.programStageSection()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.programStageSection.hashCode();
    return h;
  }

}
