package eco.ext;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import eco.AbstractCell;
import eco.CellView;
import eco.ComponentCellView;

/**
 * Any number and combination of actors are allowed to occupy an instance of this class.
 */
public class Open extends AbstractCell {

  private JComponent component = new JComponent() {

    private static final long serialVersionUID = -8690420760057894819L;

    protected void paintComponent(Graphics g) {
      Color color = Color.lightGray;
      if (! getOccupants().isEmpty()) {
        color = Color.orange;
      }
      g.setColor(color);
      g.fill3DRect(0, 0, getWidth() - 1, getHeight() - 1, true);
    }
  };

  private CellView view = new ComponentCellView(component);

  public Open() { super(10000); } // very high capacity

  public String toString() { return "."; }

  public CellView getView() { return view; }
}
