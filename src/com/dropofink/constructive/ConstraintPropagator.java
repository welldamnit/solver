package com.dropofink.constructive;

import com.dropofink.model.Assignment;
import com.dropofink.model.Constraint;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableSet;

import java.util.*;

import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.*;
import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.of;

public class ConstraintPropagator<T> {
  private final Problem<T> problem;
  private final Map<Variable<T>, LiveDomainWithContext<T>> liveDomains;
  private final Stack<Set<Variable<T>>> variablesPrunedByDepth;
  private final List<ConstraintCausedDomainWipeoutListener<T>> wipeoutListeners;

  interface ConstraintCausedDomainWipeoutListener<T> {
    void constraintCausedDomainWipeout(Constraint<T> constraint);
  }

  public ConstraintPropagator(Problem<T> problem, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains) {
    this.problem = problem;
    this.liveDomains = liveDomains;
    this.variablesPrunedByDepth = new Stack<>();
    wipeoutListeners = new ArrayList<>();
  }

  /** @return true if a solution still may exist after this assignment, false if definitely no solutions exist. */
  public boolean propagateAssignment(Assignment<T> assignment) {
    int depth = variablesPrunedByDepth.size();
    Set<Variable<T>> prunedVariables = new HashSet<>();
    if (assignment.isFailedAttempt()) {
      liveDomains.get(assignment.variable()).pruneWithReason(depth, of(assignment.value(), FAILED_ATTEMPT));
    } else {
      liveDomains.get(assignment.variable()).pruneWithReason(depth, of(assignment.value(), ASSIGNMENT));
    }
    prunedVariables.add(assignment.variable());

    // This is the arc consistency algorithm.
    Set<Variable<T>> frontier = new HashSet<>();
    frontier.add(assignment.variable());
    while (!frontier.isEmpty()) {
      Variable<T> variable = frontier.iterator().next();
      frontier.remove(variable);
      if (!propagateFromVariable(variable, prunedVariables, frontier)) {
        return false;
      }
    }
    variablesPrunedByDepth.push(prunedVariables);
    return true;
  }

  private boolean propagateFromVariable(
      Variable<T> sourceVariable, Set<Variable<T>> prunedVariables, Set<Variable<T>> frontier) {
    int depth = variablesPrunedByDepth.size();
    for (Constraint<T> constraint : problem.getConstraintsFor(sourceVariable)) {
      for (Variable<T> variable : constraint.getVariables()) {
        if (!variable.equals(sourceVariable)) {
          Set<T> variableLiveDomain = ImmutableSet.copyOf(liveDomains.get(variable).getLiveValues());
          for (T value : variableLiveDomain) {
            Assignment<T> potentialAssignment = Assignment.of(variable, value);
            if (!constraint.isSupported(potentialAssignment, liveDomains)) {
              // Prune the value as it would violate this constraint, and add variable with pruned domain to frontier.
              liveDomains.get(variable).pruneWithReason(depth, of(value, CONSTRAINT_PROPAGATION));
              prunedVariables.add(variable);
              if (liveDomains.get(variable).isEmpty()) {
                variablesPrunedByDepth.push(prunedVariables);
                for (ConstraintCausedDomainWipeoutListener<T> wipeoutListener : wipeoutListeners) {
                  wipeoutListener.constraintCausedDomainWipeout(constraint);
                }
                return false;
              }
              frontier.add(variable);
            }
          }
        }
      }
    }
    return true;
  }

  public void undoLastPropagation() {
    int depth = variablesPrunedByDepth.size() - 1;
    for (Variable<T> variable : variablesPrunedByDepth.pop()) {
      liveDomains.get(variable).undoPruningsAtDepth(depth);
    }
  }

  void registerConstraintCausedDomainWipeoutListener(ConstraintCausedDomainWipeoutListener<T> listener) {
    wipeoutListeners.add(listener);
  }
}
