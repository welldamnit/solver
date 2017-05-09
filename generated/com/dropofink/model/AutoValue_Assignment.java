
package com.dropofink.model;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_Assignment<T> extends Assignment<T> {

  private final Variable<T> variable;
  private final T value;

  AutoValue_Assignment(
      Variable<T> variable,
      T value) {
    if (variable == null) {
      throw new NullPointerException("Null variable");
    }
    this.variable = variable;
    if (value == null) {
      throw new NullPointerException("Null value");
    }
    this.value = value;
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
  public String toString() {
    return "Assignment{"
        + "variable=" + variable + ", "
        + "value=" + value
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
           && (this.value.equals(that.value()));
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
    return h;
  }

}
