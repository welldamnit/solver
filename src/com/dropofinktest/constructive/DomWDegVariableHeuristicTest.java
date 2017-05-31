package constructive;

import com.dropofink.constraints.BinaryConstraints.NotEquals;
import com.dropofink.constructive.DomWDegVariableHeuristic;
import com.dropofink.constructive.LiveDomainWithContext;
import com.dropofink.model.Domain;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.*;
import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.FAILED_ATTEMPT;
import static com.google.common.truth.Truth.assertThat;

public class DomWDegVariableHeuristicTest {
  @Test
  public void variable_withSmallestDomainChosen() {
    Variable<Integer> a = Variable.create("A", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("B", Domain.integers(1, 4));
    Variable<Integer> c = Variable.create("C", Domain.integers(1, 3));
    Map<Variable<Integer>, LiveDomainWithContext<Integer>> liveDomains = new HashMap<>();
    LiveDomainWithContext<Integer> aLiveDomain = new LiveDomainWithContext<>(a.domain());
    LiveDomainWithContext<Integer> bLiveDomain = new LiveDomainWithContext<>(b.domain());
    LiveDomainWithContext<Integer> cLiveDomain = new LiveDomainWithContext<>(c.domain());
    liveDomains.put(a, aLiveDomain);
    liveDomains.put(b, bLiveDomain);
    liveDomains.put(c, cLiveDomain);
    NotEquals<Integer> abConstraint = new NotEquals<>(a, b);
    NotEquals<Integer> acConstraint = new NotEquals<>(a, c);
    NotEquals<Integer> bcConstraint = new NotEquals<>(b, c);
    Problem<Integer> problem =
        new Problem<Integer>(ImmutableList.of(a, b, c), ImmutableSet.of(abConstraint, acConstraint, bcConstraint)) {};
    DomWDegVariableHeuristic<Integer> heuristic = new DomWDegVariableHeuristic<>(problem);
    Set<Variable<Integer>> allVariablesUnassignedAndOrdered = new TreeSet<>();
    allVariablesUnassignedAndOrdered.add(a);
    allVariablesUnassignedAndOrdered.add(b);
    allVariablesUnassignedAndOrdered.add(c);
    assertThat(heuristic.nextVariable(allVariablesUnassignedAndOrdered, liveDomains)).isEqualTo(c);
    aLiveDomain.pruneWithReason(1, of(1, FAILED_ATTEMPT));
    aLiveDomain.pruneWithReason(1, of(2, FAILED_ATTEMPT));
    aLiveDomain.pruneWithReason(1, of(3, FAILED_ATTEMPT));
    assertThat(heuristic.nextVariable(allVariablesUnassignedAndOrdered, liveDomains)).isEqualTo(a);
  }

  @Test
  public void variableChangesAfterWipeout() {
    Variable<Integer> a = Variable.create("A", Domain.integers(1, 3));
    Variable<Integer> b = Variable.create("B", Domain.integers(1, 3));
    Variable<Integer> c = Variable.create("C", Domain.integers(1, 3));
    Map<Variable<Integer>, LiveDomainWithContext<Integer>> liveDomains = new HashMap<>();
    liveDomains.put(a, new LiveDomainWithContext<>(a.domain()));
    liveDomains.put(b, new LiveDomainWithContext<>(b.domain()));
    liveDomains.put(c, new LiveDomainWithContext<>(c.domain()));
    NotEquals<Integer> abConstraint = new NotEquals<>(a, b);
    NotEquals<Integer> acConstraint = new NotEquals<>(a, c);
    NotEquals<Integer> bcConstraint = new NotEquals<>(b, c);
    Problem<Integer> problem =
        new Problem<Integer>(ImmutableList.of(a, b, c), ImmutableSet.of(abConstraint, acConstraint, bcConstraint)) {};
    DomWDegVariableHeuristic<Integer> heuristic = new DomWDegVariableHeuristic<>(problem);
    Set<Variable<Integer>> allVariablesUnassignedAndOrdered = new TreeSet<>();
    allVariablesUnassignedAndOrdered.add(a);
    allVariablesUnassignedAndOrdered.add(b);
    allVariablesUnassignedAndOrdered.add(c);
    assertThat(heuristic.nextVariable(allVariablesUnassignedAndOrdered, liveDomains)).isEqualTo(a);
    heuristic.constraintCausedDomainWipeout(bcConstraint);
    assertThat(heuristic.nextVariable(allVariablesUnassignedAndOrdered, liveDomains)).isEqualTo(b);
    heuristic.constraintCausedDomainWipeout(acConstraint);
    assertThat(heuristic.nextVariable(allVariablesUnassignedAndOrdered, liveDomains)).isEqualTo(c);
  }

  @Test
  public void variable_withSmallestRatioChosen() {
    Variable<Integer> a = Variable.create("A", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("B", Domain.integers(1, 4));
    Variable<Integer> c = Variable.create("C", Domain.integers(1, 3));
    Map<Variable<Integer>, LiveDomainWithContext<Integer>> liveDomains = new HashMap<>();
    LiveDomainWithContext<Integer> aLiveDomain = new LiveDomainWithContext<>(a.domain());
    LiveDomainWithContext<Integer> bLiveDomain = new LiveDomainWithContext<>(b.domain());
    LiveDomainWithContext<Integer> cLiveDomain = new LiveDomainWithContext<>(c.domain());
    liveDomains.put(a, aLiveDomain);
    liveDomains.put(b, bLiveDomain);
    liveDomains.put(c, cLiveDomain);
    NotEquals<Integer> abConstraint = new NotEquals<>(a, b);
    NotEquals<Integer> acConstraint = new NotEquals<>(a, c);
    NotEquals<Integer> bcConstraint = new NotEquals<>(b, c);
    Problem<Integer> problem =
        new Problem<Integer>(ImmutableList.of(a, b, c), ImmutableSet.of(abConstraint, acConstraint, bcConstraint)) {};
    DomWDegVariableHeuristic<Integer> heuristic = new DomWDegVariableHeuristic<>(problem);
    Set<Variable<Integer>> allVariablesUnassignedAndOrdered = new TreeSet<>();
    allVariablesUnassignedAndOrdered.add(a);
    allVariablesUnassignedAndOrdered.add(b);
    allVariablesUnassignedAndOrdered.add(c);
    aLiveDomain.pruneWithReason(1, of(1, FAILED_ATTEMPT));
    aLiveDomain.pruneWithReason(1, of(2, FAILED_ATTEMPT));
    aLiveDomain.pruneWithReason(1, of(3, FAILED_ATTEMPT));
    heuristic.constraintCausedDomainWipeout(bcConstraint);
    // A has smaller live domain (2), but C had caused domain wipeout and has domain size of 3, giving score of 1.5.
    assertThat(heuristic.nextVariable(allVariablesUnassignedAndOrdered, liveDomains)).isEqualTo(c);
  }
}
