package com.dropofink.problems;

import com.dropofink.constraints.BinaryConstraints;
import com.dropofink.constraints.SumConstraint;
import com.dropofink.model.*;
import com.google.common.collect.ImmutableSet;

import java.util.*;

public class MagicSquareBinaryNotEquals extends Problem<Integer> {
  private final int sideSize;
  private MagicSquareBinaryNotEquals(
      int sideSize, List<Variable<Integer>> variables, Set<Constraint<Integer>> constraints) {
    super(variables, constraints);
    this.sideSize = sideSize;
  }

  public static MagicSquareBinaryNotEquals create(int sideSize) {
    int sum = sideSize * (sideSize * sideSize + 1) / 2;
    int numberOfSquares = sideSize * sideSize;
    List<Variable<Integer>> squares = new ArrayList<>();
    Domain<Integer> domain = Domain.integers(1, numberOfSquares);
    for (int i = 0; i < numberOfSquares; i++) {
      final Variable<Integer> square = Variable.create("S" + (i + 1), domain);
      squares.add(square);
    }
    ImmutableSet.Builder<Constraint<Integer>> constraintBuilder = ImmutableSet.builder();
    for (int i = 0; i < numberOfSquares; i++) {
      for (int j = i + 1; j < numberOfSquares; j++) {
        constraintBuilder.add(new BinaryConstraints.NotEquals<>(squares.get(i), squares.get(j)));
      }
    }

    for (int i = 0; i < sideSize; i++) {
      constraintBuilder.add(new SumConstraint(getRowVariables(sideSize, i, squares), sum));
      constraintBuilder.add(new SumConstraint(getColumnVariables(sideSize, i, squares), sum));
    }
    constraintBuilder.add(new SumConstraint(getDiagonal1Variables(sideSize, squares), sum));
    constraintBuilder.add(new SumConstraint(getDiagonal2Variables(sideSize, squares), sum));

    return new MagicSquareBinaryNotEquals(sideSize, squares, constraintBuilder.build());
  }

  public static <T> Set<Variable<T>> getRowVariables(int rowLength, int column, List<Variable<T>> allVariables) {
    Set<Variable<T>> rowVariables = new HashSet<>();
    for (int i = 0; i < rowLength; i++) {
      int cell = column * rowLength + i;
      rowVariables.add(allVariables.get(cell));
    }
    return rowVariables;
  }

  public static <T> Set<Variable<T>> getColumnVariables(int colLength, int row, List<Variable<T>> allVariables) {
    Set<Variable<T>> columnVariables = new HashSet<>();
    for (int i = 0; i < colLength; i++) {
      int cell = i * colLength + row;
      columnVariables.add(allVariables.get(cell));
    }
    return columnVariables;
  }

  public static <T> Set<Variable<T>> getDiagonal1Variables(int sideSize, List<Variable<T>> allVariables) {
    Set<Variable<T>> diagonal1Variables = new HashSet<>();
    for (int i = 0; i < sideSize; i++) {
      int cell = i * sideSize + i;
      diagonal1Variables.add(allVariables.get(cell));
    }
    return diagonal1Variables;
  }

  public static <T> Set<Variable<T>> getDiagonal2Variables(int sideSize, List<Variable<T>> allVariables) {
    Set<Variable<T>> diagonal2Variables = new HashSet<>();
    for (int i = 0; i < sideSize; i++) {
      int cell = i * sideSize + (sideSize - i - 1);
      diagonal2Variables.add(allVariables.get(cell));
    }
    return diagonal2Variables;
  }

  public String solutionToString(Assignments<Integer> solution) {
    int maxCellLength = ("" + sideSize*sideSize).length();
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < sideSize; row++) {
      StringJoiner joiner = new StringJoiner(" ");
      for (int column = 0; column < sideSize; column++) {
        Variable<Integer> variable = getVariables().get(row*sideSize + column);
        String entry = solution.getAssignedValueFor(variable).toString();
        String padding = "                ".substring(0, maxCellLength - entry.length());
        joiner.add(padding + entry);
      }
      sb.append(joiner);
      sb.append("\n");
    }
    return sb.toString();
  }
}
