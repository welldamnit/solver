
package com.dropofink.model;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_Variable<T> extends Variable<T> {

  private final String name;
  private final Domain<T> domain;

  AutoValue_Variable(
      String name,
      Domain<T> domain) {
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
    if (domain == null) {
      throw new NullPointerException("Null domain");
    }
    this.domain = domain;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Domain<T> domain() {
    return domain;
  }

  @Override
  public String toString() {
    return "Variable{"
        + "name=" + name + ", "
        + "domain=" + domain
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Variable) {
      Variable<?> that = (Variable<?>) o;
      return (this.name.equals(that.name()))
           && (this.domain.equals(that.domain()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= name.hashCode();
    h *= 1000003;
    h ^= domain.hashCode();
    return h;
  }

}
