package constructive;

import com.dropofink.constructive.ConstraintPropagator;
import com.dropofink.constructive.LiveDomainWithContext;
import com.dropofink.model.Assignment;
import com.dropofink.model.Variable;
import com.dropofink.problems.NQueensBinary;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class ConstraintPropagatorTest {
  @Test
  public void propagate_eliminatesDomain_threeQueens() {
    NQueensBinary problem = NQueensBinary.create(3);
    Map<Variable<Integer>, LiveDomainWithContext<Integer>> liveDomains = initLiveDomains(problem.getVariables());
    Variable<Integer> q1 = problem.getVariables().get(0);
    ConstraintPropagator<Integer> propagator = new ConstraintPropagator<>(problem, liveDomains);
    assertThat(propagator.propagateAssignment(Assignment.of(q1, 1))).isFalse();

  }

  @Test
  public void propagate_removesManyLiveValues_fourQueens() {
    NQueensBinary problem = NQueensBinary.create(4);
    Map<Variable<Integer>, LiveDomainWithContext<Integer>> liveDomains = initLiveDomains(problem.getVariables());
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    ConstraintPropagator<Integer> propagator = new ConstraintPropagator<>(problem, liveDomains);
    assertThat(propagator.propagateAssignment(Assignment.of(q1, 2))).isTrue();
    assertThat(liveDomains.get(q1).getLiveValues()).containsExactly(2);
    assertThat(liveDomains.get(q2).getLiveValues()).containsExactly(4);
    assertThat(liveDomains.get(q3).getLiveValues()).containsExactly(1);
    assertThat(liveDomains.get(q4).getLiveValues()).containsExactly(3);
  }

  @Test
  public void undoLastPropagation_returnsToOriginalState_fourQueens() {
    NQueensBinary problem = NQueensBinary.create(4);
    Map<Variable<Integer>, LiveDomainWithContext<Integer>> liveDomains = initLiveDomains(problem.getVariables());
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    ConstraintPropagator<Integer> propagator = new ConstraintPropagator<>(problem, liveDomains);
    propagator.propagateAssignment(Assignment.of(q1, 2));
    propagator.undoLastPropagation();
    assertThat(liveDomains.get(q1).getLiveValues()).containsExactly(1, 2, 3, 4);
    assertThat(liveDomains.get(q2).getLiveValues()).containsExactly(1, 2, 3, 4);
    assertThat(liveDomains.get(q3).getLiveValues()).containsExactly(1, 2, 3, 4);
    assertThat(liveDomains.get(q4).getLiveValues()).containsExactly(1, 2, 3, 4);
  }

  private static <T> Map<Variable<T>, LiveDomainWithContext<T>> initLiveDomains(Collection<Variable<T>> variables) {
    Map<Variable<T>, LiveDomainWithContext<T>> liveDomains = new HashMap<>();
    for (Variable<T> variable : variables) {
      liveDomains.put(variable, new LiveDomainWithContext<>(variable.domain()));
    }
    return liveDomains;
  }
}
