package constraints;

import com.dropofink.constraints.SumConstraint;
import com.dropofink.constructive.LiveDomain;
import com.dropofink.model.Assignment;
import com.dropofink.model.Assignments;
import com.dropofink.model.Domain;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class SumConstraintTest {
  @Test
  public void isSatisfied() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    Variable<Integer> c = Variable.create("c", Domain.integers(1, 5));
    SumConstraint constraint = new SumConstraint(ImmutableSet.of(a, b, c), 10);
    Assignments<Integer> assignments = new Assignments<>();
    assertThat(constraint.isSatisfied(assignments)).isTrue();
    assignments.add(Assignment.of(a, 5));
    assertThat(constraint.isSatisfied(assignments)).isTrue();
    assignments.add(Assignment.of(b, 5));
    assertThat(constraint.isSatisfied(assignments)).isFalse();
    assignments.remove(Assignment.of(b, 5));
    assignments.add(Assignment.of(b, 4));
    assertThat(constraint.isSatisfied(assignments)).isTrue();
    assignments.add(Assignment.of(c, 4));
    assertThat(constraint.isSatisfied(assignments)).isFalse();
    assignments.remove(Assignment.of(c, 4));
    assignments.add(Assignment.of(c, 1));
    assertThat(constraint.isSatisfied(assignments)).isTrue();
  }

  @Test
  public void isSatisfied_withMin() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    Variable<Integer> c = Variable.create("c", Domain.integers(1, 5));
    SumConstraint constraint = new SumConstraint(ImmutableSet.of(a, b, c), 2);
    Assignments<Integer> assignments = new Assignments<>();
    assertThat(constraint.isSatisfied(assignments)).isFalse();
  }

  @Test
  public void isSatisfied_withMax() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    Variable<Integer> c = Variable.create("c", Domain.integers(1, 5));
    SumConstraint constraint = new SumConstraint(ImmutableSet.of(a, b, c), 25);
    Assignments<Integer> assignments = new Assignments<>();
    assertThat(constraint.isSatisfied(assignments)).isFalse();
  }

  @Test
  public void isSupported() {
    Variable<Integer> a = Variable.create("a", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("b", Domain.integers(1, 5));
    Variable<Integer> c = Variable.create("c", Domain.integers(1, 5));
    SumConstraint constraint = new SumConstraint(ImmutableSet.of(a, b, c), 10);
    Map<Variable<Integer>, LiveDomain<Integer>> liveDomains = new HashMap<>();
    LiveDomain<Integer> liveDomainA = new LiveDomain<>(a.domain());
    LiveDomain<Integer> liveDomainB = new LiveDomain<>(b.domain());
    LiveDomain<Integer> liveDomainC = new LiveDomain<>(c.domain());
    liveDomains.put(a, liveDomainA);
    liveDomains.put(b, liveDomainB);
    liveDomains.put(c, liveDomainC);
    assertThat(constraint.isSupported(Assignment.of(a, 5), liveDomains)).isTrue();

    liveDomainB.pruneToSingleValue(5);
    liveDomainC.pruneToSingleValue(1);
    assertThat(constraint.isSupported(Assignment.of(a, 5), liveDomains)).isFalse();
    assertThat(constraint.isSupported(Assignment.of(a, 4), liveDomains)).isTrue();
    liveDomainB.unpruneSingleValue();
    liveDomainB.prune(1);
    liveDomainB.prune(2);
    liveDomainB.prune(3);
    assertThat(constraint.isSupported(Assignment.of(a, 5), liveDomains)).isTrue();
  }
}
