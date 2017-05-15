package com.dropofink.constructive;

import com.dropofink.model.Assignment;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class State<T> {
  private final Set<Variable<T>> unassignedVariables;
  private Stack<Assignment<T>> assignmentStack = new Stack<>();

  public State(Problem<T> problem) {
    this.unassignedVariables = new HashSet<>();
    unassignedVariables.addAll(problem.getVariables());
  }

  public void extend(Assignment<T> assignment) {
    Preconditions.checkArgument(assignment.isFailedAttempt() || unassignedVariables.contains(assignment.variable()));

    assignmentStack.push(assignment);
    if (!assignment.isFailedAttempt()) {
      unassignedVariables.remove(assignment.variable());
    }
  }

  public Assignment<T> unassignLast() {
    Preconditions.checkArgument(!isRoot());
    Assignment<T> assignment = assignmentStack.pop();
    if (!assignment.isFailedAttempt()) {
      unassignedVariables.add(assignment.variable());
    }
    return assignment;
  }

  public boolean isLeaf() {
    return unassignedVariables.isEmpty();
  }
  public boolean isRoot() { return assignmentStack.isEmpty(); }

  Set<Assignment<T>> getAssignmentStack() {
    return assignmentStack.stream().filter(e -> !e.isFailedAttempt()).collect(Collectors.toSet());
  }

  public Set<Variable<T>> getUnassignedVariables() {
    return unassignedVariables;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(", ");
    for (Assignment<T> assignment : assignmentStack) {
      if (!assignment.isFailedAttempt()) {
        joiner.add(String.format("%s = %s", assignment.variable().name(), assignment.value()));
      }
    }
    return joiner.toString();
  }
}
