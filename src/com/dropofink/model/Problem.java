package com.dropofink.model;

import com.dropofink.problems.NQueensBinary;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Problem<T> {
  private final ImmutableList<Variable<T>> variables;
  private final ImmutableSet<Constraint<T>> constraints;

  public Problem(List<Variable<T>> variables, Set<Constraint<T>> constraints) {
    this.variables = ImmutableList.copyOf(variables);
    this.constraints = ImmutableSet.copyOf(constraints);
  }

  public List<Variable<T>> getVariables() {
    return variables;
  }

  public Set<Constraint<T>> getConstraints() {
    return constraints;
  }

  public boolean isSatisfied(Assignments<T> assignments) {
    long broken = getConstraints().stream().filter(c -> !c.isSatisfied(assignments)).count();
    return broken == 0;
  }
}
