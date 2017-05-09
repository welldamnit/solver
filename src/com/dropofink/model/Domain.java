package com.dropofink.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Domain<T> extends HashSet<T> {
  public static Domain<Integer> integers(int firstValue, int lastValue) {
    Domain<Integer> domain = new Domain<>();
    for (int i = firstValue; i <= lastValue; i++) {
      domain.add(i);
    }
    return domain;
  }
}
