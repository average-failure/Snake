package game.frame;

import game.App;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SizeFrame extends BaseFrame {

  public SizeFrame() {
    super();
    this.setTitle("Size Menu");

    final JButton x24 = new JButton("24 x 24");
    final JButton x100 = new JButton("100 x 100");

    x24.setPreferredSize(new Dimension(100, 100));
    x100.setPreferredSize(new Dimension(100, 100));

    x24.addActionListener(e -> {
      App.setSize(24);
      new ModeFrame();
      this.dispose();
    });
    x100.addActionListener(e -> {
      App.setSize(100);
      new ModeFrame();
      this.dispose();
    });

    int width = getWidth() / 20;
    int height = getHeight() / 20;

    final JLabel label = new JLabel("Choose the size of the game");
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setHorizontalTextPosition(JLabel.CENTER);
    label.setPreferredSize(new Dimension(getWidth(), getHeight() / 5));
    label.setFont(new Font("Papyrus", Font.PLAIN, width));

    final GridLayout layout = new GridLayout();
    layout.setHgap(width);
    layout.setVgap(height);

    final JPanel panel = new JPanel();
    panel.setBorder(new EmptyBorder(0, width, height, width));
    panel.setLayout(layout);
    panel.add(x24);
    panel.add(x100);

    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    add(label);
    add(panel);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
