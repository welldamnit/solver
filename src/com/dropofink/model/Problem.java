package com.dropofink.model;

import com.dropofink.problems.NQueensBinary;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Problem {
  private final ImmutableList<Variable> variables;
  private final ImmutableSet<Constraint> constraints;

  public Problem(List<Variable> variables, Set<Constraint> constraints) {
    this.variables = ImmutableList.copyOf(variables);
    this.constraints = ImmutableSet.copyOf(constraints);
  }

  public List<Variable> getVariables() {
    return variables;
  }

  public Set<Constraint> getConstraints() {
    return constraints;
  }

  public boolean isSatisfied(Assignments assignments) {
    long broken = getConstraints().stream().filter(c -> !c.isSatisfied(assignments)).count();
    return broken == 0;
  }
}
