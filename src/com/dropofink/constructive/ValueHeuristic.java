package com.dropofink.constructive;

import com.dropofink.model.Variable;

import java.util.Collection;

public interface ValueHeuristic<T> {
  T nextValue(Variable variable, Collection<T> allowedValues);
}
