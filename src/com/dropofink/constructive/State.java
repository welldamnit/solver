package com.dropofink.constructive;

import com.dropofink.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class State {
  private final Problem problem;
  private final Set<Variable> unassignedVariables;
  private Stack<Assignment> assignmentStack = new Stack<>();
  private Stack<Assignments> attemptedAssignments = new Stack<>();

  public State(Problem problem) {
    this.problem = problem;
    this.unassignedVariables = new HashSet<>();
    getUnassignedVariables().addAll(problem.getVariables());

    attemptedAssignments.push(new Assignments());
  }

  public void extend(Assignment assignment) {
    Preconditions.checkArgument(unassignedVariables.contains(assignment.variable()));
    Assignments currentAttemptedAssignments = getCurrentAttemptedAssignments();
    Preconditions.checkArgument(!currentAttemptedAssignments.contains(assignment));
    currentAttemptedAssignments.add(assignment);
    attemptedAssignments.push(new Assignments());
    assignmentStack.push(assignment);
    unassignedVariables.remove(assignment.variable());
  }

  public Assignment unassignLast() {
    Preconditions.checkArgument(!isRoot());
    attemptedAssignments.pop();
    Assignment assignment = assignmentStack.pop();
    unassignedVariables.add(assignment.variable());
    return assignment;
  }

  public boolean isLeaf() {
    return assignmentStack.size() == problem.getVariables().size();
  }
  public boolean isRoot() { return assignmentStack.isEmpty(); }

  public Set<Variable> getUnassignedVariables() {
    return unassignedVariables;
  }

  public Set<Assignment> getAssignmentStack() {
    return ImmutableSet.copyOf(assignmentStack);
  }

  public Assignments getCurrentAttemptedAssignments() {
    return attemptedAssignments.peek();
  }

  // TODO: Constraints should actively monitor state and this set should be maintained incrementally.
  public Assignments getConstraintViolatingPotentialAssignments() {
    Assignments violations = new Assignments();
    Assignments partialState = new Assignments(getAssignmentStack());
    for (Variable variable : unassignedVariables) {
      for (Value value : variable.domain().getValues()) {
        Assignment nextAssignment = Assignment.of(variable, value);
        partialState.add(nextAssignment);
        for (Constraint constraint : problem.getConstraints()) {
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
