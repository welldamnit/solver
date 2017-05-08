package com.dropofink.model;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public abstract class Domain {
  public abstract Set<Value> getValues();

  @Override
  public int hashCode() { return toString().hashCode(); }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Domain) {
      return getValues().equals(((Domain)o).getValues());
    }
    return false;
  }

  @Override
  public String toString() {
    return format("{%s}", getValues().stream().map(Value::toString).sorted().collect(Collectors.joining(", ")));
  }
}
