package eco;

import javax.swing.JComponent;

/**
 * A cell view that uses a Swing component.
 * @see javax.swing.JComponent
 */
public class ComponentCellView implements CellView {
  private JComponent component;
  public ComponentCellView(JComponent component) {
    this.component = component;
  }
  public void update() { component.repaint(); }
  public JComponent getComponent() { return component; }
}