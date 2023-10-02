package game.frame;

import game.ai.SnakeAI;
import game.panel.GamePanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends BaseFrame {

  private class MyKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
          System.exit(0);
          break;
        case KeyEvent.VK_R:
          resetGame();
          break;
        case KeyEvent.VK_B:
          if (parent != null) new AIFrame(); else new ModeFrame();
          dispose();
          break;
        default:
          if (parent != null) break;
          panel.updateDirection(e);
          break;
      }
    }
  }

  public GamePanel panel;

  private SnakeAI parent;

  public GameFrame() {
    super();
    panel = new GamePanel(this);
    init(false);
  }

  public GameFrame(SnakeAI parent) {
    super();
    this.parent = parent;
    panel = new GamePanel(this, parent.getMode());
    init(true);
  }

  private void init(boolean ai) {
    this.add(panel);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setTitle("Snake" + (ai ? "AI" : ""));
    this.setResizable(false);
    this.addKeyListener(new MyKeyAdapter());
  }

  public void resetGame() {
    this.remove(panel);
    panel =
      parent == null
        ? new GamePanel(this)
        : new GamePanel(this, parent.getMode());
    this.add(panel);
    this.revalidate();
    if (parent != null) parent.restartGame(this, panel);
  }
}
