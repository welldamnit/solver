package com.dropofink.constructive;

import com.dropofink.model.Domain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.dropofink.constructive.LiveDomainWithContext.PruningWithReason.PruningReason.ASSIGNMENT;
import static com.google.common.base.Preconditions.checkArgument;

public class LiveDomainWithContext<T> extends LiveDomain<T> {
  private final Stack<PruningsAtDepth<T>> pruningStack;

  private static class PruningsAtDepth<T> {
    int depth;
    List<PruningWithReason<T>> pruningsWithReasons;
    private PruningsAtDepth(int depth) {
      this.depth = depth;
      this.pruningsWithReasons = new ArrayList<>();
    }
  }
  
  public LiveDomainWithContext(Domain<T> underlyingDomain) {
    super(underlyingDomain);
    this.pruningStack = new Stack<>();

    // Include a sentinel value in the stack to avoid extra logic for dealing with an empty stack elsewhere.
    pruningStack.push(new PruningsAtDepth<>(-1));
  }

  @Override
  public void prune(T value) {
    throw new UnsupportedOperationException("Don't call prune directly");
  }

  @Override
  public void unprune(T value) {
    throw new UnsupportedOperationException("Don't call unprune directly");
  }

  @Override
  public void pruneToSingleValue(T value) {
    throw new UnsupportedOperationException("Don't call unprune directly");
  }

  @Override
  public void unpruneSingleValue() {
    throw new UnsupportedOperationException("Don't call unprune directly");
  }

  public void pruneWithReason(int depth, PruningWithReason<T> pruningWithReason) {
    checkArgument(depth >= pruningStack.peek().depth);
    if (pruningStack.peek().depth != depth) {
      pruningStack.push(new PruningsAtDepth<>(depth));
    }
    pruningStack.peek().pruningsWithReasons.add(pruningWithReason);
    if (pruningWithReason.pruningReason == ASSIGNMENT) {
      super.pruneToSingleValue(pruningWithReason.prunedValue);
    } else {
      super.prune(pruningWithReason.prunedValue);
    }
  }

  public void undoPruningsAtDepth(int depth) {
    while(pruningStack.peek().depth >= depth) {
      for(PruningWithReason<T> pruning : pruningStack.pop().pruningsWithReasons) {
        if (pruning.pruningReason == ASSIGNMENT) {
          super.unpruneSingleValue();
        } else {
          super.unprune(pruning.prunedValue);
        }
      }
    }
  }

  public static class PruningWithReason<T> {
    public enum PruningReason {CONSTRAINT_PROPAGATION, ASSIGNMENT, FAILED_ATTEMPT}

    final T prunedValue;
    final PruningReason pruningReason;

    private PruningWithReason(T prunedValue, PruningReason pruningReason) {
      this.prunedValue = prunedValue;
      this.pruningReason = pruningReason;
    }

    @NotNull
    public static <T> PruningWithReason<T> of(T prunedValue, PruningReason pruningReason) {
      return new PruningWithReason<>(prunedValue, pruningReason);
    }
  }
}
