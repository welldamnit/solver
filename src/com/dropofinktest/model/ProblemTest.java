package model;

import com.dropofink.constraints.BinaryConstraints.NotEquals;
import com.dropofink.model.Domain;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ProblemTest {
  @Test
  public void getConstraintsFor() {
    Variable<Integer> a = Variable.create("A", Domain.integers(1, 2));
    Variable<Integer> b = Variable.create("B", Domain.integers(1, 2));
    Variable<Integer> c = Variable.create("C", Domain.integers(1, 2));
    Problem<Integer> problem = new Problem<Integer>(
        ImmutableList.of(a, b, c),
        ImmutableSet.of(new NotEquals<>(a, b), new NotEquals<>(a, c), new NotEquals<>(b, c))) {};
    assertThat(problem.getConstraintsFor(a)).containsExactly(new NotEquals<>(a, b), new NotEquals<>(a, c));
  }
}
