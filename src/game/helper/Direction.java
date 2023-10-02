package game.helper;

import java.util.Arrays;
import java.util.Random;

public enum Direction {
  RIGHT,
  LEFT,
  UP,
  DOWN;

  private static final Direction[] VALUES = values();
  private static final int SIZE = VALUES.length;
  private static final Random RANDOM = new Random();

  public static Direction random() {
    return VALUES[RANDOM.nextInt(SIZE)];
  }

  public static Direction random(Direction exclusion) {
    Direction value;
    do {
      value = random();
    } while (value == exclusion);
    return value;
  }

  public Direction invert() {
    switch (this) {
      case RIGHT:
        return LEFT;
      case LEFT:
        return RIGHT;
      case DOWN:
        return UP;
      case UP:
        return DOWN;
      default:
        return null;
    }
  }

  public static Direction[] values(Direction exclusion) {
    return Arrays
      .stream(VALUES)
      .filter(v -> v != exclusion)
      .toArray(Direction[]::new);
  }
}
