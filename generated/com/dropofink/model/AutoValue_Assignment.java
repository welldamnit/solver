
package com.dropofink.model;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_Assignment<T> extends Assignment<T> {

  private final Variable<T> variable;
  private final T value;
  private final boolean isFailedAttempt;

  AutoValue_Assignment(
      Variable<T> variable,
      T value,
      boolean isFailedAttempt) {
    if (variable == null) {
      throw new NullPointerException("Null variable");
    }
    this.variable = variable;
    if (value == null) {
      throw new NullPointerException("Null value");
    }
    this.value = value;
    this.isFailedAttempt = isFailedAttempt;
  }

  @Override
  public Variable<T> variable() {
    return variable;
  }

  @Override
  public T value() {
    return value;
  }

  @Override
  public boolean isFailedAttempt() {
    return isFailedAttempt;
  }

  @Override
  public String toString() {
    return "Assignment{"
        + "variable=" + variable + ", "
        + "value=" + value + ", "
        + "isFailedAttempt=" + isFailedAttempt
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Assignment) {
      Assignment<?> that = (Assignment<?>) o;
      return (this.variable.equals(that.variable()))
           && (this.value.equals(that.value()))
           && (this.isFailedAttempt == that.isFailedAttempt());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= variable.hashCode();
    h *= 1000003;
    h ^= value.hashCode();
    h *= 1000003;
    h ^= isFailedAttempt ? 1231 : 1237;
    return h;
  }

}
