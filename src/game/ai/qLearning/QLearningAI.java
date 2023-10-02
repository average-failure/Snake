package game.ai.qLearning;

import game.helper.Direction;
import game.helper.Point;
import game.panel.GamePanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearningAI {

  private static class EpsilonGreedy {

    private static float epsilon = 1;

    private static final Random RANDOM = new Random();
    private static final float DECAY = 0.0001f;

    public static boolean isGreedy() {
      boolean isGreedy = RANDOM.nextFloat(1) > epsilon;
      if (!isGreedy && epsilon > 0) epsilon -= DECAY;
      return isGreedy;
    }

    public static void reset() {
      epsilon = 1;
    }
  }

  private static final float LEARNING_RATE = 0.7f;
  private static final float DISCOUNT = 0.5f;
  private static final short TOTAL_STATES = (short) (
    Direction.values().length *
    (Horizontal.values().length * Vertical.values().length) *
    Collision.POSSIBLE_VALUES *
    2
  );
  private static final State[] Q_TABLE = new State[TOTAL_STATES];

  private final List<History> history = new ArrayList<>(0);

  public static void initTable() {
    boolean[] flags = { true, false };
    short i = 0;
    for (Direction dir : Direction.values()) {
      for (Horizontal ax : Horizontal.values()) for (Vertical ay : Vertical.values()) {
        for (boolean r : flags) for (boolean l : flags) for (boolean u : flags) for (boolean d : flags) {
          for (boolean c : flags) {
            Q_TABLE[i++] = new State(dir, ax, ay, new Collision(r, l, u, d), c);
          }
        }
      }
    }
    EpsilonGreedy.reset();
  }

  public void act(GamePanel game) {
    State state = getState(game);

    Direction action = state.getRandomAction().getDirection();

    if (EpsilonGreedy.isGreedy()) action = state.getBestAction().getDirection();

    Point head = game.getSnakePart(0);
    Point apple = game.getApple();

    int ax = head.getX() - apple.getX();
    int ay = head.getY() - apple.getY();

    history.add(
      new History(
        state,
        state.getAction(action),
        ax * ax + ay * ay,
        game.getApplesEaten()
      )
    );

    switch (action) {
      case RIGHT:
        game.turn(Direction.RIGHT);
        break;
      case LEFT:
        game.turn(Direction.LEFT);
        break;
      case UP:
        game.turn(Direction.UP);
        break;
      case DOWN:
        game.turn(Direction.DOWN);
        break;
    }

    updateTable(game.isRunning());
  }

  private void updateTable(boolean isRunning) {
    boolean dead = !isRunning;
    for (int i = history.size() - 1; i > 0; i--) {
      if (dead) {
        Action action = history.get(history.size() - 1).getAction();

        byte reward = -10;

        // Bellman equation
        action.setWeight(
          (1 - LEARNING_RATE) * action.getWeight() + LEARNING_RATE * reward
        );

        dead = false;
      } else {
        History current = history.get(i);
        History previous = history.get(i - 1);
        Action previousAction = previous.getAction();

        int dSq1 = current.getDSq();
        int dSq2 = previous.getDSq();

        boolean foodEaten = current.getScore() != previous.getScore();
        boolean foodCloser = dSq1 < dSq2;

        byte reward = -3;

        if (foodEaten) reward = 10; else if (foodCloser) reward = 1;

        State currentState = current.getState();

        // Bellman equation
        previousAction.setWeight(
          (1 - LEARNING_RATE) *
          previousAction.getWeight() +
          LEARNING_RATE *
          (reward + DISCOUNT * currentState.getBestAction().getWeight())
        );
      }
    }
  }

  private State getState(GamePanel game) {
    Horizontal adx;
    Vertical ady;
    Collision collision = checkAdjacent(game);

    Point head = game.getSnakePart(0);
    Point apple = game.getApple();

    short ax = (short) (head.getX() - apple.getX());
    short ay = (short) (head.getY() - apple.getY());

    if (ax < 0) {
      adx = Horizontal.RIGHT;
    } else if (ax > 0) {
      adx = Horizontal.LEFT;
    } else {
      adx = Horizontal.SAME;
    }

    if (ay < 0) {
      ady = Vertical.DOWN;
    } else if (ay > 0) {
      ady = Vertical.UP;
    } else {
      ady = Vertical.SAME;
    }

    for (State state : Q_TABLE) {
      if (
        state.match(
          game.getDirection(),
          adx,
          ady,
          collision,
          checkDirCollision(game)
        )
      ) return state;
    }

    return null;
  }

  private boolean checkDirCollision(GamePanel game) {
    byte x = game.getSnakePart(0).getX();
    byte y = game.getSnakePart(0).getY();
    switch (game.getDirection()) {
      case RIGHT:
        for (byte i = x; i < game.getXCells(); i++) {
          if (checkCollision(new Point(x, y), game)) return true;
        }
        break;
      case LEFT:
        for (byte i = x; i > 0; i--) {
          if (checkCollision(new Point(x, y), game)) return true;
        }
        break;
      case DOWN:
        for (byte i = y; i < game.getYCells(); i++) {
          if (checkCollision(new Point(x, y), game)) return true;
        }
        break;
      case UP:
        for (byte i = y; i > 0; i--) {
          if (checkCollision(new Point(x, y), game)) return true;
        }
        break;
      default:
        break;
    }
    return false;
  }

  private Collision checkAdjacent(GamePanel game) {
    Point[] adjacents = game.getSnakePart(0).getAdjacents();
    Collision collision = new Collision();
    for (byte i = 0; i < adjacents.length; i++) {
      if (checkCollision(adjacents[i], game)) {
        switch (i) {
          case 0:
            collision.left = true;
          case 1:
            collision.right = true;
          case 2:
            collision.up = true;
          case 3:
            collision.down = true;
          default:
            break;
        }
      }
    }

    return collision;
  }

  private boolean checkCollision(Point point, GamePanel game) {
    byte x = point.getX();
    byte y = point.getY();

    if (
      x < 0 || x >= game.getXCells() || y < 0 || y >= game.getYCells()
    ) return true;

    for (short i = 1; i < game.getBodyParts(); i++) {
      if (point.equals(game.getSnakePart(i))) return true;
    }

    return false;
  }
}
