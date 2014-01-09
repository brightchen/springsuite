package my.bc.persistent.dao;

import java.util.List;

import my.bc.model.BaseEntity;

/**
 * GenericDao is a dao which bind to certain type of entity
 * instead of use generic PK, use Long as it is best practice
 * 
 * @author Bright Chen
 *
 * @param <T> the type of the entity
 * 
 */
public interface GenericDao<T extends BaseEntity>
{

  /**
   * Generic method used to get all objects of a particular type. This is the
   * same as lookup up all rows in a table.
   * 
   * @return List of populated objects
   */
  public List<T> getAll();

  /**
   * Generic method used to get the count of all objects of a particular type.
   * This is the same as the count of all rows in a table.
   * 
   * @return long count
   */
  public long countAll();

  /**
   * Generic method to get an object based on class and identifier. An
   * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
   * found.
   * 
   * @param id
   *          the identifier (primary key) of the object to get
   * @return a populated object
   * @see org.springframework.orm.ObjectRetrievalFailureException
   */
  public T getById(long id);

  /**
   * get entity by the name 
   * @param name
   * @return
   */
  public T getByName( String name );
  
  /**
   * Checks for existence of an object of type T using the id arg.
   * 
   * @param id
   * @return - true if it exists, false if it doesn't
   */
  public boolean exists(long id);

  /**
   * Generic method to save an object - handles both update and insert.
   * 
   * @param object
   *          the object to save
   */
  public T save(T object);

  /**
   * Generic method to add an object - handles only insert.
   * 
   * @param object
   *          the object to add
   */
  public void add(T object);

  /**
   * Generic method to delete an object based on class and id
   * 
   * @param id
   *          the identifier (primary key) of the object to remove
   */
  public void remove(long id);

  /**
   * Generic method to remove all object from database of the same type
   */
  public void removeAll();

}