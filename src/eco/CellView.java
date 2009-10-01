package eco;

import javax.swing.JComponent;

/**
 * This interface represents a graphical view associated with a cell.
 * @see Cell
 */
public interface CellView {

  /**
   * This method schedules this view for repainting.  It should be invoked
   * if there have been changes to the cell that affect the view.
   */
  void update();

  /**
   * This method returns the Swing component associated with this view.
   */
  JComponent getComponent();
}