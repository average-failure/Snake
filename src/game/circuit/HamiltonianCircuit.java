package game.circuit;

import game.helper.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HamiltonianCircuit {

  public static Point[] get24By24() {
    return getCircuit((byte) 24);
  }

  public static Point[] get100By100() {
    return getCircuit((byte) 100);
  }

  public static Point[] getCircuit(byte size) {
    Point[] circuit = new Point[size * size];
    try {
      Scanner sc = new Scanner(
        new BufferedReader(
          new InputStreamReader(
            HamiltonianCircuit.class.getResourceAsStream(
                "/assets/" + size + "x" + size + ".txt"
              )
          )
        )
      );

      while (sc.hasNextLine()) {
        for (short i = 0; i < circuit.length; i++) {
          String[] line = sc.nextLine().trim().split(" ");
          circuit[i] =
            new Point(Byte.parseByte(line[0]), Byte.parseByte(line[1]), i);
        }
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }

    return circuit;
  }

  public static Point[] generateCircuit(byte size) {
    if (size % 2 != 0) {
      System.out.println("No solution exists");
      return null;
    }

    List<Integer> path = new ArrayList<>(0);

    byte j = 0;
    for (byte i = 0; i < size; i++) {
      if (j <= 1) {
        while (j < size - 1) {
          path.add(i * size + j);
          j++;
        }
      } else if (j == size - 1) {
        while (j != 1) {
          path.add(i * size + j);
          j--;
        }
      }

      path.add(i * size + j);
    }

    j = 0;
    for (byte i = 0; i < size; i++) {
      path.add((size - i - 1) * size + j);
    }

    Point[] circuit = new Point[size * size];

    for (short i = 0; i < path.size() - 1; i++) {
      int p = path.get(i);
      circuit[i] = new Point((byte) (p / size), (byte) (p % size), i);
    }

    return circuit;
  }
}
