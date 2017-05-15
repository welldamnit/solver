package problems;

import com.dropofink.model.Domain;
import com.dropofink.model.Variable;
import com.dropofink.problems.MagicSquareBinaryNotEquals;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class MagicSquareBinaryNotEqualsTest {
  private static final Variable<Integer> V11 = Variable.create("V11", Domain.integers(1, 9));
  private static final Variable<Integer> V12 = Variable.create("V12", Domain.integers(1, 9));
  private static final Variable<Integer> V13 = Variable.create("V13", Domain.integers(1, 9));
  private static final Variable<Integer> V21 = Variable.create("V21", Domain.integers(1, 9));
  private static final Variable<Integer> V22 = Variable.create("V22", Domain.integers(1, 9));
  private static final Variable<Integer> V23 = Variable.create("V23", Domain.integers(1, 9));
  private static final Variable<Integer> V31 = Variable.create("V31", Domain.integers(1, 9));
  private static final Variable<Integer> V32 = Variable.create("V32", Domain.integers(1, 9));
  private static final Variable<Integer> V33 = Variable.create("V33", Domain.integers(1, 9));
  private static final List<Variable<Integer>> SQUARE = ImmutableList.of(
      V11, V12, V13,
      V21, V22, V23,
      V31, V32, V33);

  @Test
  public void getRowVariables() {
    Set<Variable<Integer>> secondRow = MagicSquareBinaryNotEquals.getRowVariables(3, 1, SQUARE);
    assertThat(secondRow).containsExactly(V21, V22, V23);
  }

  @Test
  public void getColumnVariables() {
    Set<Variable<Integer>> secondColumn = MagicSquareBinaryNotEquals.getColumnVariables(3, 1, SQUARE);
    assertThat(secondColumn).containsExactly(V12, V22, V32);
  }

  @Test
  public void getDiagonal1Variables() {
    Set<Variable<Integer>> diagonal1 = MagicSquareBinaryNotEquals.getDiagonal1Variables(3, SQUARE);
    assertThat(diagonal1).containsExactly(V11, V22, V33);
  }

  @Test
  public void getDiagonal2Variables() {
    Set<Variable<Integer>> diagonal2 = MagicSquareBinaryNotEquals.getDiagonal2Variables(3, SQUARE);
    assertThat(diagonal2).containsExactly(V13, V22, V31);
  }
}
