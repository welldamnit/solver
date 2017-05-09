package com.dropofink.problems;

import com.dropofink.constraints.BinaryConstraint;
import com.dropofink.constraints.BinaryConstraints;
import com.dropofink.model.Constraint;
import com.dropofink.model.Domain;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NQueensBinary extends Problem<Integer> {
  private NQueensBinary(List<Variable<Integer>> variables, Set<Constraint<Integer>> constraints) {
    super(variables, constraints);
  }

  public static NQueensBinary create(int numberOfQueens) {
    List<Variable<Integer>> queens = new ArrayList<>();
    Domain<Integer> domain = Domain.integers(1, numberOfQueens);
    for (int i = 0; i < numberOfQueens; i++) {
      final Variable<Integer> queen = Variable.create("Q" + (i + 1), domain);
      queens.add(queen);
    }
    ImmutableSet.Builder<Constraint<Integer>> constraintBuilder = ImmutableSet.builder();
    for (int i = 0; i < numberOfQueens; i++) {
      for (int j = i + 1; j < numberOfQueens; j++) {
        constraintBuilder.add(new BinaryConstraints.NotEquals<>(queens.get(i), queens.get(j)));
        constraintBuilder.add(new DiagonalConstraint(queens.get(i), queens.get(j), i, j));
      }
    }

    return new NQueensBinary(queens, constraintBuilder.build());
  }

  private static class DiagonalConstraint extends BinaryConstraint<Integer> {
    private final int columnDelta;
    private DiagonalConstraint(Variable<Integer> q1, Variable<Integer> q2, int q1Column, int q2Column) {
      super(q1, q2);
      columnDelta = Math.abs(q1Column - q2Column);
    }

    @Override
    public boolean isSatisfied(Integer row1, Integer row2) {
      int rowDelta = Math.abs(row1 - row2);
      return rowDelta != columnDelta;
    }
  }
}
