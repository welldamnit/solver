package constructive;

import com.dropofink.constructive.LiveDomainWithContext;
import com.dropofink.model.Domain;
import org.junit.Test;

import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.CONSTRAINT_PROPAGATION;
import static com.google.common.truth.Truth.assertThat;

public class LiveDomainWithContextTest {
  private static final Domain<Integer> DOMAIN = Domain.integers(1, 4);
  private LiveDomainWithContext<Integer> liveDomain = new LiveDomainWithContext<>(DOMAIN);

  @Test(expected = UnsupportedOperationException.class)
  public void prune_DirectCallDisallowed() {
    liveDomain.prune(1);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void unprune_DirectCallDisallowed() {
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
    liveDomain.unprune(1);
  }

    @Test(expected = IllegalArgumentException.class)
  public void pruneWithReason_failsIfDepthsAreInconsistent() {
    liveDomain.pruneWithReason(2, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(2, CONSTRAINT_PROPAGATION));
  }

  @Test(expected = IllegalArgumentException.class)
  public void pruneWithReason_failsAlreadyPruned() {
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
  }

  @Test
  public void pruneWithReason_canPruneAtSameDepth() {
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(2, CONSTRAINT_PROPAGATION));
    assertThat(liveDomain.getLiveValues()).containsExactly(3, 4);
  }

  @Test
  public void undoPruningsAtDepth_undoLastDepthOnly() {
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
    liveDomain.pruneWithReason(2, LiveDomainWithContext.PruningWithReason.of(2, CONSTRAINT_PROPAGATION));
    liveDomain.undoPruningsAtDepth(2);
    assertThat(liveDomain.getLiveValues()).containsExactly(2, 3, 4);
  }

  @Test
  public void undoPruningsAtDepth_undoMany() {
    liveDomain.pruneWithReason(1, LiveDomainWithContext.PruningWithReason.of(1, CONSTRAINT_PROPAGATION));
    liveDomain.pruneWithReason(2, LiveDomainWithContext.PruningWithReason.of(2, CONSTRAINT_PROPAGATION));
    liveDomain.pruneWithReason(2, LiveDomainWithContext.PruningWithReason.of(3, CONSTRAINT_PROPAGATION));
    liveDomain.undoPruningsAtDepth(1);
    assertThat(liveDomain.getLiveValues()).containsExactly(1, 2, 3, 4);
  }
}
