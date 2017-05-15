package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;
import java.util.Map;

public class SimpleHeuristics {
  public static class FirstVariable<T> implements VariableHeuristic<T> {
    @Override
    public Variable<T> nextVariable(
        Collection<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains) {
      return unassignedVariables.stream().sorted().iterator().next();
    }
  }

  public static class FirstValue<T> implements ValueHeuristic<T> {
    @Override
    public T nextValue(Variable variable, Collection<T> allowedValues) {
      return allowedValues.stream().sorted().iterator().next();
    }
  }

  public static class SmallestDomain<T> implements VariableHeuristic<T> {
    @Override
    public Variable<T> nextVariable(
        Collection<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains) {
      int smallestDomainSize = Integer.MAX_VALUE;
      Variable<T> smallestDomainVariable = null;
      for (Variable<T> variable : unassignedVariables) {
        int domainSize = liveDomains.get(variable).size();
        if (domainSize < smallestDomainSize) {
          smallestDomainSize = domainSize;
          smallestDomainVariable = variable;
        }
      }
      return smallestDomainVariable;
    }
  }
}
