package com.dropofink.model;

import java.util.Map;
import java.util.Set;

public interface Constraint {
  Set<Variable> getVariables();
  boolean isSatisfied(Assignments assignments);
}
