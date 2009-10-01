package eco.ext;

import java.util.Random;

import eco.CellEvent;
import eco.LiveActor;

/**
 * A class representing mice.
 */
public class Mouse extends LiveActor {

  private static Random random = new Random();
  
  public void run() {
    while (! Thread.interrupted()) {
      try {
        Thread.sleep(random.nextInt(1000));
        // schedule a move for execution
        execute(move);
      } catch (InterruptedException exc) {
        if (DEBUG) System.out.println(this + " live thread interrupted");
        Thread.currentThread().interrupt();
      }
    }
    System.out.println(this + ": squeak!");
  }

  /**
   * A runnable representing a move.
   */
  private Runnable move = new Runnable() {
    public void run() {
      try {
        getCell().randomNeighbor().enter(Mouse.this);
      } catch (InterruptedException e) {
        // if interrupted before entering the cell, then set interrupted flag
        // so that the worker thread can detect this
        Thread.currentThread().interrupt();
      }
    }
  };

  public void enterCell(CellEvent event) {
    if (event.getActor() != this) {
//      System.out.println(this + ": hello " + event.getActor());
    }
  }

  public void leaveCell(CellEvent event) {
    if (event.getActor() != this) {
//      System.out.println(this + ": goodbye " + event.getActor());
    }
  }
}
