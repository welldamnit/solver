package com.dropofink.constructive;

import com.dropofink.model.*;

import java.util.*;

public class Search<T> {
  private final VariableHeuristic<T> variableHeuristic;
  private final ValueHeuristic<T> valueHeuristic;
  private final State<T> state;
  private final Map<Variable<T>, LiveDomainWithContext<T>> liveDomains;
  private final ConstraintPropagator<T> constraintPropagator;

  private long statesVisited;
  private boolean done;

  public Search(Problem<T> problem, VariableHeuristic<T> variableHeuristic, ValueHeuristic<T> valueHeuristic) {
    this.variableHeuristic = variableHeuristic;
    this.valueHeuristic = valueHeuristic;
    this.state = new State<>(problem);
    this.liveDomains = initLiveDomains(problem.getVariables());
    this.constraintPropagator = new ConstraintPropagator<>(problem, liveDomains);
    statesVisited = 0;
    done = false;
  }

  private static <T> Map<Variable<T>, LiveDomainWithContext<T>> initLiveDomains(Collection<Variable<T>> variables) {
    Map<Variable<T>, LiveDomainWithContext<T>> liveDomains = new HashMap<>();
    for (Variable<T> variable : variables) {
      liveDomains.put(variable, new LiveDomainWithContext<>(variable.domain()));
    }
    return liveDomains;
  }

  public boolean hasNextSolution() {
    while (!state.isLeaf() && !done) {
      statesVisited++;

      Variable<T> nextVariable = variableHeuristic.nextVariable(state.getUnassignedVariables(), liveDomains);
      Set<T> liveValues = liveDomains.get(nextVariable).getLiveValues();
      boolean shouldBacktrack = liveValues.isEmpty();
      if (!shouldBacktrack) {
        T nextValue = valueHeuristic.nextValue(nextVariable, liveValues);
        Assignment<T> nextAssignment = Assignment.of(nextVariable, nextValue);
        shouldBacktrack = !constraintPropagator.propagateAssignment(nextAssignment);
        state.extend(nextAssignment);
      }
      if (shouldBacktrack) {
        // If we got here then either the selected variable already had an empty live domain, or constraint propagation
        // just wiped out domain of some variable. Need to backtrack and search elsewhere.
        backtrack();
      }
    }
    // When we get here we are either at a leaf, which is a solution, or we exhausted search space, and we are done,
    // no more solutions can be found.
    return !done;
  }

  private void backtrack() {
    while (!state.isRoot()) {
      // Retract the latest assignment and repropagate.
      Assignment<T> lastAssignment = state.unassignLast();
      constraintPropagator.undoLastPropagation();
      if (lastAssignment.isFailedAttempt()) {
        // If we got here then we explored both branches: assignment and negation. Need to backtrack another level up.
        continue;
      }

      // If we got here then we explored the left branch, assignment, and can now try negation. Do so, and if subsequent
      // constraint propagation doesn't wipe out a variable's live domain, then we exited inconsistent state and can
      // stop backtracking.
      final Assignment<T> failedAttempt = lastAssignment.toFailedAttempt();
      boolean canResumeSearch = constraintPropagator.propagateAssignment(failedAttempt);
      state.extend(failedAttempt);
      if (canResumeSearch) {
        return;
      }
    }
    done = true;
  }

  public Assignments<T> nextSolution() {
    final Assignments<T> assignments = new Assignments<>(state.getAssignmentStack());
    // Backtrack out of the current solution to move on to the next.
    backtrack();
    return assignments;
  }

  public long getStatesVisited() {
    return statesVisited;
  }
}
