package com.dropofink.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.stream.Collectors;

public class Assignments<T> {
  private final HashMap<Variable, Set<T>> valuesForVariable;

  public Assignments() {
    this.valuesForVariable = new HashMap<>();
  }

  public Assignments(Set<Assignment<T>> assignmentSet) {
    this.valuesForVariable = new HashMap<>();
    for (Assignment<T> assignment : assignmentSet) {
      add(assignment);
    }
  }

  public void add(Assignment<T> assignment) {
    Variable<T> variable = assignment.variable();
    T value = assignment.value();
    Set<T> values = valuesForVariable.getOrDefault(variable, new HashSet<>());
    values.add(value);
    valuesForVariable.put(variable, values);
  }

  public void remove(Assignment<T> assignment) {
    Variable<T> variable = assignment.variable();
    T value = assignment.value();
    Set<T> values = valuesForVariable.getOrDefault(variable, new HashSet<>());
    values.remove(value);
    if (values.isEmpty()) {
      valuesForVariable.remove(variable);
    } else {
      valuesForVariable.put(variable, values);
    }
  }

  public boolean contains(Assignment<T> assignment) {
    return getAssignedValuesFor(assignment.variable()).contains(assignment.value());
  }

  public boolean isEmpty() {
    return valuesForVariable.entrySet().stream().allMatch(e -> e.getValue().isEmpty());
  }

  public Set<T> getAssignedValuesFor(Variable<T> variable) {
    return valuesForVariable.getOrDefault(variable, ImmutableSet.of());
  }

  public T getAssignedValueFor(Variable<T> variable) {
    return Iterables.getOnlyElement(valuesForVariable.getOrDefault(variable, ImmutableSet.of()), null);
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
