package eco.ext;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import eco.Actor;
import eco.Cell;
import eco.CellEvent;
import eco.CellView;
import eco.ComponentCellView;

/**
 * No actor is able to enter an instance of this class.
 */
public class Wall implements Cell {

  private JComponent component = new JComponent() {

    private static final long serialVersionUID = 1060952563965718710L;

    protected void paintComponent(Graphics g) {
      g.setColor(Color.darkGray);
      g.fill3DRect(0, 0, getWidth() - 1, getHeight() - 1, true);
    }
  };

  private CellView view = new ComponentCellView(component);

  public void addNeighbor(Cell cell) { }
  @SuppressWarnings("unchecked")
  public List<Cell> getNeighbors() { return Collections.EMPTY_LIST; }
  @SuppressWarnings("unchecked")
  public List<Actor> getOccupants() { return Collections.EMPTY_LIST; }
  public void enter(Actor actor) { } // TODO consider balking instead!
  public void leave(Actor actor) { throw new UnsupportedOperationException(); }
  public void enterCell(CellEvent event) { throw new UnsupportedOperationException(); }
  public void leaveCell(CellEvent event) { throw new UnsupportedOperationException(); }
  public Cell randomNeighbor() { throw new UnsupportedOperationException(); }
  public String toString() { return "#"; }
  public CellView getView() { return view; }
}