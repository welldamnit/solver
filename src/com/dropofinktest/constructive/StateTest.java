package constructive;

import com.dropofink.constructive.State;
import com.dropofink.domains.IntegerDomain;
import com.dropofink.model.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static com.dropofink.constraints.BinaryConstraints.NotEquals;
import static com.dropofink.domains.IntegerDomain.IntegerValue.*;
import static com.google.common.truth.Truth.assertThat;

public class StateTest {
  private static final Domain INT_DOMAIN = IntegerDomain.ofInclusive(1, 5);
  private static final Variable A = Variable.create("a", INT_DOMAIN);
  private static final Variable B = Variable.create("b", INT_DOMAIN);
  private static final Variable C = Variable.create("c", INT_DOMAIN);

  private State state;

  @Before
  public void setUp() {
    Problem problem = new Problem(
        ImmutableList.of(A, B, C),
        ImmutableSet.of(new NotEquals(A, B), new NotEquals(B, C), new NotEquals(A, C)));
    state = new State(problem);
  }

  @Test
  public void unassignLast() {
    state.extend(Assignment.of(A, of(3)));
    state.extend(Assignment.of(B, of(3)));
    assertThat(state.unassignLast()).isEqualTo(Assignment.of(B, of(3)));
    assertThat(state.unassignLast()).isEqualTo(Assignment.of(A, of(3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotUnassign_atRoot() {
    state.unassignLast();
  }

  @Test
  public void isLeaf() {
    assertThat(state.isLeaf()).isFalse();
    state.extend(Assignment.of(A, of(3)));
    assertThat(state.isLeaf()).isFalse();
    state.extend(Assignment.of(B, of(3)));
    assertThat(state.isLeaf()).isFalse();
    state.extend(Assignment.of(C, of(3)));
    assertThat(state.isLeaf()).isTrue();
    state.unassignLast();
    assertThat(state.isLeaf()).isFalse();
  }

  @Test
  public void isRoot() {
    assertThat(state.isRoot()).isTrue();
    state.extend(Assignment.of(A, of(3)));
    assertThat(state.isRoot()).isFalse();
    state.extend(Assignment.of(B, of(3)));
    assertThat(state.isRoot()).isFalse();
    state.extend(Assignment.of(C, of(3)));
    assertThat(state.isRoot()).isFalse();
    state.unassignLast();
    assertThat(state.isRoot()).isFalse();
    state.unassignLast();
    assertThat(state.isRoot()).isFalse();
    state.unassignLast();
    assertThat(state.isRoot()).isTrue();
  }

  @Test
  public void unassignedVariables() {
    assertThat(state.getUnassignedVariables()).containsExactly(A, B, C);
    state.extend(Assignment.of(A, of(3)));
    assertThat(state.getUnassignedVariables()).containsExactly(B, C);
    state.extend(Assignment.of(C, of(3)));
    assertThat(state.getUnassignedVariables()).containsExactly(B);
    state.unassignLast();
    assertThat(state.getUnassignedVariables()).containsExactly(B, C);
    state.unassignLast();
    assertThat(state.getUnassignedVariables()).containsExactly(A, B, C);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotAssignAlreadyAssignedVariable() {
    state.extend(Assignment.of(A, of(3)));
    state.extend(Assignment.of(A, of(3)));
  }

  @Test
  public void currentAttemptedAssignments() {
    assertThat(state.getCurrentAttemptedAssignments().isEmpty()).isTrue();
    state.extend(Assignment.of(A, of(3)));
    assertThat(state.getCurrentAttemptedAssignments().isEmpty()).isTrue();
    state.unassignLast();

    Assignments expectedAssignments = new Assignments();
    expectedAssignments.add(Assignment.of(A, of(3)));
    assertThat(state.getCurrentAttemptedAssignments()).isEqualTo(expectedAssignments);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotExtend_withAlreadyAttempedAssignment() {
    state.extend(Assignment.of(A, of(3)));
    state.unassignLast();
    state.extend(Assignment.of(A, of(3)));
  }
}
