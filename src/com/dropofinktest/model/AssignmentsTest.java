package model;

import com.dropofink.domains.IntegerDomain;
import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Variable;
import org.junit.Test;

import static com.dropofink.domains.IntegerDomain.IntegerValue.of;
import static com.google.common.truth.Truth.assertThat;

public class AssignmentsTest {
  @Test
  public void emptyAssignment() {
    Assignments assignments = new Assignments();
    Variable v = Variable.create("v", IntegerDomain.ofInclusive(1, 5));
    assertThat(assignments.getAssignedValuesFor(v)).isEmpty();
    assertThat(assignments.getAssignedValueFor(v)).isNull();
  }

  @Test
  public void singleAssignment() {
    Assignments assignments = new Assignments();
    Variable v = Variable.create("v", IntegerDomain.ofInclusive(1, 5));
    assignments.add(Assignment.of(v, of(4)));
    assertThat(assignments.getAssignedValuesFor(v)).containsExactly(of(4));
    assertThat(assignments.getAssignedValueFor(v)).isEqualTo(of(4));
    assertThat(assignments.contains(Assignment.of(v, of(4)))).isTrue();
    assertThat(assignments.contains(Assignment.of(v, of(1)))).isFalse();
  }

  @Test
  public void singleVariable_manyAssignments() {
    Assignments assignments = new Assignments();
    Variable v = Variable.create("v", IntegerDomain.ofInclusive(1, 5));
    assignments.add(Assignment.of(v, of(4)));
    assignments.add(Assignment.of(v, of(2)));
    assertThat(assignments.getAssignedValuesFor(v)).containsExactly(of(2), of(4));
  }

  @Test
  public void manyVariablesAssigned() {
    Assignments assignments = new Assignments();
    Variable v1 = Variable.create("v1", IntegerDomain.ofInclusive(1, 5));
    Variable v2 = Variable.create("v2", IntegerDomain.ofInclusive(1, 5));
    assignments.add(Assignment.of(v1, of(4)));
    assignments.add(Assignment.of(v2, of(2)));
    assignments.add(Assignment.of(v2, of(5)));
    assertThat(assignments.getAssignedValuesFor(v1)).containsExactly(of(4));
    assertThat(assignments.getAssignedValuesFor(v2)).containsExactly(of(2), of(5));
  }
}
