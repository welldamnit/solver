package com.dropofink.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Assignment<T> {
  public abstract Variable<T> variable();
  public abstract T value();

  public static <T> Assignment<T> of(Variable<T> variable, T value) {
    return new AutoValue_Assignment<>(variable, value);
  }
}
