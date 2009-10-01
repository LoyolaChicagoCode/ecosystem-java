package eco;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * This class provides a main method for running an application
 * that uses the eco framework.
 * @see World
 * @see Cell
 * @see Actor
 */
public class Main implements Constants {

  /**
   * This main method reads a configuration from the properties file
   * provided as an optional command-line argument.  It then
   * builds and runs a world from the given configuration.
   */
  public static void main(String[] args) throws Exception {

    // check command-line arguments
    if (args.length > 1) {
      System.err.println(USAGE);
      System.exit(0);
    }

    // look for propFile argument, use default if absent
    String propFile = DEFAULT_PROP_FILE;
    if (args.length == 1) {
      propFile = args[0];
    }

    // read properties
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(propFile));
      if (DEBUG) properties.list(System.out);
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }

    // create application
    new Main(properties);
  }

  public Main(Properties properties) throws Exception {
    Factory factory = new DynamicFactory();

    // load factory entries for cells
    StringTokenizer symbols = new StringTokenizer(properties.getProperty(PROPERTY_CELL_SYMBOLS));
    StringTokenizer classes = new StringTokenizer(properties.getProperty(PROPERTY_CELL_CLASSES));
    while (symbols.hasMoreTokens() && classes.hasMoreTokens()) {
      String symbol = symbols.nextToken();
      String className = classes.nextToken();
      factory.register(symbol.charAt(0), Class.forName(className));
    }

    // load factory entries for actors
    symbols = new StringTokenizer(properties.getProperty(PROPERTY_ACTOR_SYMBOLS));
    classes = new StringTokenizer(properties.getProperty(PROPERTY_ACTOR_CLASSES));
    while (symbols.hasMoreTokens() && classes.hasMoreTokens()) {
      String symbol = symbols.nextToken();
      String className = classes.nextToken();
      factory.register(symbol.charAt(0), Class.forName(className));
    }

    // print factory for debugging
    if (DEBUG) System.out.println(factory);

    // load game map
    List<String> lines = new ArrayList<String>();
    BufferedReader map = new BufferedReader(new FileReader(properties.getProperty(PROPERTY_MAPFILE)));
    int height = 0;
    int width = -1;
    String line = null;
    while ((line = map.readLine()) != null) {
      if (line.length() == 0) break;
      lines.add(line);
      height ++;
      System.out.println(line.length() + ": " + line);
      if (width == -1) {
        width = line.length();
      } else if (width != line.length()) {
        throw new IOException();
      }
    }

    // create cells according to map
    Cell[][] cells = new Cell[height][width];
    for (int i = 0; i < height; i ++) {
      cells[i] = new Cell[width];
      for (int j = 0; j < width; j ++) {
        cells[i][j] = (Cell) factory.create(((String) lines.get(i)).charAt(j));
      }
    }

    // print map for debugging
    if (DEBUG) {
      for (int i = 0; i < cells.length; i ++) {
        for (int j = 0; j < cells[i].length; j ++) {
          System.out.print(cells[i][j]);
        }
        System.out.println();
      }
    }

    // create world
    World world = new CellWorld(cells);

    // create actors and populate world
    ArrayList<Actor> actors = new ArrayList<Actor>();
    StringTokenizer positions = new StringTokenizer(properties.getProperty(PROPERTY_ACTOR_POSITIONS));
    while (positions.hasMoreTokens()) {
      String symbol = positions.nextToken();
      int x = Integer.parseInt(positions.nextToken());
      int y = Integer.parseInt(positions.nextToken());
      Actor actor = (Actor) factory.create(symbol.charAt(0));
      actors.add(actor);
      world.addActor(actor, x, y);
    }

    if (DEBUG) System.out.println("done populating world");

    // create world view
    new WorldView(cells, Integer.parseInt(properties.getProperty(PROPERTY_CELL_SIZE)));

    // start actors
    for (Actor a : actors) {
      a.start();
    }
  }
}