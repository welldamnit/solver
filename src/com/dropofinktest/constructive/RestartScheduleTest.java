package constructive;

import com.dropofink.constructive.RestartSchedule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RestartScheduleTest {
  @Test
  public void shouldRestart_sameRate() {
    RestartSchedule schedule = RestartSchedule.sameRate(100);
    assertThat(schedule.shouldRestart(10)).isFalse();
    assertThat(schedule.shouldRestart(100)).isTrue();
    assertThat(schedule.shouldRestart(101)).isFalse();
    assertThat(schedule.shouldRestart(200)).isTrue();
  }

  @Test
  public void shouldRestart_increasingRate() {
    RestartSchedule schedule = RestartSchedule.increasingRate(100, 2);
    assertThat(schedule.shouldRestart(10)).isFalse();
    assertThat(schedule.shouldRestart(100)).isTrue();
    assertThat(schedule.shouldRestart(101)).isFalse();
    assertThat(schedule.shouldRestart(200)).isFalse();
    assertThat(schedule.shouldRestart(300)).isTrue();
  }
}
