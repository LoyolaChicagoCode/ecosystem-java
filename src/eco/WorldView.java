package eco;

import java.awt.GridLayout;
import java.awt.Dimension;
import javax.swing.*;

/**
 * This class implements a graphical world view within a Swing frame.
 */
public class WorldView extends JFrame implements View {

  private static final long serialVersionUID = 7145096450058754218L;

  /**
   * This constructor creates a view that visualizes the
   * given cells in a grid with the specified cell size.
   */
  public WorldView(final Cell[][] cells, final int cellSize) {

    final int rows = cells.length;
    final int cols = cells[0].length;

    setTitle("EcoSystem");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(new GridLayout(rows, cols, 1, 1));

    for (int i = 0; i < rows; i ++) {
      for (int j = 0; j < cols; j ++) {
        getContentPane().add(cells[i][j].getView().getComponent());
      }
    }

    setSize(new Dimension(cellSize * cols, cellSize * rows));
    validate();
    setVisible(true);
  }
}