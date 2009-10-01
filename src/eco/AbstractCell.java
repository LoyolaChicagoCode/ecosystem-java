package eco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * This class implements a cell with a fixed capacity.
 */

public abstract class AbstractCell implements Cell, Constants {

  private static Random random = new Random(System.currentTimeMillis());

  /**
   * The neighboring cells of this cell.
   */
  protected ArrayList<Cell> neighbors = new ArrayList<Cell>();

  /**
   * The current occupants of this cell.
   */
  protected ArrayList<Actor> occupants = new ArrayList<Actor>();

  /**
   * A semaphore to control entry into this limited-capacity cell.
   */
  protected Semaphore sema;

  /**
   * Constructs a cell with the given capacity.
   */
  public AbstractCell(int capacity) {
    sema = new Semaphore(capacity);
  }

  /**
   * This method adds a neighbor to this cell in a thread-safe manner.
   */
  public synchronized void addNeighbor(Cell cell) { neighbors.add(cell); }

  /**
   * This method adds an occupant to this cell in a thread-safe manner.
   */
  protected synchronized void addOccupant(Actor actor) { occupants.add(actor); }

  /**
   * This method removes an occupant from this cell in a thread-safe manner.
   */
  protected synchronized void removeOccupant(Actor actor) { occupants.remove(actor); }

  /**
   * This method waits for space to open in this cell, if necessary, and then
   * places the actor inside the cell.
   */
  public void enter(Actor actor) throws InterruptedException {
    Cell previous = actor.getCell();
    if (this != previous) {
      if (DEBUG) System.out.println(actor + " waiting for space in " + this);
      // if necessary, wait for space in this cell
      sema.acquire();
      // if the actor was somewhere else before, take them out of there
      if (previous != null) {
        previous.leave(actor);
      }
      if (DEBUG) System.out.println(actor + " entering " + this);
      // put the actor into this cell
      actor.setCell(this);
      addOccupant(actor);
      // fire event to tell all occupants of this cell about the new arrival
      enterCell(new CellEvent(this, actor));
      getView().update();
    } else {
      if (DEBUG) System.out.println(actor + " staying in " + this);
    }
  }

  public void leave(Actor actor) {
    if (DEBUG) System.out.println(actor + " leaving " + this);
    removeOccupant(actor);
    // fire event to tell all occupants of this cell about the departure
    leaveCell(new CellEvent(this, actor));
    // release the space that was occupied by the actor who just left
    sema.release();
    getView().update();
  }

  /**
   * This method fires an <code>CellEvent.enterCell</code> event
   * to all occupants of this cell.
   */
  @SuppressWarnings("unchecked")
  public void enterCell(final CellEvent event) {
    List<Actor> currentOccupants;
    synchronized (this) {
      currentOccupants = (List<Actor>) this.occupants.clone();
    }
    for (Actor a : currentOccupants) {
      a.enterCell(event);
    }
  }

  /**
   * This method fires an <code>CellEvent.leaveCell</code> event
   * to all occupants of this cell.
   */
  @SuppressWarnings("unchecked")
  public void leaveCell(final CellEvent event) {
    List<Actor> currentOccupants;
    synchronized (this) {
      currentOccupants = (List<Actor>) this.occupants.clone();
    }
    for (Actor a : currentOccupants) {
      a.enterCell(event);
    }
  }

  /**
   * This method returns a clone of this cell's neighbors.
   */
  @SuppressWarnings("unchecked")
  public synchronized List<Cell> getNeighbors() { return (List<Cell>) neighbors.clone(); }

  /**
   * This method returns a clone of this cell's occupants.
   */
  @SuppressWarnings("unchecked")
  public synchronized List<Actor> getOccupants() { return (List<Actor>) occupants.clone(); }

  public synchronized Cell randomNeighbor() {
    int size = neighbors.size();
    return size == 0 ? null : (Cell) neighbors.get(random.nextInt(size));
  }
}