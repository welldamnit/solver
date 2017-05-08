package com.dropofink.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.stream.Collectors;

public class Assignments {
  private static final Set<Value> NO_VALUES = ImmutableSet.of();
  private final HashMap<Variable, Set<Value>> valuesForVariable;

  public Assignments() {
    this.valuesForVariable = new HashMap<>();
  }

  public Assignments(Set<Assignment> assignmentSet) {
    this.valuesForVariable = new HashMap<>();
    for (Assignment assignment : assignmentSet) {
      add(assignment);
    }
  }

  public void add(Assignment assignment) {
    Variable variable = assignment.variable();
    Value value = assignment.value();
    Set<Value> values = valuesForVariable.getOrDefault(variable, new HashSet<>());
    values.add(value);
    valuesForVariable.put(variable, values);
  }

  public void remove(Assignment assignment) {
    Variable variable = assignment.variable();
    Value value = assignment.value();
    Set<Value> values = valuesForVariable.getOrDefault(variable, new HashSet<>());
    values.remove(value);
    if (values.isEmpty()) {
      valuesForVariable.remove(variable);
    } else {
      valuesForVariable.put(variable, values);
    }
  }

  public boolean contains(Assignment assignment) {
    return getAssignedValuesFor(assignment.variable()).contains(assignment.value());
  }

  public boolean isEmpty() {
    return valuesForVariable.entrySet().stream().allMatch(e -> e.getValue().isEmpty());
  }

  public Set<Value> getAssignedValuesFor(Variable variable) {
    return valuesForVariable.getOrDefault(variable, NO_VALUES);
  }

  public Value getAssignedValueFor(Variable variable) {
    return Iterables.getOnlyElement(valuesForVariable.getOrDefault(variable, NO_VALUES), null);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(valuesForVariable);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Assignments) {
      Assignments other = (Assignments) o;
      return other.valuesForVariable.equals(valuesForVariable);
    }
    return false;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(", ");
    for (Variable var : valuesForVariable.keySet().stream().sorted().collect(Collectors.toList())) {
      joiner.add(String.format("%s = %s", var.name(), valuesForVariable.get(var)));
    }
    return joiner.toString();
  }
}
