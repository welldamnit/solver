package com.dropofink.constraints;

import com.dropofink.constructive.LiveDomain;
import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Constraint;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

public abstract class BinaryConstraint<T> implements Constraint<T> {
  protected final Variable<T> first;
  protected final Variable<T> second;
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

  public boolean isSupported(Assignment<T> assignment, Map<Variable<T>, ? extends LiveDomain<T>> liveDomains) {
    boolean firstAssigned = assignment.variable().equals(first);
    if (firstAssigned) {
      for (T secondValue : liveDomains.get(second).getLiveValues()) {
        if (isSatisfied(assignment.value(), secondValue)) {
          return true;
        }
      }
    } else {
      for (T firstValue : liveDomains.get(first).getLiveValues()) {
        if (isSatisfied(firstValue, assignment.value())) {
          return true;
        }
      }
    }
    return false;
  }

  public abstract boolean isSatisfied(T firstValue, T secondValue);
}
