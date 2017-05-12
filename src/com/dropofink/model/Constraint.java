package com.dropofink.model;

import com.dropofink.constructive.LiveDomain;

import java.util.Map;
import java.util.Set;

public interface Constraint<T> {
  Set<Variable<T>> getVariables();
  boolean isSatisfied(Assignments<T> assignments);
  boolean isSupported(Assignment<T> assignment, Map<Variable<T>, ? extends LiveDomain<T>> liveDomains);
}
