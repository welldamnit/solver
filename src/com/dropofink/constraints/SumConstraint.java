package com.dropofink.constraints;

import com.dropofink.constructive.LiveDomain;
import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Constraint;
import com.dropofink.model.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SumConstraint implements Constraint<Integer> {
  private final Set<Variable<Integer>> variables;
  private final Map<Variable<Integer>, Integer> minValues;
  private final Map<Variable<Integer>, Integer> maxValues;
  private final int sum;

  public SumConstraint(Set<Variable<Integer>> variables, int sum) {
    this.variables = variables;
    this.sum = sum;
    minValues = new HashMap<>();
    maxValues = new HashMap<>();
    for (Variable<Integer> var : variables) {
      int min = Integer.MAX_VALUE;
      int max = Integer.MIN_VALUE;
      for (Integer value : var.domain()) {
        min = Math.min(min, value);
        max = Math.max(max, value);
      }
      minValues.put(var, min);
      maxValues.put(var, max);
    }
  }

  @Override
  public Set<Variable<Integer>> getVariables() {
    return variables;
  }

  @Override
  public boolean isSatisfied(Assignments<Integer> assignments) {
    int actualSum = 0;
    int minUnassignedSum = 0;
    int maxUnassignedSum = 0;
    for (Variable<Integer> v : variables) {
      Integer value = assignments.getAssignedValueFor(v);
      if (value == null) {
        minUnassignedSum += minValues.get(v);
        maxUnassignedSum += maxValues.get(v);
      } else {
        actualSum += value;
      }
    }
    if (minUnassignedSum == 0 && maxUnassignedSum == 0) {
      return actualSum == sum;
    } else {
      if (actualSum + minUnassignedSum > sum) {
        return false;
      } else if (actualSum + maxUnassignedSum < sum) {
        return false;
      }
      return true;
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