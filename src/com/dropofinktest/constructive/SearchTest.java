package constructive;

import com.dropofink.constraints.BinaryConstraints;
import com.dropofink.constructive.Search;
import com.dropofink.constructive.SimpleHeuristics.FirstValue;
import com.dropofink.constructive.SimpleHeuristics.FirstVariable;
import com.dropofink.model.Domain;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.dropofink.problems.NQueensBinary;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class SearchTest {
  @Test
  public void noSolutionsBasicProblem() {
    Variable<Integer> a = Variable.create("A", Domain.integers(1, 1));
    Variable<Integer> b = Variable.create("B", Domain.integers(1, 1));
    Problem<Integer> noSolutionsProblem =
        new Problem<Integer>(ImmutableList.of(a, b), ImmutableSet.of(new BinaryConstraints.NotEquals<>(a, b))) {};
    Search<Integer> search = new Search<>(noSolutionsProblem, new FirstVariable<>(), new FirstValue<>());
    assertThat(search.hasNextSolution()).isFalse();
  }

  @Test
  public void noConstraintsProblem_findsAllSolutions() {
    Variable<Integer> a = Variable.create("A", Domain.integers(1, 5));
    Variable<Integer> b = Variable.create("B", Domain.integers(1, 5));
    Problem<Integer> noConstraintsProblem = new Problem<Integer>(ImmutableList.of(a, b), ImmutableSet.of()) {};
    Search<Integer> search = new Search<>(noConstraintsProblem, new FirstVariable<>(), new FirstValue<>());
    int actualSolutions = 0;
    while (search.hasNextSolution()) {
      actualSolutions++;
      search.nextSolution();
    }
    assertThat(actualSolutions).isEqualTo(25);
  }

  @Test
  public void threeQueensNoSolution_noSolutions() {
    NQueensBinary problem = NQueensBinary.create(3);
    Search<Integer> search = new Search<>(problem, new FirstVariable<>(), new FirstValue<>());
    assertThat(search.hasNextSolution()).isFalse();
  }

  @Test
  public void fourQueensSolution_findsAllSolutions() {
    NQueensBinary problem = NQueensBinary.create(4);
    Search<Integer> search = new Search<>(problem, new FirstVariable<>(), new FirstValue<>());
    Set<String> expectedSolutions =
        ImmutableSet.of("Q1 = [2], Q2 = [4], Q3 = [1], Q4 = [3]", "Q1 = [3], Q2 = [1], Q3 = [4], Q4 = [2]");
    Set<String> actualSolutions = new HashSet<>();
    while (search.hasNextSolution()) {
      actualSolutions.add(search.nextSolution().toString());
    }
    assertThat(actualSolutions).containsExactlyElementsIn(expectedSolutions);
  }

  @Test
  public void eightQueensSanityCheck() {
    NQueensBinary problem = NQueensBinary.create(8);
    Search<Integer> search = new Search<>(problem, new FirstVariable<>(), new FirstValue<>());
    int solutions = 0;
    while (search.hasNextSolution()) {
      solutions++;
      search.nextSolution();
    }
    assertThat(solutions).isEqualTo(92);
  }

  @Test
  public void tenQueensSanityCheck() {
    NQueensBinary problem = NQueensBinary.create(10);
    Search<Integer> search = new Search<>(problem, new FirstVariable<>(), new FirstValue<>());
    int actualSolutions = 0;
    while (search.hasNextSolution()) {
      actualSolutions++;
      search.nextSolution();
    }
    assertThat(actualSolutions).isEqualTo(724);
  }
}
