package model;

import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Domain;
import com.dropofink.model.Variable;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class AssignmentsTest {
  @Test
  public void emptyAssignment() {
    Assignments<Integer> assignments = new Assignments<>();
    Variable<Integer> v = Variable.create("v", Domain.integers(2, 5));
    assertThat(assignments.getAssignedValuesFor(v)).isEmpty();
    assertThat(assignments.getAssignedValueFor(v)).isNull();
  }

  @Test
  public void singleAssignment() {
    Assignments<Integer> assignments = new Assignments<>();
    Variable<Integer> v = Variable.create("v", Domain.integers(1, 5));
    assignments.add(Assignment.of(v, 4));
    assertThat(assignments.getAssignedValuesFor(v)).containsExactly(4);
    assertThat(assignments.getAssignedValueFor(v)).isEqualTo(4);
    assertThat(assignments.contains(Assignment.of(v, 4))).isTrue();
    assertThat(assignments.contains(Assignment.of(v, 1))).isFalse();
  }

  @Test
  public void singleVariable_manyAssignments() {
    Assignments<Integer> assignments = new Assignments<>();
    Variable<Integer> v = Variable.create("v", Domain.integers(1, 5));
    assignments.add(Assignment.of(v, 4));
    assignments.add(Assignment.of(v, 2));
    assertThat(assignments.getAssignedValuesFor(v)).containsExactly(2, 4);
  }

  @Test
  public void manyVariablesAssigned() {
    Assignments<Integer> assignments = new Assignments<>();
    Variable<Integer> v1 = Variable.create("v1", Domain.integers(1, 5));
    Variable<Integer> v2 = Variable.create("v2", Domain.integers(1, 5));
    assignments.add(Assignment.of(v1, 4));
    assignments.add(Assignment.of(v2, 2));
    assignments.add(Assignment.of(v2, 5));
    assertThat(assignments.getAssignedValuesFor(v1)).containsExactly(4);
    assertThat(assignments.getAssignedValuesFor(v2)).containsExactly(2, 5);
  }
}
