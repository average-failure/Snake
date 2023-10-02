package game;

import game.frame.SizeFrame;
import javax.swing.ImageIcon;

public class App {

  public static final ImageIcon ICON = new ImageIcon(
    App.class.getResource("/assets/icon.png")
  );

  private static byte size = 24;

  /**
   * @return the size
   */
  public static byte getSize() {
    return size;
  }

  /**
   * @param size the size to set
   */
  public static void setSize(int size) {
    App.size = (byte) size;
  }

  public static void main(String[] args) throws Exception {
    // for (int i = 0; i < instances.length; i++) {
    // instances[i] = new SnakeAI(SnakeAI.HAMILTONIAN, true, 100);
    // }
    new SizeFrame();
    // new SnakeAI(SnakeAI.HAMILTONIAN, false, 24);
  }
}
