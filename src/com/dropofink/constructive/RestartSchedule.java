package com.dropofink.constructive;

public class RestartSchedule {
  private long nextRestartIteration;
  private final long increaseNumber;
  private final double increaseFactor;

  private RestartSchedule(long increaseNumber, double increaseFactor) {
    this.nextRestartIteration = increaseNumber;
    this.increaseNumber = increaseNumber;
    this.increaseFactor = increaseFactor;
  }

  static RestartSchedule never() {
    return new RestartSchedule(Integer.MAX_VALUE, 1);
  }

  public static RestartSchedule sameRate(long iterationsPerRestart) {
    return new RestartSchedule(iterationsPerRestart, 1);
  }

  public static RestartSchedule increasingRate(long firstRestartIteration, double increaseFactor) {
    return new RestartSchedule(firstRestartIteration, increaseFactor);
  }

  public boolean shouldRestart(long iterations) {
    if (iterations >= nextRestartIteration) {
      nextRestartIteration += increaseNumber * increaseFactor;
      return true;
    }
    return false;
  }
}
