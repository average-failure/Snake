package game.ai;

import game.App;
import game.ai.ql.QLearningAI;
import game.circuit.HamiltonianCircuit;
import game.frame.GameFrame;
import game.frame.QDisplayFrame;
import game.helper.Direction;
import game.helper.Point;
import game.panel.GamePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class SnakeAI implements ActionListener {

  public enum Mode {
    SIMPLE,
    HAMILTONIAN,
    Q_LEARNING,
  }

  private static Point[] circuit;

  private static byte gameSize;

  /**
   * @return the circuit
   */
  public static Point[] getCircuit() {
    return circuit;
  }

  private GameFrame frame;
  private GamePanel game;
  private Timer timer;
  private Mode mode;
  private float speed;

  private QLearningAI qLearningAI;
  private QDisplayFrame qDisplayFrame;

  private final ActionEvent actionEvent = new ActionEvent(
    this,
    ActionEvent.ACTION_PERFORMED,
    null
  );

  public SnakeAI(Mode mode) {
    this(mode, 5);
  }

  public SnakeAI(Mode mode, float speed) {
    this.mode = mode;
    this.speed = speed;

    if (gameSize != App.getSize()) {
      gameSize = App.getSize();
    }

    if (mode == Mode.HAMILTONIAN) circuit =
      HamiltonianCircuit.getCircuit(gameSize);

    frame = new GameFrame(this);
    game = frame.panel;

    if (mode == Mode.Q_LEARNING) {
      qDisplayFrame = new QDisplayFrame(frame);
      qLearningAI = new QLearningAI();
      QLearningAI.initTable();
    }

    timer = new Timer(30, this);
    timer.start();
  }

  /**
   * @return the mode
   */
  public Mode getMode() {
    return mode;
  }

  public void restartGame(GameFrame frame, GamePanel panel) {
    short oldScore = getScore();

    this.frame = frame;
    game = panel;

    if (mode == Mode.Q_LEARNING) {
      qLearningAI = new QLearningAI();
      if (qDisplayFrame.updateScore(oldScore) > 15) {
        speed = 1;
        game.stopAutoReset();
      }
      qDisplayFrame.newIteration();
    }

    timer.start();
  }

  /**
   * @return the frame
   */
  public GameFrame getFrame() {
    return frame;
  }

  /**
   * @return the current score
   */
  public short getScore() {
    return game.getApplesEaten();
  }

  /**
   * @return the state of the game
   */
  public boolean isAlive() {
    return game.isRunning();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    for (short i = 0; i < speed; i++) {
      move();

      if (!game.isRunning()) timer.stop();
    }
    if (mode == Mode.Q_LEARNING) {
      if (speed != 1 && speed < 1000) speed = (float) Math.pow(speed, 1.01);
    } else speed *= 1.001;
  }

  private void move() {
    switch (mode) {
      case SIMPLE:
        simple();
        game.actionPerformed(actionEvent);
        break;
      case HAMILTONIAN:
        hamiltonian();
        break;
      case Q_LEARNING:
        qLearningAI.act(game);
        game.actionPerformed(actionEvent);
        break;
      default:
        break;
    }
  }

  private void hamiltonian() {
    Point head = game.getSnakePart(0);
    int headIndex = head.getIndex();

    if (headIndex < 0) return;

    if (headIndex + 1 >= circuit.length) headIndex = 0;

    Point point = circuit[headIndex + 1];

    int i = detectShortcut(head, headIndex);
    if (i > -1) point = circuit[i];

    Direction direction = game.getDirection();

    byte hx = head.getX();
    byte hy = head.getY();
    byte px = point.getX();
    byte py = point.getY();

    if (hx < px && direction != Direction.LEFT) {
      game.faceRight();
    } else if (hx > px && direction != Direction.RIGHT) {
      game.faceLeft();
    } else if (hy < py && direction != Direction.UP) {
      game.faceDown();
    } else if (hy > py && direction != Direction.DOWN) {
      game.faceUp();
    }

    game.actionPerformed(point);
  }

  private int detectShortcut(Point head, int headIndex) {
    if (headIndex == circuit.length - 1) return -1;

    short snakeLength = game.getBodyParts();
    int appleIndex = game.getApple().getIndex();
    if (headIndex > appleIndex) appleIndex = circuit.length - 1;

    int bestIndex = -1;
    int bestDiff = Integer.MAX_VALUE;

    CIRCUIT_LOOP:for (int i = headIndex; i < appleIndex; i++) {
      Point currentPoint = circuit[i];

      if (head.distanceSq(currentPoint) != 1) continue CIRCUIT_LOOP;

      for (short j = 0; j < snakeLength; j++) {
        Point part = game.getSnakePart(j);

        if (
          part.equals(currentPoint) ||
          part.getIndex() - headIndex > snakeLength - j
        ) continue CIRCUIT_LOOP;
      }

      int currentDiff = appleIndex - currentPoint.getIndex();
      if (currentDiff < bestDiff) {
        bestIndex = i;
        bestDiff = currentDiff;
      }
    }

    return bestIndex;
  }

  private void simple() {
    Point head = game.getSnakePart(0);
    Point apple = game.getApple();
    Direction direction = game.getDirection();

    byte hx = head.getX();
    byte hy = head.getY();
    byte ax = apple.getX();
    byte ay = apple.getY();

    if (hx < ax && direction != Direction.LEFT) {
      game.faceRight();
    } else if (hx > ax && direction != Direction.RIGHT) {
      game.faceLeft();
    } else if (
      hx == ax &&
      (
        (direction == Direction.DOWN && hy > ay) ||
        (direction == Direction.UP && hy < ay)
      )
    ) {
      game.faceRight();
    } else if (hy < ay && direction != Direction.UP) {
      game.faceDown();
    } else if (hy > ay && direction != Direction.DOWN) {
      game.faceUp();
    } else if (
      hy == ay &&
      (
        (direction == Direction.RIGHT && hx > ax) ||
        (direction == Direction.LEFT && hx < ax)
      )
    ) {
      game.faceDown();
    }
  }
}
