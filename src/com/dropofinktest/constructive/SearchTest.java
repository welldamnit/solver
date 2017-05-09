package constructive;

import com.dropofink.constructive.Search;
import com.dropofink.constructive.SimpleHeuristics;
import com.dropofink.problems.NQueensBinary;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class SearchTest {
  @Test
  public void nQueensNoSolution() {
    NQueensBinary problem = NQueensBinary.create(3);
    Search<Integer> search =
        new Search<>(problem, new SimpleHeuristics.FirstVariable<>(), new SimpleHeuristics.FirstValue<>());
    assertThat(search.hasNextSolution()).isFalse();
  }

  @Test
  public void nQueensSolution() {
    NQueensBinary problem = NQueensBinary.create(4);
    Search<Integer> search =
        new Search<>(problem, new SimpleHeuristics.FirstVariable<>(), new SimpleHeuristics.FirstValue<>());
    assertThat(search.hasNextSolution()).isTrue();
    assertThat(search.nextSolution().toString()).isEqualTo("Q1 = [2], Q2 = [4], Q3 = [1], Q4 = [3]");
    assertThat(search.hasNextSolution()).isTrue();
    assertThat(search.nextSolution().toString()).isEqualTo("Q1 = [3], Q2 = [1], Q3 = [4], Q4 = [2]");
    assertThat(search.hasNextSolution()).isFalse();
  }

  @Test
  public void eightQueensSanityCheck() {
    NQueensBinary problem = NQueensBinary.create(8);
    Search<Integer> search =
        new Search<>(problem, new SimpleHeuristics.FirstVariable<>(), new SimpleHeuristics.FirstValue<>());
    int solutions = 0;
    while (search.hasNextSolution()) {
      solutions++;
      search.nextSolution();
    }
    assertThat(solutions).isEqualTo(92);
    assertThat(search.getStatesVisited()).isEqualTo(4021);
  }
}
