package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;

public interface VariableHeuristic<T> {
  Variable<T> nextVariable(Collection<Variable<T>> unassignedVariables);
}
