package constraints;

import static com.dropofink.constraints.BinaryConstraints.NotEquals;
import static com.google.common.truth.Truth.assertThat;

import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Domain;
import com.dropofink.model.Variable;
import org.junit.Test;

public class BinaryConstraintsTest {
  @Test
  public void testNotEquals_bothSet() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    NotEquals<Integer> constraint = new NotEquals<>(a, b);

    Assignments<Integer> assignments = new Assignments<>();
    assignments.add(Assignment.of(a, 1));
    assignments.add(Assignment.of(b, 1));
    assertThat(constraint.isSatisfied(assignments)).isFalse();
    assignments.remove(Assignment.of(b, 1));
    assignments.add(Assignment.of(b, 5));
    assertThat(constraint.isSatisfied(assignments)).isTrue();
  }
}
