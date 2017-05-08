package com.dropofink.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Assignment {
  public abstract Variable variable();
  public abstract Value value();

  public static Assignment of(Variable variable, Value value) {
    return new AutoValue_Assignment(variable, value);
  }
}
