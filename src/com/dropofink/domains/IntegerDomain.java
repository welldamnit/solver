package com.dropofink.domains;

import com.dropofink.model.Domain;
import com.dropofink.model.Value;
import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.*;

public class IntegerDomain extends Domain {
  private final Set<Value> values;

  @Override
  public Set<Value> getValues() {
    return values;
  }

  private IntegerDomain(int lowerBound, int upperBound) {
    ImmutableSet.Builder<Value> valueBuilder  = new ImmutableSet.Builder();
    for (int i = lowerBound; i <= upperBound; i++) {
      valueBuilder.add(new IntegerValue(i));
    }
    this.values = valueBuilder.build();
  }

  public static IntegerDomain ofInclusive(int lowerBound, int upperBound) {
    return new IntegerDomain(lowerBound, upperBound);
  }

  public static class IntegerValue extends Value<Integer> {
    private final int value;

    private IntegerValue(int value) {
      this.value = value;
    }

    public static IntegerValue of(int value) {
      return new IntegerValue(value);
    }

    @Override
    public Integer getValue() {
      return value;
    }

    @Override
    public int compareTo(Value<Integer> o) {
      return Integer.compare(value, o.getValue());
    }
  }
}
