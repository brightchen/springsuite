package my.bc.common;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * convert from one object to another object 
 * 
 * @author Bright Chen
 *
 */
public interface ObjectConverter
{
  public <T> T convertObject( Object srcObj, Class<T> destType, CollectionConvertInfo collectionConvertInfo );
  public void copyProperties( Object srcObj, Object destObj );
  public boolean copyProperty( Object srcObj, Method srcGetter, Object destObj, Collection<Method> potentialSetters );

}
