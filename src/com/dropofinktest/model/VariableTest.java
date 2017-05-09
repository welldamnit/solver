package model;

import com.dropofink.model.Domain;
import com.dropofink.model.Variable;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class VariableTest {
  @Test
  public void equality() {
    Domain<Integer> domain = Domain.integers (1, 3);
    Variable v1 = Variable.create("a", domain);
    Variable v2 = Variable.create("a", domain);
    assertThat(v1).isEqualTo(v2);
  }

  @Test
  public void inequality_name() {
    Domain<Integer> domain = Domain.integers (1, 3);
    Variable v1 = Variable.create("a", domain);
    Variable v2 = Variable.create("b", domain);
    assertThat(v1).isNotEqualTo(v2);
  }

  @Test
  public void inequality_domain() {
    Domain<Integer> domain = Domain.integers (1, 3);
    Domain<Integer> otherDomain = Domain.integers (1, 4);
    Variable v1 = Variable.create("a", domain);
    Variable v2 = Variable.create("a", otherDomain);
    assertThat(v1).isNotEqualTo(v2);
  }
}
