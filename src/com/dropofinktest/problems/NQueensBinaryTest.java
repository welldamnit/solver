package problems;

import com.dropofink.model.*;
import com.dropofink.problems.NQueensBinary;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class NQueensBinaryTest {
  @Test
  public void validateBinaryConstraints() {
    Problem problem = NQueensBinary.create(3);
    Domain<Integer> expectedDomain = Domain.integers(1, 3);
    assertThat(problem.getVariables()).containsExactly(
        Variable.create("Q1", expectedDomain),
        Variable.create("Q2", expectedDomain),
        Variable.create("Q3", expectedDomain));
  }

  @Test
  public void validateSatisfiability_failsSameRow() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    Assignments<Integer> assignments = new Assignments<>();
    assignments.add(Assignment.of(q1, 1));
    assignments.add(Assignment.of(q2, 1));
    assignments.add(Assignment.of(q3, 1));
    assignments.add(Assignment.of(q4, 1));

    assertThat(problem.isSatisfied(assignments)).isFalse();
  }

  @Test
  public void validateSatisfiability_failsSameDiagonal() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    Assignments<Integer> assignments = new Assignments<>();
    assignments.add(Assignment.of(q1, 1));
    assignments.add(Assignment.of(q2, 2));
    assignments.add(Assignment.of(q3, 3));
    assignments.add(Assignment.of(q4, 4));

    assertThat(problem.isSatisfied(assignments)).isFalse();
  }

  @Test
  public void validateSatisfiability_passesGivenSolution() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable<Integer> q1 = problem.getVariables().get(0);
    Variable<Integer> q2 = problem.getVariables().get(1);
    Variable<Integer> q3 = problem.getVariables().get(2);
    Variable<Integer> q4 = problem.getVariables().get(3);
    Assignments<Integer> assignments = new Assignments<>();
    assignments.add(Assignment.of(q1, 2));
    assignments.add(Assignment.of(q2, 4));
    assignments.add(Assignment.of(q3, 1));
    assignments.add(Assignment.of(q4, 3));

    assertThat(problem.isSatisfied(assignments)).isTrue();
  }
}
