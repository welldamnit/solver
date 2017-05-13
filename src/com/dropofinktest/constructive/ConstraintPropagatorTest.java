package constructive;

import com.dropofink.constructive.ConstraintPropagator;
import com.dropofink.model.Assignment;
import com.dropofink.model.Variable;
import com.dropofink.problems.NQueensBinary;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ConstraintPropagatorTest {
  @Test
  public void propagate_eliminatesDomain_threeQueens() {
    NQueensBinary problem = NQueensBinary.create(3);
    Variable<Integer> q1 = problem.getVariables().get(0);
    ConstraintPropagator<Integer> propagator = new ConstraintPropagator<>(problem);
    assertThat(propagator.propagateAssignment(Assignment.of(q1, 1))).isFalse();

  }

  @Test
  public void propagate_removesManyLiveValues_fourQueens() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    ConstraintPropagator<Integer> propagator = new ConstraintPropagator<>(problem);
    assertThat(propagator.propagateAssignment(Assignment.of(q1, 2))).isTrue();
    assertThat(propagator.getLiveValues(q1)).containsExactly(2);
    assertThat(propagator.getLiveValues(q2)).containsExactly(4);
    assertThat(propagator.getLiveValues(q3)).containsExactly(1);
    assertThat(propagator.getLiveValues(q4)).containsExactly(3);
  }

  @Test
  public void undoLastPropagation_returnsToOriginalState_fourQueens() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    ConstraintPropagator<Integer> propagator = new ConstraintPropagator<>(problem);
    propagator.propagateAssignment(Assignment.of(q1, 2));
    propagator.undoLastPropagation();
    assertThat(propagator.getLiveValues(q1)).containsExactly(1, 2, 3, 4);
    assertThat(propagator.getLiveValues(q2)).containsExactly(1, 2, 3, 4);
    assertThat(propagator.getLiveValues(q3)).containsExactly(1, 2, 3, 4);
    assertThat(propagator.getLiveValues(q4)).containsExactly(1, 2, 3, 4);
  }
}
