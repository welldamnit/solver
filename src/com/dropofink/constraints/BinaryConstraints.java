package com.dropofink.constraints;

import com.dropofink.model.Variable;

public class BinaryConstraints {
  public static class NotEquals<T> extends BinaryConstraint<T> {
    public NotEquals(Variable<T> first, Variable<T> second) {
      super(first, second);
    }

    @Override
    public boolean isSatisfied(T firstValue, T secondValue) {
      return !firstValue.equals(secondValue);
    }
  }
}
