package game.ai.qLearning;

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

class Collision {

  public static final int POSSIBLE_VALUES = 1 << 4;

  public boolean right;
  public boolean left;
  public boolean up;
  public boolean down;

  public Collision() {
    this(false, false, false, false);
  }

  public Collision(boolean right, boolean left, boolean up, boolean down) {
    this.right = right;
    this.left = left;
    this.up = up;
    this.down = down;
  }

  public boolean equals(Collision other) {
    return (
      this.right == other.right &&
      this.left == other.left &&
      this.up == other.up &&
      this.down == other.down
    );
  }
}
