package constraints;

import com.dropofink.constructive.LiveDomain;
import com.dropofink.model.Assignments;
import com.dropofink.model.Domain;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static com.dropofink.constraints.BinaryConstraints.NotEquals;
import static com.dropofink.model.Assignment.of;
import static com.google.common.truth.Truth.assertThat;

public class BinaryConstraintsTest {
  @Test
  public void testNotEquals_bothSet() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    NotEquals<Integer> constraint = new NotEquals<>(a, b);

    Assignments<Integer> assignments = new Assignments<>();
    assignments.add(of(a, 1));
    assignments.add(of(b, 1));
    assertThat(constraint.isSatisfied(assignments)).isFalse();
    assignments.remove(of(b, 1));
    assignments.add(of(b, 5));
    assertThat(constraint.isSatisfied(assignments)).isTrue();
  }

  @Test
  public void testIsSupported_firstSet() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    NotEquals<Integer> constraint = new NotEquals<>(a, b);

    LiveDomain<Integer> liveDomainB = new LiveDomain<>(Domain.integers(1, 5));
    assertThat(constraint.isSupported(of(a, 1), ImmutableMap.of(b, liveDomainB))).isTrue();
    liveDomainB.prune(2);
    liveDomainB.prune(3);
    liveDomainB.prune(4);
    assertThat(constraint.isSupported(of(a, 1), ImmutableMap.of(b, liveDomainB))).isTrue();
    liveDomainB.prune(5);
    assertThat(constraint.isSupported(of(a, 1), ImmutableMap.of(b, liveDomainB))).isFalse();
  }

  @Test
  public void testIsSupported_secondSet() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    NotEquals<Integer> constraint = new NotEquals<>(a, b);

    LiveDomain<Integer> liveDomainA = new LiveDomain<>(Domain.integers(1, 5));
    assertThat(constraint.isSupported(of(b, 1), ImmutableMap.of(a, liveDomainA))).isTrue();
    liveDomainA.prune(2);
    liveDomainA.prune(3);
    liveDomainA.prune(4);
    assertThat(constraint.isSupported(of(b, 1), ImmutableMap.of(a, liveDomainA))).isTrue();
    liveDomainA.prune(5);
    assertThat(constraint.isSupported(of(b, 1), ImmutableMap.of(a, liveDomainA))).isFalse();
  }
}
