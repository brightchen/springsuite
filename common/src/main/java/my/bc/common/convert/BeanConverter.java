package my.bc.common.convert;

import java.lang.reflect.Method;
import java.util.Collection;

public interface BeanConverter
{
  public <T> T convertBean( Object srcObj, Class<T> destType, CollectionConvertInfo collectionConvertInfo );
  public void copyProperties( Object srcObj, Object destObj );
  public boolean copyProperty( Object srcObj, Method srcGetter, Object destObj, Collection<Method> potentialSetters );

}
