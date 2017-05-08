package com.dropofink.constructive;

import com.dropofink.model.*;
import com.google.common.collect.Sets;

import java.util.*;

public class Search {
  private final VariableHeuristic variableHeuristic;
  private final ValueHeuristic valueHeuristic;

  private State currentState;

  public Search(Problem problem, VariableHeuristic variableHeuristic, ValueHeuristic valueHeuristic) {
    this.variableHeuristic = variableHeuristic;
    this.valueHeuristic = valueHeuristic;

    currentState = new State(problem);
  }

  public boolean hasNextSolution() {
    while (!currentState.isLeaf()) {
      Variable nextVar = variableHeuristic.nextVariable(currentState.getUnassignedVariables());
      Set<Value> allowedValues = getAllowedValues(nextVar);
      if (allowedValues.isEmpty()) {
        if (currentState.isRoot()) {
          return false;
        }
        // TODO: No solution here, backtracking. Could record a no-good.
        currentState.unassignLast();
      } else {
        Value nextVal = valueHeuristic.nextValue(nextVar, allowedValues);
        currentState.extend(Assignment.of(nextVar, nextVal));
      }
    }
    return true;
  }

  public Assignments nextSolution() {
    final Assignments assignments = new Assignments(currentState.getAssignmentStack());
    currentState.unassignLast();
    return assignments;
  }

  private Set<Value> getAllowedValues(Variable variable) {
    Set<Value> attemptedValues = currentState.getCurrentAttemptedAssignments().getAssignedValuesFor(variable);
    Set<Value> constraintBreakingValues =
        currentState.getConstraintViolatingPotentialAssignments().getAssignedValuesFor(variable);
    Set<Value> unattemptedValues = Sets.difference(variable.domain().getValues(), attemptedValues);
    return Sets.difference(unattemptedValues, constraintBreakingValues);
  }
}
