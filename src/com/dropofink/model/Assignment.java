package com.dropofink.model;

import com.google.auto.value.AutoValue;

import static com.google.common.base.Preconditions.checkArgument;

@AutoValue
public abstract class Assignment<T> {
  public abstract Variable<T> variable();
  public abstract T value();
  public abstract boolean isFailedAttempt();

  public static <T> Assignment<T> of(Variable<T> variable, T value) {
    return new AutoValue_Assignment<>(variable, value, false);
  }

  public static <T> Assignment<T> failedAttempt(Variable<T> variable, T value) {
    return new AutoValue_Assignment<>(variable, value, true);
  }

  public Assignment<T> toFailedAttempt() {
    checkArgument(!isFailedAttempt());
    return new AutoValue_Assignment<>(variable(), value(), true);
  }
}
