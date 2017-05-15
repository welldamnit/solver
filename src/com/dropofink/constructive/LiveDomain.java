package com.dropofink.constructive;

import com.dropofink.model.Domain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public class LiveDomain<T> {
  private final Domain<T> underlyingDomain;
  private final Set<T> prunings;
  private Optional<T> singleValue;

  public LiveDomain(Domain<T> underlyingDomain) {
    this.underlyingDomain = underlyingDomain;
    this.prunings = new HashSet<>();
    this.singleValue = Optional.empty();
  }

  public void prune(T value) {
    checkArgument(!singleValue.isPresent() || singleValue.get().equals(value));
    checkArgument(underlyingDomain.contains(value));
    checkArgument(!prunings.contains(value));
    prunings.add(value);
  }

  public void unprune(T value) {
    checkArgument(!singleValue.isPresent() || singleValue.get().equals(value));
    checkArgument(prunings.contains(value));
    prunings.remove(value);
  }

  public void pruneToSingleValue(T value) {
    checkArgument(!prunings.contains(value));
    this.singleValue = Optional.of(value);
  }

  public void unpruneSingleValue() {
    this.singleValue = Optional.empty();
  }

  public boolean isEmpty() {
    return prunings.size() == underlyingDomain.size();
  }

  int size() {
    if (singleValue.isPresent()) {
      return prunings.contains(singleValue.get()) ? 0 : 1;
    }
    return underlyingDomain.size() - prunings.size();
  }

  public Set<T> getLiveValues() {
    if (singleValue.isPresent()) {
      final T value = singleValue.get();
      return prunings.contains(value) ? ImmutableSet.of() : ImmutableSet.of(value);
    } else {
      return Sets.difference(underlyingDomain, prunings);
    }
  }

  @Override
  public String toString() {
    return getLiveValues().toString();
  }
}
