package my.bc.model.data;

/**
 * RequestData is used for handling request
 * namely, the controller convert request into client data and then convert into beans
 * RequestData only need implement the converting from clientdata to beans
 * 
 * An clientdata may be converted to several beans
 * 
 * @author Bright Chen
 *
 */
public interface RequestData<T>
{
  /**
   * convert data to bean
   * @return the bean converted from this data
   */
  T toBean( T bean );

}
