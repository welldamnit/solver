package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;
import java.util.Map;

public interface VariableHeuristic<T> {
  Variable<T> nextVariable(
      Collection<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains);
}
