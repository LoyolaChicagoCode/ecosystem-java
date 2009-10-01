package eco;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * This abstract class represents an actor with autonomous behavior.
 * Concrete subclasses should implement the <code>run()</code> method.
 */
public abstract class LiveActor implements Actor, Runnable, Constants {

  /**
   * The cell this actor is currently occupying.
   */
  private Cell currentCell;

  /**
   * The live thread of this actor.  This thread runs the <code>run()</code>
   * method implemented by concrete subclasses of this class.
   */
  private ExecutorService liveThread;

  /**
   * The work thread of this actor.  This thread handles requests, usually
   * coming from the live thread, to do some work, such as moving to another cell.
   * It is necessary to use a separate thread for this because an attempt to
   * enter another cell might block.  For example, without using a separate
   * work thread, this can cause deadlock
   * if two actors are trying to move into each other's cell of capacity one.
   * A separate work thread also allows an actor to change their mind
   * if another task of higher priority should take precedence over
   * a task on which the work thread is currently working.
   */
  private ExecutorService workThread;

  /**
   * The destination of the most recent attempt to move.
   */
  private Future task = null;

  /**
   * This method indicates whether this actor is still alive.
   */
  protected synchronized boolean isAlive() { return liveThread != null; }

  /**
   * This method brings this actor to life by starting its internal threads.
   */
  public synchronized void start() {
    if (! isAlive()) {
      liveThread = Executors.newFixedThreadPool(1);
      workThread = Executors.newFixedThreadPool(1);
    }
    liveThread.execute(this);
  }

  /**
   * This method kills this actor by stopping its internal threads.
   */
  public synchronized void kill() {
    if (isAlive()) {
      liveThread.shutdown();
      workThread.shutdown();
      liveThread = null;
      workThread = null;
    }
  }

  public synchronized void setCell(Cell cell) { currentCell = cell; }

  public synchronized Cell getCell() { return currentCell; }

  /**
   * This method is used to schedule the runnable for execution by this
   * actor.  If the actor is still waiting for a previously scheduled
   * runnable to execute, then this invocation preempts the previous one.
   */
  protected synchronized void execute(Runnable runnable) {
    if (DEBUG) System.out.println(this + " wants to execute " + runnable);
    if (task != null && ! task.isDone()) {
      task.cancel(true);
    }
    task = workThread.submit(runnable);
  }
  
  /**
   * This method removes this dead actor from the cell it occupied.
   */
  protected synchronized void die() {
    Cell cell = getCell();
    setCell(null);
    cell.leave(this);
  }
}