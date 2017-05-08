package domains;

import static com.dropofink.domains.BooleanDomain.BooleanValue;
import static com.google.common.truth.Truth.assertThat;

import com.dropofink.domains.BooleanDomain;
import org.junit.Test;

public class BooleanDomainTest {
  @Test
  public void booleanEquality() {
    assertThat(BooleanValue.TRUE).isEqualTo(BooleanValue.TRUE);
    assertThat(BooleanValue.TRUE).isNotEqualTo(BooleanValue.FALSE);
  }

  @Test
  public void domainContainsTrueAndFalse() {
    assertThat(new BooleanDomain().getValues()).containsExactly(BooleanValue.TRUE, BooleanValue.FALSE);
  }
}
