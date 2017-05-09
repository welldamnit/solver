package com.dropofink.model;

import com.google.auto.value.AutoValue;

import javax.validation.constraints.NotNull;

@AutoValue
public abstract class Variable<T> implements Comparable<Variable<T>> {
  public abstract String name();
  public abstract Domain<T> domain();

  public static <T> Variable<T> create(String name, Domain<T> domain) {
    return new AutoValue_Variable<>(name, domain);
  }

  @Override
  public int compareTo(@org.jetbrains.annotations.NotNull Variable<T> variable) {
    return name().compareTo(variable.name());
  }
}
