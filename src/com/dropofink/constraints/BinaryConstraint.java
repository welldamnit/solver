package com.dropofink.constraints;

import com.dropofink.model.Assignments;
import com.dropofink.model.Constraint;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public abstract class BinaryConstraint<T> implements Constraint<T> {
  private final Variable<T> first;
  private final Variable<T> second;
  private final Set<Variable<T>> variables;

  public BinaryConstraint(Variable<T> first, Variable<T> second) {
    this.first = first;
    this.second = second;
    this.variables = ImmutableSet.of(first, second);
  }

  @Override
  public Set<Variable<T>> getVariables() {
    return variables;
  }

  public boolean isSatisfied(Assignments<T> assignments) {
    T firstValue = assignments.getAssignedValueFor(first);
    T secondValue = assignments.getAssignedValueFor(second);
    return firstValue == null || secondValue == null || isSatisfied(firstValue, secondValue);
  }

  public abstract boolean isSatisfied(T firstValue, T secondValue);
}
