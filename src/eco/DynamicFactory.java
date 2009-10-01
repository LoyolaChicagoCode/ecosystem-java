package eco;

import java.util.HashMap;

/**
 * This class implements a dynamic factory.
 */
public class DynamicFactory extends HashMap<Character, Class> implements Factory {

  private static final long serialVersionUID = 4227111024684561418L;

  public void register(char symbol, Class clazz) {
    put(symbol, clazz);
  }

  public Object create(char symbol) throws IllegalAccessException, InstantiationException {
    return get(symbol).newInstance();
  }
}