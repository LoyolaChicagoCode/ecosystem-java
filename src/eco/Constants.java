package eco;

/**
 * This interface contains global constants used by the eco framework.
 * TODO replace with static imports
 */
public interface Constants {

  /**
   * A global flag to control debugging output.
   */
  boolean DEBUG = true;

  String USAGE = "usage: eco.Main [ propFile ]";
  String DEFAULT_PROP_FILE = "etc/default.properties";
  String PROPERTY_CELL_CLASSES = "eco.cell.classes";
  String PROPERTY_CELL_SYMBOLS = "eco.cell.symbols";
  String PROPERTY_CELL_SIZE = "eco.cell.size";
  String PROPERTY_ACTOR_SYMBOLS = "eco.actor.symbols";
  String PROPERTY_ACTOR_CLASSES = "eco.actor.classes";
  String PROPERTY_ACTOR_POSITIONS = "eco.actor.positions";
  String PROPERTY_MAPFILE = "eco.mapfile";
}