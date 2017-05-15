package com.dropofink.constraints;

import com.dropofink.model.Variable;
import com.google.common.base.Objects;

public class BinaryConstraints {
  public static class NotEquals<T> extends BinaryConstraint<T> {
    public NotEquals(Variable<T> first, Variable<T> second) {
      super(first, second);
    }

    @Override
    public boolean isSatisfied(T firstValue, T secondValue) {
      return !firstValue.equals(secondValue);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(first, second);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null) {
        return false;
      }
      if (o instanceof NotEquals) {
        NotEquals other = (NotEquals) o;
        return other.getVariables().equals(getVariables());
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("%s != %s", first.name(), second.name());
    }
  }
}
