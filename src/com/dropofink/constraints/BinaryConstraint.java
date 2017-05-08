package com.dropofink.constraints;

import com.dropofink.model.*;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

public abstract class BinaryConstraint implements Constraint {
  protected final Variable first;
  protected final Variable second;
  private final Set<Variable> variables;

  public BinaryConstraint(Variable first, Variable second) {
    this.first = first;
    this.second = second;
    this.variables = ImmutableSet.of(first, second);
  }

  @Override
  public Set<Variable> getVariables() {
    return variables;
  }

  public boolean isSatisfied(Assignments assignments) {
    Value firstValue = assignments.getAssignedValueFor(first);
    Value secondValue = assignments.getAssignedValueFor(second);
    if (firstValue == null || secondValue == null) {
      return true;
    }
    return isSatisfied(firstValue, secondValue);
  }

  public abstract boolean isSatisfied(Value firstValue, Value secondValue);
}
