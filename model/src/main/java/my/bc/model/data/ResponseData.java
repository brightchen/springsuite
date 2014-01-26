package my.bc.model.data;

/**
 * ResponseData is used for handling response
 * namely, the controller convert beans into clientdata and then convert into response
 * ResponseClientData only need implement the converting from beans to clientdata
 * 
 * An clientdata may be converted from several beans, but it can only have one main bean.
 * 
 * @author Bright Chen
 *
 */
public interface ResponseData<T>
{
  /**
   * convert the bean to data
   * @param bean
   */
  void toData( T bean );
}
