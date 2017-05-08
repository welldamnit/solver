package com.dropofink.domains;

import com.dropofink.model.Domain;
import com.dropofink.model.Value;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class BooleanDomain extends Domain {
  private static final Set<Value> VALUES = ImmutableSet.of(BooleanValue.TRUE, BooleanValue.FALSE);

  @Override
  public Set<Value> getValues() {return VALUES;}

  public static class BooleanValue extends Value<Boolean> {
    public static BooleanValue TRUE = new BooleanValue(true);
    public static BooleanValue FALSE = new BooleanValue(false);

    private final boolean value;
    private BooleanValue(boolean value) {
      this.value = value;
    }

    @Override
    public Boolean getValue() {
      return value;
    }

    @Override
    public int compareTo(Value<Boolean> o) {
      return Boolean.compare(value, o.getValue());
    }
  }
}
