package com.dropofink.constructive;

import com.dropofink.model.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.util.*;

import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.ASSIGNMENT;
import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.CONSTRAINT_PROPAGATION;
import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.FAILED_ATTEMPT;
import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.of;
import static com.google.common.collect.ImmutableSet.copyOf;

public class ConstraintPropagator<T> {
  private final Multimap<Variable<T>, Constraint<T>> variableToConstraints;
  private final Map<Variable<T>, LiveDomainWithContext<T>> liveDomains;
  private final Stack<Set<Variable<T>>> variablesPrunedByDepth;

  public ConstraintPropagator(Problem<T> problem) {
    this.variableToConstraints = HashMultimap.create();
    this.liveDomains = new HashMap<>();
    this.variablesPrunedByDepth = new Stack<>();

    for (Constraint<T> constraint : problem.getConstraints()) {
      for (Variable<T>  constraintVariable : constraint.getVariables()) {
        variableToConstraints.put(constraintVariable, constraint);
      }
    }

    for (Variable<T> variable : problem.getVariables()) {
      liveDomains.put(variable, new LiveDomainWithContext<>(variable.domain()));
    }
  }

  /** @return true if a solution still may exist after this assignment, false if definitely no solutions exist. */
  public boolean propagateAssignment(Assignment<T> assignment) {
    int depth = variablesPrunedByDepth.size();
    Set<Variable<T>> pruned = new HashSet<>();
    if (assignment.isFailedAttempt()) {
      liveDomains.get(assignment.variable()).pruneWithReason(depth, of(assignment.value(), FAILED_ATTEMPT));
    } else {
      liveDomains.get(assignment.variable()).pruneWithReason(depth, of(assignment.value(), ASSIGNMENT));
    }
    pruned.add(assignment.variable());

    // This is the arc consistency algorithm.
    Set<Variable<T>> frontier = new HashSet<>();
    frontier.add(assignment.variable());
    while (!frontier.isEmpty()) {
      Variable<T> variable = frontier.iterator().next();
      frontier.remove(variable);
      for (Constraint<T> affectedConstraint : variableToConstraints.get(variable)) {
        for (Variable<T> constraintVariable : affectedConstraint.getVariables()) {
          if (!constraintVariable.equals(variable)) {
            Set<T> constraintVariableLiveDomain =
                ImmutableSet.copyOf(liveDomains.get(constraintVariable).getLiveValues());
            for (T value : constraintVariableLiveDomain) {
              Assignment<T> potentialAssignment = Assignment.of(constraintVariable, value);
              if (!affectedConstraint.isSupported(potentialAssignment, liveDomains)) {
                // Prune the value as it would violate this constraint, and add variable with pruned domain to frontier.
                liveDomains.get(constraintVariable).pruneWithReason(depth, of(value, CONSTRAINT_PROPAGATION));
                pruned.add(constraintVariable);
                if (liveDomains.get(constraintVariable).isEmpty()) {
                  variablesPrunedByDepth.push(pruned);
                  return false;
                }
                frontier.add(constraintVariable);
              }
            }
          }
        }
      }
    }
    variablesPrunedByDepth.push(pruned);
    return true;
  }

  public void undoLastPropagation() {
    int depth = variablesPrunedByDepth.size() - 1;
    for (Variable<T> variable : variablesPrunedByDepth.pop()) {
      liveDomains.get(variable).undoPruningsAtDepth(depth);
    }
  }

  public Set<T> getLiveValues(Variable<T> variable) {
    return liveDomains.get(variable).getLiveValues();
  }
}
