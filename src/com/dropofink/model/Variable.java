package com.dropofink.model;

import com.google.auto.value.AutoValue;

import javax.validation.constraints.NotNull;

@AutoValue
public abstract class Variable implements Comparable<Variable> {
  public abstract String name();
  public abstract Domain domain();

  public static Variable create(String name, Domain domain) {
    return new AutoValue_Variable(name, domain);
  }

  @Override
  public int compareTo(@org.jetbrains.annotations.NotNull Variable variable) {
    return name().compareTo(variable.name());
  }
}
