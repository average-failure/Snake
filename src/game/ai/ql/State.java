package game.ai.ql;

import game.helper.Direction;
import java.util.Arrays;
import java.util.Random;

class State {

  private static final Random RANDOM = new Random();

  private Action[] actions;

  private Direction dir;
  private Horizontal ax;
  private Vertical ay;
  private Collision collision;
  private boolean collisionInFacingDir;

  public State(
    Direction dir,
    Horizontal ax,
    Vertical ay,
    Collision collision,
    boolean collisionInFacingDir
  ) {
    this.dir = dir;
    this.ax = ax;
    this.ay = ay;
    this.collision = collision;
    this.collisionInFacingDir = collisionInFacingDir;

    Direction[] possibleActions = Direction.values(dir.invert());
    actions = new Action[possibleActions.length];

    for (int i = 0; i < possibleActions.length; i++) {
      actions[i] = new Action(0, possibleActions[i]);
    }
  }

  public boolean match(
    Direction dir,
    Horizontal ax,
    Vertical ay,
    Collision collision,
    boolean collisionInFacingDir
  ) {
    return (
      this.dir == dir &&
      this.ax == ax &&
      this.ay == ay &&
      this.collision.equals(collision) &&
      this.collisionInFacingDir == collisionInFacingDir
    );
  }

  public Action getBestAction() {
    Action bestAction = actions[0];

    for (Action action : actions) {
      if (action.getWeight() > bestAction.getWeight()) bestAction = action;
    }

    return bestAction;
  }

  public Action getAction(Direction action) {
    for (Action a : actions) {
      if (a.getDirection() == action) {
        return a;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return (
      "State [actions=" +
      Arrays.toString(actions) +
      ", dir=" +
      dir +
      ", ax=" +
      ax +
      ", ay=" +
      ay +
      ", collision=" +
      collision +
      "]"
    );
  }

  public Action getRandomAction() {
    return actions[RANDOM.nextInt(actions.length)];
  }
}
