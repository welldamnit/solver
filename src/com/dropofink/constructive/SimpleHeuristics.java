package com.dropofink.constructive;

import com.dropofink.model.Value;
import com.dropofink.model.Variable;

import java.util.Collection;

public class SimpleHeuristics {
  public static class FirstVariable implements VariableHeuristic {
    @Override
    public Variable nextVariable(Collection<Variable> unassignedVariables) {
      return unassignedVariables.iterator().next();
    }
  }

  public static class FirstValue implements ValueHeuristic {
    @Override
    public Value nextValue(Variable variable, Collection<Value> allowedValues) {
      return allowedValues.iterator().next();
    }
  }
}
