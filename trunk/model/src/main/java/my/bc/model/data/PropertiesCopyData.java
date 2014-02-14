package my.bc.model.data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import my.bc.common.convert.BeanConverter;
import my.bc.common.convert.CollectionConvertInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertiesCopyData<T> implements ResponseData<T>, RequestData<T>, Serializable
{
  private static final long serialVersionUID = -4296896528348537768L;

  private static final Log log = LogFactory.getLog( PropertiesCopyData.class );
  
  @Autowired
  private BeanConverter beanConverter;
  
  public void toData( T bean)
  {
    beforeCopyPropertiesFromBean( bean );
    copyPropertiesFromBean( bean );
    afterCopyPropertiesFromBean( bean );
  }
  
  public T toBean(T bean)
  {
    beforeCopyPropertiesToBean( bean );
    copyPropertiesToBean( bean );
    afterCopyPropertiesToBean( bean );
    return bean;
  }

  protected void copyPropertiesToBean( T bean )
  {
    beanConverter.copyProperties( this, bean );
  }
  
  @SuppressWarnings( {"rawtypes","unchecked" } )
  public <I> I convertObject( Object srcObj, Class<I> destType, CollectionConvertInfo collectionConvertInfo )
  {
    if( srcObj == null )
      return null;
    
    try
    {
      if( srcObj instanceof PropertiesCopyData )
      {
        I destValue = destType.newInstance();
        return (I)( (PropertiesCopyData)srcObj ).toBean( destValue );
      }
      
      if( PropertiesCopyData.class.isAssignableFrom( destType ) )
      {
        //dest is data, src is bean
        PropertiesCopyData destValue = (PropertiesCopyData)destType.newInstance();
        destValue.toData( srcObj );
        return (I)destValue;
      }
      
      return beanConverter.convertBean( srcObj, destType, collectionConvertInfo );
    }
    catch( Exception e )
    {
      log.warn( e );
      return null;
    }
  }

  protected Class<?> getConvertedToTypeForCollectionItem( Object srcItemObj, CollectionConvertInfo collectionConvertInfo )
  {
    // convert to bean
    if( srcItemObj instanceof PropertiesCopyData )
      return getBeanTypeForData( srcItemObj.getClass() );
    
    return getConvertedToTypeForCollectionItem( srcItemObj, collectionConvertInfo );
  }
  
  protected Class<?> getConvertedToTypeForCollectionItem( Object srcItemObj )
  {
    if( srcItemObj instanceof PropertiesCopyData )
      return getBeanTypeForData( srcItemObj.getClass() );

    return srcItemObj.getClass();
  }

  @SuppressWarnings("rawtypes")
  protected Class<?> getBeanTypeForData( Class dataType )
  {
    Type superGenericType = dataType.getGenericSuperclass();
    return (Class)((ParameterizedType)superGenericType).getActualTypeArguments()[0];
  }
  /**
   * copy properties from bean to data
   * @param bean
   */
  protected void copyPropertiesFromBean( T bean )
  {
    if( bean == null )
      return;
    beanConverter.copyProperties( bean, this );
  }
  
  protected void beforeCopyPropertiesFromBean( T bean ){};
  protected void afterCopyPropertiesFromBean( T bean ){};

  protected void beforeCopyPropertiesToBean( T bean ){};
  protected void afterCopyPropertiesToBean( T bean ){};
}
