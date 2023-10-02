package game.frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;

class ModeFrame extends BaseFrame {

  private class MyKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
          System.exit(0);
          break;
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_B:
          new SizeFrame();
          dispose();
          break;
      }
    }
  }

  public ModeFrame() {
    super();
    final JButton game = new JButton("Start Game");
    final JButton ai = new JButton("Start AI");

    game.addActionListener(e -> {
      new GameFrame();
      this.dispose();
    });
    ai.addActionListener(e -> {
      new AIFrame();
      this.dispose();
    });

    this.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(50, 30, 50, 30);
    c.ipadx = 150;
    c.ipady = 100;
    c.gridx = 1;
    c.gridy = 1;
    this.add(game, c);
    c.gridy = 2;
    this.add(ai, c);
    this.setLocationRelativeTo(null);

    this.setTitle("Start Menu");
    this.addKeyListener(new MyKeyAdapter());
  }
}
