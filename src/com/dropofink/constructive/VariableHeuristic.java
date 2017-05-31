package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface VariableHeuristic<T> {
  Variable<T> nextVariable(
      Set<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains);
}
