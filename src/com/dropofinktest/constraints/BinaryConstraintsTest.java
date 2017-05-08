package constraints;

import static com.dropofink.constraints.BinaryConstraints.NotEquals;
import static com.dropofink.domains.IntegerDomain.IntegerValue;
import static com.dropofink.domains.IntegerDomain.IntegerValue.of;
import static com.google.common.truth.Truth.assertThat;

import com.dropofink.constraints.BinaryConstraints;
import com.dropofink.domains.IntegerDomain;
import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Value;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class BinaryConstraintsTest {
  @Test
  public void testNotEquals_bothSet() {
    Variable a = Variable.create("a", IntegerDomain.ofInclusive(1, 5));
    Variable b = Variable.create("b", IntegerDomain.ofInclusive(1, 5));
    NotEquals constraint = new NotEquals(a, b);

    Assignments assignments = new Assignments();
    assignments.add(Assignment.of(a, of(1)));
    assignments.add(Assignment.of(b, of(1)));
    assertThat(constraint.isSatisfied(assignments)).isFalse();
    assignments.remove(Assignment.of(b, of(1)));
    assignments.add(Assignment.of(b, of(5)));
    assertThat(constraint.isSatisfied(assignments)).isTrue();
  }
}
