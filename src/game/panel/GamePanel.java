package game.panel;

import game.App;
import game.ai.SnakeAI;
import game.frame.GameFrame;
import game.helper.Direction;
import game.helper.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

  private enum Result {
    WON,
    LOST,
  }

  public static final byte DELAY = 75;
  private static final short SCREEN_WIDTH = 600;
  private static final short SCREEN_HEIGHT = 600;
  private static final Random RANDOM = new Random();

  private byte cellSize;
  private byte xCells;
  private byte yCells;
  private short gameUnits;
  private byte gap;
  private byte halfGap;

  private Point[] snake;
  private Point apple;
  private short bodyParts = 6;
  private short applesEaten;
  private Direction direction = Direction.random();
  private Direction nextDirection;
  private boolean directionChanged;
  private boolean running;
  private Timer timer;
  private Point[] circuit;
  private Result result;
  private SnakeAI.Mode mode;
  private GameFrame parent;
  private boolean autoReset;

  public GamePanel(GameFrame parent) {
    this(parent, null);
  }

  public GamePanel(GameFrame parent, SnakeAI.Mode mode) {
    this.parent = parent;
    this.mode = mode;

    if (mode == SnakeAI.Mode.Q_LEARNING) autoReset = true;

    if (mode == SnakeAI.Mode.HAMILTONIAN) {
      circuit = SnakeAI.getCircuit();
      gameUnits = (short) circuit.length;
    } else gameUnits = (short) (App.getSize() * App.getSize());

    cellSize = (byte) (SCREEN_WIDTH / Math.sqrt(gameUnits));
    xCells = (byte) (SCREEN_WIDTH / cellSize);
    yCells = (byte) (SCREEN_HEIGHT / cellSize);
    gap = (byte) (cellSize / 10);
    halfGap = (byte) (gap / 2);

    snake = new Point[gameUnits];
    Arrays.fill(
      snake,
      circuit == null
        ? new Point(RANDOM.nextInt(xCells), RANDOM.nextInt(yCells))
        : circuit[RANDOM.nextInt(circuit.length)].clone()
    );

    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.BLACK);
    this.setFocusable(false);

    startGame();
  }

  public void stopAutoReset() {
    autoReset = false;
  }

  /**
   * @return the xCells
   */
  public byte getXCells() {
    return xCells;
  }

  /**
   * @return the yCells
   */
  public byte getYCells() {
    return yCells;
  }

  public void faceRight() {
    if (direction == Direction.LEFT || direction == Direction.RIGHT) return;
    turn(Direction.RIGHT);
  }

  public void faceLeft() {
    if (direction == Direction.LEFT || direction == Direction.RIGHT) return;
    turn(Direction.LEFT);
  }

  public void faceUp() {
    if (direction == Direction.UP || direction == Direction.DOWN) return;
    turn(Direction.UP);
  }

  public void faceDown() {
    if (direction == Direction.UP || direction == Direction.DOWN) return;
    turn(Direction.DOWN);
  }

  public void updateDirection(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_D:
      case KeyEvent.VK_RIGHT:
        faceRight();
        break;
      case KeyEvent.VK_A:
      case KeyEvent.VK_LEFT:
        faceLeft();
        break;
      case KeyEvent.VK_S:
      case KeyEvent.VK_DOWN:
        faceDown();
        break;
      case KeyEvent.VK_W:
      case KeyEvent.VK_UP:
        faceUp();
        break;
      default:
        break;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollision();
    }
    repaint();
  }

  public void actionPerformed(Point moveTo) {
    if (running) {
      move(moveTo);
      checkApple();
      checkCollision();
    }
    repaint();
  }

  public void move(Point moveTo) {
    for (short i = bodyParts; i >= 0; i--) snake[i + 1] = snake[i].clone();

    snake[0] = moveTo.clone();
  }

  /**
   * @return the apple
   */
  public Point getApple() {
    return apple;
  }

  /**
   * @return the number of body parts the snake has
   */
  public short getBodyParts() {
    return bodyParts;
  }

  public Point getSnakePart(int index) {
    return getSnakePart((short) index);
  }

  /**
   * @return the part of the snake at index given
   */
  public Point getSnakePart(short index) {
    if (index < 0 || index >= bodyParts) return null;
    return snake[index];
  }

  /**
   * @return the apples eaten (the score)
   */
  public short getApplesEaten() {
    return applesEaten;
  }

  /**
   * @return the direction of the snake
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * @return the game state
   */
  public boolean isRunning() {
    return running;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void turn(Direction dir) {
    if (directionChanged) nextDirection = dir; else {
      direction = dir;
      directionChanged = true;
    }
  }

  private void startGame() {
    newApple((short) 0);
    running = true;
    if (mode != null) return;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  private void move() {
    for (short i = bodyParts; i >= 0; i--) snake[i + 1] = snake[i].clone();

    if (nextDirection != null && !directionChanged) {
      direction = nextDirection;
      nextDirection = null;
    }

    Point head = snake[0];

    switch (direction) {
      case RIGHT:
        head.moveRight(1);
        break;
      case LEFT:
        head.moveLeft(1);
        break;
      case DOWN:
        head.moveDown(1);
        break;
      case UP:
        head.moveUp(1);
        break;
      default:
        break;
    }

    directionChanged = false;
  }

  private void checkCollision() {
    for (short i = bodyParts; i > 0; i--) {
      if (snake[0].equals(snake[i])) {
        stopGame(Result.LOST);
        return;
      }
    }

    byte x = snake[0].getX();
    byte y = snake[0].getY();

    if (x < 0 || x >= xCells || y < 0 || y >= yCells) stopGame(Result.LOST);
  }

  private void stopGame(Result result) {
    running = false;
    this.result = result;
    if (timer != null) timer.stop();
    if (parent != null && autoReset) parent.resetGame();
  }

  private void checkApple() {
    if (!snake[0].equals(apple)) return;

    bodyParts++;
    applesEaten++;

    if (bodyParts + 1 >= gameUnits) {
      stopGame(Result.WON);
      return;
    }

    newApple((short) 0);
  }

  private void draw(Graphics g) {
    g.setColor(Color.RED);
    g.fillOval(
      apple.getX() * cellSize,
      apple.getY() * cellSize,
      cellSize,
      cellSize
    );

    for (short i = 0; i < bodyParts; i++) {
      if (i == 0) g.setColor(Color.GREEN); else g.setColor(
        new Color(45, 180, 0)
      );

      Direction adjacent1 = null;
      Direction adjacent2 = null;

      if (i < bodyParts - 1) {
        adjacent1 = checkAdjacent(snake[i], snake[i + 1]);
      }
      if (i > 0) {
        adjacent2 = checkAdjacent(snake[i], snake[i - 1]);
      }

      short x = (short) (snake[i].getX() * cellSize);
      short y = (short) (snake[i].getY() * cellSize);

      byte width = cellSize;
      byte height = cellSize;

      if (
        (adjacent1 == Direction.RIGHT && adjacent2 == Direction.LEFT) ||
        (adjacent1 == Direction.LEFT && adjacent2 == Direction.RIGHT)
      ) {
        y += halfGap;
        height -= gap;
      } else if (
        (adjacent1 == Direction.UP && adjacent2 == Direction.DOWN) ||
        (adjacent1 == Direction.DOWN && adjacent2 == Direction.UP)
      ) {
        x += halfGap;
        width -= gap;
      } else {
        if (adjacent1 == Direction.RIGHT || adjacent2 == Direction.RIGHT) {
          x += halfGap;
          width -= halfGap;
        } else if (adjacent1 == Direction.LEFT || adjacent2 == Direction.LEFT) {
          width -= halfGap;
        }
        if (adjacent1 == Direction.UP || adjacent2 == Direction.UP) {
          height -= halfGap;
        } else if (adjacent1 == Direction.DOWN || adjacent2 == Direction.DOWN) {
          y += halfGap;
          height -= halfGap;
        }
      }

      g.fillRect(x, y, width, height);
    }

    displayScore(g);

    if (!running) {
      g.setColor(Color.RED);
      g.setFont(new Font("Ink Free", Font.BOLD, 75));
      String text = "Game Over";
      if (result == Result.WON) {
        g.setColor(Color.GREEN);
        text = "You Won!";
      }
      drawCenterText(g, text, (short) 0);
      short yOffset = (short) (g.getFont().getSize() * 0.6);
      g.setColor(Color.ORANGE);
      g.setFont(new Font("Ink Free", Font.BOLD, 25));
      text = "Press 'r' to restart,";
      drawCenterText(g, text, yOffset);
      yOffset += g.getFont().getSize() * 1.5;
      text = "'b' or 'backspace' to go the previous menu,";
      drawCenterText(g, text, yOffset);
      yOffset += g.getFont().getSize() * 1.5;
      text = "or 'escape' to exit";
      drawCenterText(g, text, yOffset);
    }
  }

  private void drawCenterText(Graphics g, String text, short yOffset) {
    g.drawString(
      text,
      (SCREEN_WIDTH - getFontMetrics(g.getFont()).stringWidth(text)) / 2,
      SCREEN_HEIGHT / 2 + yOffset
    );
  }

  private Direction checkAdjacent(Point center, Point other) {
    Point checker = center.clone();

    checker.moveRight(1);
    if (checker.equals(other)) return Direction.RIGHT;

    checker.moveLeft(2);
    if (checker.equals(other)) return Direction.LEFT;

    checker.moveRight(1);
    checker.moveDown(1);
    if (checker.equals(other)) return Direction.DOWN;

    checker.moveUp(2);
    if (checker.equals(other)) return Direction.UP;

    return null;
  }

  private void displayScore(Graphics g) {
    g.setColor(Color.CYAN);
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics = getFontMetrics(g.getFont());
    String text = "Score: " + applesEaten;
    g.drawString(
      text,
      (SCREEN_WIDTH - metrics.stringWidth(text)) / 2,
      g.getFont().getSize()
    );
  }

  private void newApple(short tries) {
    if (tries > gameUnits) return;

    apple = new Point(RANDOM.nextInt(xCells), RANDOM.nextInt(yCells));

    if (Stream.of(snake).anyMatch(point -> point.equals(apple))) {
      newApple((short) (tries + 1));
      return;
    }

    if (circuit != null) apple.setIndex(Arrays.asList(circuit).indexOf(apple));
  }
}
