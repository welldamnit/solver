package constructive;

import com.dropofink.constructive.LiveDomain;
import com.dropofink.model.Domain;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class LiveDomainTest {
  private static final Domain<Integer> DOMAIN = Domain.integers(1, 4);
  private LiveDomain<Integer> liveDomain = new LiveDomain<>(DOMAIN);

  @Test
  public void basic() {
    assertThat(liveDomain.getLiveValues()).containsExactly(1, 2, 3, 4);
  }

  @Test
  public void prune_valid() {
    liveDomain.prune(1);
    assertThat(liveDomain.getLiveValues()).containsExactly(2, 3, 4);
  }

  @Test
  public void prune_allTheWay() {
    liveDomain.prune(1);
    assertThat(liveDomain.isEmpty()).isFalse();
    liveDomain.prune(2);
    assertThat(liveDomain.isEmpty()).isFalse();
    liveDomain.prune(3);
    assertThat(liveDomain.isEmpty()).isFalse();
    liveDomain.prune(4);
    assertThat(liveDomain.isEmpty()).isTrue();
    liveDomain.unprune(4);
    assertThat(liveDomain.isEmpty()).isFalse();
  }

  @Test(expected = IllegalArgumentException.class)
  public void prune_valueNotInDomain() {
    liveDomain.prune(7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void prune_valueAlreadyPruned() {
    liveDomain.prune(1);
    liveDomain.prune(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void prune_differentSingleValueSet() {
    liveDomain.pruneToSingleValue(1);
    liveDomain.prune(2);
  }

  @Test
  public void prune_sameSingleValueSet() {
    liveDomain.pruneToSingleValue(1);
    liveDomain.prune(1);
    assertThat(liveDomain.getLiveValues()).isEmpty();
  }

  @Test
  public void unprune_valid() {
    liveDomain.prune(1);
    liveDomain.prune(2);
    liveDomain.prune(3);
    liveDomain.unprune(1);
    assertThat(liveDomain.getLiveValues()).containsExactly(1, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void unprune_valueNotPruned() {
    liveDomain.unprune(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void unprune_singleValueSet() {
    liveDomain.prune(2);
    liveDomain.pruneToSingleValue(1);
    liveDomain.unprune(2);
  }

  @Test
  public void unprune_sameSingleValueSet() {
    liveDomain.pruneToSingleValue(1);
    liveDomain.prune(1);
    liveDomain.unprune(1);
    assertThat(liveDomain.getLiveValues()).containsExactly(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pruneToSingleValue_valueAlreadyPruned() {
    liveDomain.prune(1);
    liveDomain.pruneToSingleValue(1);
  }

  @Test
  public void pruneToSingleValue() {
    liveDomain.pruneToSingleValue(1);
    assertThat(liveDomain.getLiveValues()).containsExactly(1);
  }

  @Test
  public void unpruneSingleValue() {
    liveDomain.pruneToSingleValue(1);
    liveDomain.unpruneSingleValue();
    assertThat(liveDomain.getLiveValues()).containsExactly(1, 2, 3, 4);
  }
}
