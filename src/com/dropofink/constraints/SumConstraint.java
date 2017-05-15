package com.dropofink.constraints;

import com.dropofink.constructive.LiveDomain;
import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Constraint;
import com.dropofink.model.Variable;

import java.util.Map;
import java.util.Set;

public class SumConstraint implements Constraint<Integer> {
  private final Set<Variable<Integer>> variables;
  private final int sum;

  public SumConstraint(Set<Variable<Integer>> variables, int sum) {
    this.variables = variables;
    this.sum = sum;
  }

  @Override
  public Set<Variable<Integer>> getVariables() {
    return variables;
  }

  @Override
  public boolean isSatisfied(Assignments<Integer> assignments) {
    int actualSum = 0;
    boolean allAssigned = true;
    for (Variable<Integer> v : variables) {
      Integer value = assignments.getAssignedValueFor(v);
      if (value == null) {
        allAssigned = false;
      } else {
        actualSum += value;
      }
    }
    if (allAssigned) {
      return actualSum == sum;
    } else {
      return actualSum < sum;
    }
  }

  @Override
  public boolean isSupported(
      Assignment<Integer> assignment, Map<Variable<Integer>, ? extends LiveDomain<Integer>> liveDomains) {
    int minSum = assignment.value();
    int maxSum = assignment.value();
    for (Variable<Integer> variable : variables) {
      if (!variable.equals(assignment.variable())) {
        LiveDomain<Integer> live = liveDomains.get(variable);
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int value : live.getLiveValues()) {
          min = Math.min(min, value);
          max = Math.max(max, value);
        }
        minSum += min;
        maxSum += max;
      }
    }
    return minSum <= sum && maxSum >= sum;
  }
}