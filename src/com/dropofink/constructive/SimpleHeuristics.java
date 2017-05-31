package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.*;

public class SimpleHeuristics {
  public static class FirstVariable<T> implements VariableHeuristic<T> {
    @Override
    public Variable<T> nextVariable(
        Set<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains) {
      return unassignedVariables.stream().sorted().iterator().next();
    }
  }

  public static class FirstValue<T> implements ValueHeuristic<T> {
    @Override
    public T nextValue(Variable variable, Collection<T> allowedValues) {
      return allowedValues.stream().sorted().iterator().next();
    }
  }

  public static class RandomValue<T> implements ValueHeuristic<T> {
    private final Random random = new Random();

    @Override
    public T nextValue(Variable variable, Collection<T> allowedValues) {
      int index = random.nextInt(allowedValues.size());
      // TODO: This is inefficient when allowedValues is large.
      for (T value : allowedValues) {
        if (index-- == 0) {
          return value;
        }
      }
      throw new IllegalStateException("Not supposed to get here");
    }
  }
  public static class SmallestDomain<T> implements VariableHeuristic<T> {
    @Override
    public Variable<T> nextVariable(
        Set<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains) {
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
