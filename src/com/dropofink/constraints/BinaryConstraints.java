package com.dropofink.constraints;

import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Value;
import com.dropofink.model.Variable;

import java.util.Map;
import java.util.Set;

public class BinaryConstraints {
  public static class NotEquals extends BinaryConstraint {
    public NotEquals(Variable first, Variable second) {
      super(first, second);
    }

    @Override
    public boolean isSatisfied(Value firstValue, Value secondValue) {
      return !firstValue.equals(secondValue);
    }
  }
}
