package com.dropofink.problems;

import com.dropofink.constraints.BinaryConstraint;
import com.dropofink.constraints.BinaryConstraints;
import com.dropofink.domains.IntegerDomain;
import com.dropofink.model.Constraint;
import com.dropofink.model.Problem;
import com.dropofink.model.Value;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NQueensBinary extends Problem {
  private NQueensBinary(List<Variable> variables, Set<Constraint> constraints) {
    super(variables, constraints);
  }

  public static NQueensBinary create(int numberOfQueens) {
    Variable queens[] = new Variable[numberOfQueens];
    IntegerDomain domain = IntegerDomain.ofInclusive(1, numberOfQueens);
    for (int i = 0; i < numberOfQueens; i++) {
      queens[i] = (Variable.create("Q" + (i + 1), domain));
    }
    ImmutableSet.Builder<Constraint> constraintBuilder = ImmutableSet.builder();
    for (int i = 0; i < numberOfQueens; i++) {
      for (int j = i + 1; j < numberOfQueens; j++) {
        constraintBuilder.add(new BinaryConstraints.NotEquals(queens[i], queens[j]));
        constraintBuilder.add(new DiagonalConstraint(queens[i], queens[j], i, j));
      }
    }

    return new NQueensBinary(Arrays.asList(queens), constraintBuilder.build());
  }

  private static class DiagonalConstraint extends BinaryConstraint {
    private final int columnDelta;
    private DiagonalConstraint(Variable q1, Variable q2, int q1Column, int q2Column) {
      super(q1, q2);
      columnDelta = Math.abs(q1Column - q2Column);
    }

    @Override
    public boolean isSatisfied(Value firstValue, Value secondValue) {
      int row1 = ((IntegerDomain.IntegerValue)firstValue).getValue();
      int row2 = ((IntegerDomain.IntegerValue)secondValue).getValue();
      int rowDelta = Math.abs(row1 - row2);
      return rowDelta != columnDelta;
    }
  }
}
