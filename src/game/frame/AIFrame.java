package game.frame;

import game.ai.SnakeAI;
import game.ai.qLearning.QLearningAI;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

class AIFrame extends BaseFrame {

  private class MyKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
          System.exit(0);
          break;
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_B:
          new ModeFrame();
          dispose();
          break;
      }
    }
  }

  public AIFrame() {
    super();
    final JButton simple = new JButton("Simple");
    final JButton hamiltonian = new JButton("Hamiltonian");
    final JButton qLearning = new JButton("Q Learning");

    simple.addActionListener(e -> {
      new SnakeAI(SnakeAI.Mode.SIMPLE, 1);
      this.dispose();
    });
    hamiltonian.addActionListener(e -> {
      new SnakeAI(SnakeAI.Mode.HAMILTONIAN);
      this.dispose();
    });
    qLearning.addActionListener(e -> {
      QLearningAI.initTable();
      new SnakeAI(SnakeAI.Mode.Q_LEARNING, 2);
      this.dispose();
    });

    final Dimension size = new Dimension(200, 100);
    simple.setPreferredSize(size);
    hamiltonian.setPreferredSize(size);
    qLearning.setPreferredSize(size);

    final GridLayout layout = new GridLayout();
    layout.setColumns(3);
    layout.setHgap(30);
    layout.setVgap(30);
    this.getRootPane().setBorder(new EmptyBorder(30, 30, 30, 30));
    this.setLayout(layout);
    this.add(simple);
    this.add(hamiltonian);
    this.add(qLearning);
    this.pack();
    this.setLocationRelativeTo(null);

    this.setTitle("AI Selection");
    this.addKeyListener(new MyKeyAdapter());
  }
}
