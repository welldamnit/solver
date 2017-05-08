package com.dropofink.constructive;

import com.dropofink.model.Value;
import com.dropofink.model.Variable;

import java.util.Collection;

public interface ValueHeuristic {
  Value nextValue(Variable variable, Collection<Value> allowedValues);
}
