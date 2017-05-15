package com.dropofink.model;

import com.dropofink.problems.NQueensBinary;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Problem<T> {
  private final ImmutableList<Variable<T>> variables;
  private final ImmutableSet<Constraint<T>> constraints;
  private final Multimap<Variable<T>, Constraint<T>> variableToConstraints;

  public Problem(List<Variable<T>> variables, Set<Constraint<T>> constraints) {
    this.variables = ImmutableList.copyOf(variables);
    this.constraints = ImmutableSet.copyOf(constraints);
    this.variableToConstraints = HashMultimap.create();

    for (Constraint<T> constraint : constraints) {
      for (Variable<T>  constraintVariable : constraint.getVariables()) {
        variableToConstraints.put(constraintVariable, constraint);
      }
    }
  }

  public List<Variable<T>> getVariables() {
    return variables;
  }

  public Set<Constraint<T>> getConstraints() {
    return constraints;
  }

  public Collection<Constraint<T>> getConstraintsFor(Variable<T> variable) {
    return variableToConstraints.get(variable);
  }

  public boolean isSatisfied(Assignments<T> assignments) {
    long broken = getConstraints().stream().filter(c -> !c.isSatisfied(assignments)).count();
    return broken == 0;
  }
}
