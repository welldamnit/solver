package com.dropofink.constructive;

import com.dropofink.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class State<T> {
  private final Problem<T> problem;
  private final Set<Variable<T>> unassignedVariables;
  private Stack<Assignment<T>> assignmentStack = new Stack<>();
  private Stack<Assignments<T>> attemptedAssignments = new Stack<>();

  public State(Problem<T> problem) {
    this.problem = problem;
    this.unassignedVariables = new HashSet<>();
    getUnassignedVariables().addAll(problem.getVariables());

    attemptedAssignments.push(new Assignments<>());
  }

  public void extend(Assignment<T> assignment) {
    Preconditions.checkArgument(unassignedVariables.contains(assignment.variable()));
    Assignments<T> currentAttemptedAssignments = getCurrentAttemptedAssignments();
    Preconditions.checkArgument(!currentAttemptedAssignments.contains(assignment));
    currentAttemptedAssignments.add(assignment);
    attemptedAssignments.push(new Assignments<>());
    assignmentStack.push(assignment);
    unassignedVariables.remove(assignment.variable());
  }

  public Assignment<T> unassignLast() {
    Preconditions.checkArgument(!isRoot());
    attemptedAssignments.pop();
    Assignment<T> assignment = assignmentStack.pop();
    unassignedVariables.add(assignment.variable());
    return assignment;
  }

  public boolean isLeaf() {
    return assignmentStack.size() == problem.getVariables().size();
  }
  public boolean isRoot() { return assignmentStack.isEmpty(); }

  public Set<Variable<T>> getUnassignedVariables() {
    return unassignedVariables;
  }

  Set<Assignment<T>> getAssignmentStack() {
    return ImmutableSet.copyOf(assignmentStack);
  }

  public Assignments<T> getCurrentAttemptedAssignments() {
    return attemptedAssignments.peek();
  }

  // TODO: Constraints should actively monitor state and this set should be maintained incrementally.
  Assignments<T> getConstraintViolatingPotentialAssignments() {
    Assignments<T> violations = new Assignments<>();
    Assignments<T> partialState = new Assignments<>(getAssignmentStack());
    for (Variable<T> variable : unassignedVariables) {
      for (T value : variable.domain()) {
        Assignment<T> nextAssignment = Assignment.of(variable, value);
        partialState.add(nextAssignment);
        for (Constraint<T> constraint : problem.getConstraints()) {
          if (!constraint.isSatisfied(partialState)) {
            violations.add(nextAssignment);
          }
        }
        partialState.remove(nextAssignment);
      }
    }
    return violations;
  }
}
