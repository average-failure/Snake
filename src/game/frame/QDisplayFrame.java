package game.frame;

import game.App;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class QDisplayFrame extends JFrame {

  private static final DecimalFormat DF = new DecimalFormat("#.###");
  private static final byte PAST_AMOUNT = 100;

  private JLabel maxScoreLabel;
  private JLabel averageScoreLabel;
  private JLabel iterationsLabel;
  private JLabel ipsLabel;

  private ArrayList<Short> scores = new ArrayList<>(0);
  private int iterations = 1;
  private double pastTime = System.currentTimeMillis();
  private float ips = 0;
  private float smoothingFactor = 0.2f;

  public QDisplayFrame(GameFrame gameFrame) {
    maxScoreLabel = new JLabel("Max Score: " + 0);
    averageScoreLabel =
      new JLabel("Past " + PAST_AMOUNT + " Scores Average: " + 0.0);
    iterationsLabel = new JLabel("Iterations: " + iterations);
    ipsLabel = new JLabel("Iterations/s: " + 0.0);

    setTitle("AI Stats");
    setIconImage(App.ICON.getImage());
    setSize(300, 300);

    short width = (short) getWidth();
    short height = (short) (getHeight() / 4);

    maxScoreLabel.setPreferredSize(new Dimension(width, height));
    averageScoreLabel.setPreferredSize(new Dimension(width, height));
    iterationsLabel.setPreferredSize(new Dimension(width, height));
    ipsLabel.setPreferredSize(new Dimension(width, height));

    maxScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    averageScoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    iterationsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    ipsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    maxScoreLabel.setHorizontalAlignment(JLabel.CENTER);
    averageScoreLabel.setHorizontalAlignment(JLabel.CENTER);
    iterationsLabel.setHorizontalAlignment(JLabel.CENTER);
    ipsLabel.setHorizontalAlignment(JLabel.CENTER);

    maxScoreLabel.setVerticalAlignment(JLabel.CENTER);
    averageScoreLabel.setVerticalAlignment(JLabel.CENTER);
    iterationsLabel.setVerticalAlignment(JLabel.CENTER);
    ipsLabel.setVerticalAlignment(JLabel.CENTER);

    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    add(maxScoreLabel);
    add(averageScoreLabel);
    add(iterationsLabel);
    add(ipsLabel);

    setLocation(
      gameFrame.getX() - gameFrame.getWidth() / 2,
      gameFrame.getY() + (gameFrame.getHeight() - getHeight()) / 2
    );
    setVisible(true);
  }

  public float updateScore(short score) {
    scores.add(score);

    int size = scores.size();
    float avgScore = (float) scores
      .subList(size - Math.min(size, PAST_AMOUNT), size)
      .stream()
      .mapToDouble(d -> d)
      .average()
      .orElse(0.0);

    maxScoreLabel.setText("Max Score: " + Collections.max(scores));
    averageScoreLabel.setText(
      "Past " + PAST_AMOUNT + " Scores Average: " + DF.format(avgScore)
    );

    return avgScore;
  }

  public void newIteration() {
    iterationsLabel.setText("Iterations: " + (++iterations));
    long currentTime = System.currentTimeMillis();

    if (currentTime <= pastTime) return;

    ipsLabel.setText(
      "Iterations/s: " +
      (
        ips =
          (float) (
            (1 - smoothingFactor) *
            ips +
            smoothingFactor *
            (1000 / (currentTime - pastTime))
          )
      )
    );

    pastTime = currentTime;
  }
}
