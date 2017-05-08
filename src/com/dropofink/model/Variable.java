package com.dropofink.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Variable {
  public abstract String name();
  public abstract Domain domain();

  public static Variable create(String name, Domain domain) {
    return new AutoValue_Variable(name, domain);
  }
}
