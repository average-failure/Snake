package game.ai.ql;

import game.helper.Direction;

class Action {

  private float weight = 0;
  private Direction direction;

  /**
   * @param weight
   * @param direction
   */
  public Action(float weight, Direction direction) {
    this.weight = weight;
    this.direction = direction;
  }

  /**
   * @param weight the weight to set
   */
  public void setWeight(float weight) {
    this.weight = weight;
  }

  /**
   * @return the weight
   */
  public float getWeight() {
    return weight;
  }

  /**
   * @return the direction
   */
  public Direction getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return "Action [weight=" + weight + ", direction=" + direction + "]";
  }
}
