package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;

public interface VariableHeuristic {
  Variable nextVariable(Collection<Variable> unassignedVariables);
}
