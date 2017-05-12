package com.dropofink.constructive;

import com.dropofink.model.Domain;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public class LiveDomain<T> {
  private final Domain<T> underlyingDomain;
  private final Set<T> prunings;

  public LiveDomain(Domain<T> underlyingDomain) {
    this.underlyingDomain = underlyingDomain;
    prunings = new HashSet<>();
  }

  public void prune(T value) {
    checkArgument(underlyingDomain.contains(value));
    checkArgument(!prunings.contains(value));
    prunings.add(value);
  }

  public void unprune(T value) {
    checkArgument(prunings.contains(value));
    prunings.remove(value);
  }

  public boolean isEmpty() {
    return prunings.size() == underlyingDomain.size();
  }

  public Set<T> getLiveValues() {
    return Sets.difference(underlyingDomain, prunings);
  }

  @Override
  public String toString() {
    return getLiveValues().toString();
  }
}
