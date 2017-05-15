package constructive;

import com.dropofink.constructive.SimpleHeuristics;
import com.dropofink.constructive.State;
import com.dropofink.constructive.VariableHeuristic;
import com.dropofink.model.Assignment;
import com.dropofink.model.Domain;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static com.dropofink.constraints.BinaryConstraints.NotEquals;
import static com.google.common.truth.Truth.assertThat;

public class StateTest {
  private static final Domain<Integer> INT_DOMAIN = Domain.integers(1, 5);
  private static final Variable<Integer> A = Variable.create("a", INT_DOMAIN);
  private static final Variable<Integer> B = Variable.create("b", INT_DOMAIN);
  private static final Variable<Integer> C = Variable.create("c", INT_DOMAIN);

  private State<Integer> state;

  @Before
  public void setUp() {
    Problem<Integer> problem = new Problem<>(
        ImmutableList.of(A, B, C),
        ImmutableSet.of(new NotEquals<>(A, B), new NotEquals<>(B, C), new NotEquals<>(A, C)));
    state = new State<>(problem);
  }

  @Test
  public void unassignLast() {
    state.extend(Assignment.of(A, 3));
    state.extend(Assignment.of(B, 3));
    state.extend(Assignment.failedAttempt(C, 1));
    assertThat(state.unassignLast()).isEqualTo(Assignment.failedAttempt(C, 1));
    assertThat(state.unassignLast()).isEqualTo(Assignment.of(B, 3));
    assertThat(state.unassignLast()).isEqualTo(Assignment.of(A, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotUnassign_atRoot() {
    state.unassignLast();
  }

  @Test
  public void isLeaf() {
    assertThat(state.isLeaf()).isFalse();
    state.extend(Assignment.of(A, 3));
    assertThat(state.isLeaf()).isFalse();
    state.extend(Assignment.of(B, 3));
    assertThat(state.isLeaf()).isFalse();
    state.extend(Assignment.of(C, 3));
    assertThat(state.isLeaf()).isTrue();
    state.unassignLast();
    assertThat(state.isLeaf()).isFalse();
  }

  @Test
  public void isRoot() {
    assertThat(state.isRoot()).isTrue();
    state.extend(Assignment.of(A, 3));
    assertThat(state.isRoot()).isFalse();
    state.extend(Assignment.of(B, 3));
    assertThat(state.isRoot()).isFalse();
    state.extend(Assignment.of(C, 3));
    assertThat(state.isRoot()).isFalse();
    state.unassignLast();
    assertThat(state.isRoot()).isFalse();
    state.unassignLast();
    assertThat(state.isRoot()).isFalse();
    state.unassignLast();
    assertThat(state.isRoot()).isTrue();
  }

  @Test
  public void getUnassignedVariables() {
    VariableHeuristic<Integer> variableHeuristic = new SimpleHeuristics.FirstVariable<>();
    assertThat(state.getUnassignedVariables()).containsExactly(A, B, C);
    state.extend(Assignment.of(A, 3));
    assertThat(state.getUnassignedVariables()).containsExactly(B, C);
    state.extend(Assignment.failedAttempt(B, 1));
    assertThat(state.getUnassignedVariables()).containsExactly(B, C);
    state.extend(Assignment.of(B, 3));
    assertThat(state.getUnassignedVariables()).containsExactly(C);
    state.unassignLast();
    assertThat(state.getUnassignedVariables()).containsExactly(B, C);
    state.unassignLast();
    assertThat(state.getUnassignedVariables()).containsExactly(B, C);
    state.unassignLast();
    assertThat(state.getUnassignedVariables()).containsExactly(A, B, C);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotAssignAlreadyAssignedVariable() {
    state.extend(Assignment.of(A, 3));
    state.extend(Assignment.of(A, 3));
  }
}
