package com.dropofink.constructive;

import com.dropofink.model.*;

import java.util.*;

public class Search<T> {
  private final VariableHeuristic<T> variableHeuristic;
  private final ValueHeuristic<T> valueHeuristic;
  private final State<T> state;
  private final ConstraintPropagator<T> constraintPropagator;

  private long statesVisited;

  public Search(Problem<T> problem, VariableHeuristic<T> variableHeuristic, ValueHeuristic<T> valueHeuristic) {
    this.variableHeuristic = variableHeuristic;
    this.valueHeuristic = valueHeuristic;
    this.state = new State<>(problem);
    this.constraintPropagator = new ConstraintPropagator<>(problem);

    statesVisited = 0;
  }

  public boolean hasNextSolution() {
    while (!state.isLeaf()) {
      statesVisited++;

      Variable<T> nextVar = state.suggestNextVariable(variableHeuristic);
      Set<T> allowedValues = constraintPropagator.getLiveValues(nextVar);
      boolean shouldBacktrack = allowedValues.isEmpty();
      if (!shouldBacktrack) {
        T nextVal = valueHeuristic.nextValue(nextVar, allowedValues);
        Assignment<T> nextAssignment = Assignment.of(nextVar, nextVal);
        shouldBacktrack = !constraintPropagator.propagateAssignment(nextAssignment);
        state.extend(nextAssignment);
      }
      if (shouldBacktrack) {
        // If we got here either the selected variable already had an empty live domain, or constraint propagation
        // just wiped out domain of some variable. Need to backtrack and search elsewhere.
        if (!backtrack()) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean backtrack() {
    while (true) {
      if (state.isRoot()) {
        return false;
      }

      // Retract the latest assignment and repropagate.
      Assignment<T> lastAssignment = state.unassignLast();
      constraintPropagator.undoLastPropagation();
      if (lastAssignment.isFailedAttempt()) {
        // If we got here then we explored both branches: assignment and negation. Need to backtrack another level up.
        continue;
      }

      // If we got here then we explored the left branch, assignment, and can now try negation. Do so.
      final Assignment<T> failedAttempt = lastAssignment.toFailedAttempt();
      // Note that propagation of failed attempt (removing a single value from live domain, essentially) can also
      // cause us to get to a state where some variable's domain is wiped out and we should keep backtracking.
      boolean canResumeSearch = constraintPropagator.propagateAssignment(failedAttempt);
      state.extend(failedAttempt);
      if (canResumeSearch) {
        return true;
      }
    }
  }

  public Assignments<T> nextSolution() {
    final Assignments<T> assignments = new Assignments<>(state.getAssignmentStack());
    Assignment<T> lastAssignment = state.unassignLast();
    constraintPropagator.undoLastPropagation();
    final Assignment<T> disallowSameSolution = lastAssignment.toFailedAttempt();
    constraintPropagator.propagateAssignment(disallowSameSolution);
    state.extend(disallowSameSolution);
    return assignments;
  }

  public long getStatesVisited() {
    return statesVisited;
  }
}
