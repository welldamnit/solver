package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;

public class SimpleHeuristics {
  public static class FirstVariable<T> implements VariableHeuristic<T> {
    @Override
    public Variable<T> nextVariable(Collection<Variable<T>> unassignedVariables) {
      return unassignedVariables.stream().sorted().iterator().next();
    }
  }

  public static class FirstValue<T> implements ValueHeuristic<T> {
    @Override
    public T nextValue(Variable variable, Collection<T> allowedValues) {
      return allowedValues.stream().sorted().iterator().next();
    }
  }
}
