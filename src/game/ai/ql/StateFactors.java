package game.ai.ql;

enum Horizontal {
  RIGHT,
  LEFT,
  SAME,
}

enum Vertical {
  UP,
  DOWN,
  SAME,
}

record Collision(boolean right, boolean left, boolean up, boolean down) {
  public static final int POSSIBLE_VALUES = 1 << 4;

  public Collision() {
    this(false, false, false, false);
  }

  public Collision withRight() {
    return new Collision(true, left, up, down);
  }

  public Collision withLeft() {
    return new Collision(right, true, up, down);
  }

  public Collision withUp() {
    return new Collision(right, left, true, down);
  }

  public Collision withDown() {
    return new Collision(right, left, up, true);
  }
}
