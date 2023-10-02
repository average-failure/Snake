package game.helper;

public class Point {

  private byte x;
  private byte y;
  private int index;

  public Point(byte x, byte y) {
    this(x, y, -1);
  }

  public Point(int x, int y) {
    this((byte) x, (byte) y, -1);
  }

  public Point(byte x, byte y, int index) {
    this.x = x;
    this.y = y;
    this.index = index;
  }

  public Point(Point p) {
    this(p.x, p.y, p.index);
  }

  /**
   * @return the x
   */
  public byte getX() {
    return x;
  }

  /**
   * @param x the x to set
   */
  public void setX(byte x) {
    this.x = x;
  }

  /**
   * @return the y
   */
  public byte getY() {
    return y;
  }

  /**
   * @param y the y to set
   */
  public void setY(byte y) {
    this.y = y;
  }

  /**
   * @return the index
   */
  public int getIndex() {
    return index;
  }

  /**
   * @param index the index to set
   */
  public void setIndex(int index) {
    this.index = (short) index;
  }

  public void moveRight(int amount) {
    x += amount;
  }

  public void moveLeft(int amount) {
    x -= amount;
  }

  public void moveUp(int amount) {
    y -= amount;
  }

  public void moveDown(int amount) {
    y += amount;
  }

  public boolean equals(Point p) {
    return p.x == x && p.y == y;
  }

  public int distanceSq(Point p) {
    return (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y);
  }

  public Point[] getAdjacents() {
    return new Point[] {
      new Point(x - 1, y),
      new Point(x + 1, y),
      new Point(x, y - 1),
      new Point(x, y + 1),
    };
  }

  @Override
  public boolean equals(Object obj) {
    return equals((Point) obj);
  }

  @Override
  public Point clone() {
    return new Point(x, y, index);
  }

  @Override
  public String toString() {
    return "Point [x=" + x + ", y=" + y + ", index=" + index + "]";
  }
}
