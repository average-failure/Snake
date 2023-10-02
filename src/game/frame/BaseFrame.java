package game.frame;

import game.App;
import javax.swing.JFrame;

class BaseFrame extends JFrame {

  public BaseFrame() {
    this.setIconImage(App.ICON.getImage());
    this.setSize(600, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    this.setFocusable(true);
    this.requestFocusInWindow();
  }
}
