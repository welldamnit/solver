package com.dropofink.model;

public abstract class Value<T extends Comparable<T>> implements Comparable<Value<T>> {
  public abstract T getValue();

  @Override
  public int hashCode() {return toString().hashCode();}

  @Override
  public boolean equals(Object o) {
    if (o instanceof Value) {
      return compareTo((Value) o) == 0;
    }
    return false;
  }

  @Override
  public String toString() {
    return getValue().toString();
  }
}
