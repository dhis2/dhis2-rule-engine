
package org.hisp.dhis.rules;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_RuleFunctionCall extends RuleFunctionCall {

  private final String functionCall;
  private final String functionName;
  private final List<String> arguments;

  AutoValue_RuleFunctionCall(
      String functionCall,
      String functionName,
      List<String> arguments) {
    if (functionCall == null) {
      throw new NullPointerException("Null functionCall");
    }
    this.functionCall = functionCall;
    if (functionName == null) {
      throw new NullPointerException("Null functionName");
    }
    this.functionName = functionName;
    if (arguments == null) {
      throw new NullPointerException("Null arguments");
    }
    this.arguments = arguments;
  }

  @Nonnull
  @Override
  public String functionCall() {
    return functionCall;
  }

  @Nonnull
  @Override
  public String functionName() {
    return functionName;
  }

  @Nonnull
  @Override
  public List<String> arguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "RuleFunctionCall{"
        + "functionCall=" + functionCall + ", "
        + "functionName=" + functionName + ", "
        + "arguments=" + arguments
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RuleFunctionCall) {
      RuleFunctionCall that = (RuleFunctionCall) o;
      return (this.functionCall.equals(that.functionCall()))
           && (this.functionName.equals(that.functionName()))
           && (this.arguments.equals(that.arguments()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.functionCall.hashCode();
    h *= 1000003;
    h ^= this.functionName.hashCode();
    h *= 1000003;
    h ^= this.arguments.hashCode();
    return h;
  }

}
