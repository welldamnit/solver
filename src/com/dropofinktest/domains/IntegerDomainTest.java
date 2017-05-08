package domains;

import com.dropofink.domains.IntegerDomain;
import org.junit.Test;

import static com.dropofink.domains.IntegerDomain.IntegerValue;
import static com.google.common.truth.Truth.assertThat;

public class IntegerDomainTest {
  @Test
  public void testIntegerValue() {
    IntegerValue v = IntegerValue.of(5);
    assertThat(v.toString()).isEqualTo("5");
    assertThat(v).isEqualTo(IntegerValue.of(5));
  }

  @Test
  public void testDomainCreation() {
    IntegerDomain testDomain = IntegerDomain.ofInclusive(1, 3);
    assertThat(testDomain.getValues()).containsAllOf(IntegerValue.of(1), IntegerValue.of(2), IntegerValue.of(3));
  }
}
