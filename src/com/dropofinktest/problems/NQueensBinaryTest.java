package problems;

import com.dropofink.domains.IntegerDomain;
import com.dropofink.domains.IntegerDomain.IntegerValue;
import com.dropofink.model.*;
import com.dropofink.problems.NQueensBinary;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class NQueensBinaryTest {
  @Test
  public void validateBinaryConstraints() {
    Problem problem = NQueensBinary.create(3);
    Domain expectedDomain = IntegerDomain.ofInclusive(1, 3);
    assertThat(problem.getVariables()).containsExactly(
        Variable.create("Q1", expectedDomain),
        Variable.create("Q2", expectedDomain),
        Variable.create("Q3", expectedDomain));
  }

  @Test
  public void validateSatisfiability_failsSameRow() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable q1 = problem.getVariables().get(0);
    Variable q2 = problem.getVariables().get(1);
    Variable q3 = problem.getVariables().get(2);
    Variable q4 = problem.getVariables().get(3);
    IntegerValue v1 = IntegerValue.of(1);
    Assignments assignments = new Assignments();
    assignments.add(Assignment.of(q1, v1));
    assignments.add(Assignment.of(q2, v1));
    assignments.add(Assignment.of(q3, v1));
    assignments.add(Assignment.of(q4, v1));

    assertThat(problem.isSatisfied(assignments)).isFalse();
  }

  @Test
  public void validateSatisfiability_failsSameDiagonal() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable q1 = problem.getVariables().get(0);
    Variable q2 = problem.getVariables().get(1);
    Variable q3 = problem.getVariables().get(2);
    Variable q4 = problem.getVariables().get(3);
    IntegerValue v1 = IntegerValue.of(1);
    IntegerValue v2 = IntegerValue.of(2);
    IntegerValue v3 = IntegerValue.of(3);
    IntegerValue v4 = IntegerValue.of(4);
    Assignments assignments = new Assignments();
    assignments.add(Assignment.of(q1, v1));
    assignments.add(Assignment.of(q2, v2));
    assignments.add(Assignment.of(q3, v3));
    assignments.add(Assignment.of(q4, v4));

    assertThat(problem.isSatisfied(assignments)).isFalse();
  }

  @Test
  public void validateSatisfiability_passesGivenSolution() {
    NQueensBinary problem = NQueensBinary.create(4);
    Variable q1 = problem.getVariables().get(0);
    Variable q2 = problem.getVariables().get(1);
    Variable q3 = problem.getVariables().get(2);
    Variable q4 = problem.getVariables().get(3);
    IntegerValue v1 = IntegerValue.of(1);
    IntegerValue v2 = IntegerValue.of(2);
    IntegerValue v3 = IntegerValue.of(3);
    IntegerValue v4 = IntegerValue.of(4);
    Assignments assignments = new Assignments();
    assignments.add(Assignment.of(q1, v2));
    assignments.add(Assignment.of(q2, v4));
    assignments.add(Assignment.of(q3, v1));
    assignments.add(Assignment.of(q4, v3));

    assertThat(problem.isSatisfied(assignments)).isTrue();
  }
}
