package eco;

/**
 * This interface represents a factory that supports the dynamic
 * association of symbols with classes and the creation of objects
 * by providing the associated symbol.
 * @see java.lang.Class
 */
public interface Factory {

  /**
   * This method creates an instance of the class associated with the
   * given symbol.
   */
  Object create(char symbol)
      throws IllegalAccessException, InstantiationException;

  /**
   * This method associates the given symbol with a class.
   */
  void register(char symbol, Class clazz);
}