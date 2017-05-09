package com.dropofink.model;

import java.util.Set;

public interface Constraint<T> {
  Set<Variable<T>> getVariables();
  boolean isSatisfied(Assignments<T> assignments);
}
