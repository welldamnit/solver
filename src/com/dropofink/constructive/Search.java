package com.dropofink.constructive;

import com.dropofink.model.*;
import com.google.common.collect.Sets;

import java.util.*;

public class Search<T> {
  private final VariableHeuristic<T> variableHeuristic;
  private final ValueHeuristic<T> valueHeuristic;

  private State<T> currentState;
  private long statesVisited;

  public Search(Problem<T> problem, VariableHeuristic<T> variableHeuristic, ValueHeuristic<T> valueHeuristic) {
    this.variableHeuristic = variableHeuristic;
    this.valueHeuristic = valueHeuristic;

    currentState = new State<>(problem);
    statesVisited = 0;
  }

  public boolean hasNextSolution() {
    while (!currentState.isLeaf()) {
      statesVisited++;
      Variable<T> nextVar = variableHeuristic.nextVariable(currentState.getUnassignedVariables());
      Set<T> allowedValues = getAllowedValues(nextVar);
      if (allowedValues.isEmpty()) {
        if (currentState.isRoot()) {
          return false;
        }
        // TODO: No solution here, backtracking. Could record a no-good.
        currentState.unassignLast();
      } else {
        T nextVal = valueHeuristic.nextValue(nextVar, allowedValues);
        currentState.extend(Assignment.of(nextVar, nextVal));
      }
    }
    return true;
  }

  public Assignments<T> nextSolution() {
    final Assignments<T> assignments = new Assignments<>(currentState.getAssignmentStack());
    currentState.unassignLast();
    return assignments;
  }

  private Set<T> getAllowedValues(Variable<T> variable) {
    Set<T> attemptedValues = currentState.getCurrentAttemptedAssignments().getAssignedValuesFor(variable);
    Set<T> constraintBreakingValues =
        currentState.getConstraintViolatingPotentialAssignments().getAssignedValuesFor(variable);
    Set<T> unattemptedValues = Sets.difference(variable.domain(), attemptedValues);
    return Sets.difference(unattemptedValues, constraintBreakingValues);
  }

  public long getStatesVisited() {
    return statesVisited;
  }
}
