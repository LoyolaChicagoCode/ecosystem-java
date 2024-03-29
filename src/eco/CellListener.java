package eco;

import java.util.EventListener;

/**
 * A listener to cell events.
 * @see CellEvent
 * @see Actor
 */
public interface CellListener extends EventListener {

  /**
   * This method indicates that an actor has entered a cell. 
   * May be called from any thread, usually not the Swing thread.
   */
  void enterCell(CellEvent event);

  /**
   * This method indicates that an actor has left a cell.
   * May be called from any thread, usually not the Swing thread.
   */
  void leaveCell(CellEvent event);
}