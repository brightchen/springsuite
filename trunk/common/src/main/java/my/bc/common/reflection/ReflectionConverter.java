package my.bc.common.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import my.bc.common.CollectionConvertInfo;
import my.bc.common.ObjectConverter;

public class ReflectionConverter implements ObjectConverter
{
  private static final Log log = LogFactory.getLog( ReflectionConverter.class );
  
  @Override
  @SuppressWarnings( {"rawtypes","unchecked" } ) 
  public <T> T convertObject( Object srcObj, Class<T> destType, CollectionConvertInfo collectionConvertInfo )
  {
    if( srcObj == null )
      return null;
    
    if( destType == null )
      throw new IllegalArgumentException( "convertObject( Object srcObj, Class<T> destType ): destType should not null." );
    
    Class<?> srcType = srcObj.getClass();

    //can't convert
    if( Collection.class.isAssignableFrom( srcType ) && !Collection.class.isAssignableFrom( destType) )
      return null;
    
    //neither is aggregation class
    if( !Collection.class.isAssignableFrom( srcType ) && !Collection.class.isAssignableFrom( destType) )
    {
      if( destType.isAssignableFrom( srcType ) )
      {
        //direct assign
        return( (T)srcObj );
      }
      else
      {
        return convertObjectByCopingProperties( srcObj, destType );
      }
    }

    try
    {
      //both are Collection
      Collection destCollection = createCollection( ( Class< Collection > )destType, collectionConvertInfo );
      Collection<?> srcCollection = (Collection<?>)srcObj;
      
      return (T)convertCollection( srcCollection, destCollection, collectionConvertInfo );
    }
    catch( Exception e )
    {
      log.warn( e );
      return null;
    }
  }
  
  @SuppressWarnings( "rawtypes" )
  protected Collection createCollection( Class< ? extends Collection > collectionType, CollectionConvertInfo collectionConvertInfo )
  {
    if( collectionConvertInfo != null && collectionConvertInfo.targetCollectionType() != null 
            && !collectionConvertInfo.targetCollectionType().isInterface() )
    {
      try
      {
        return collectionConvertInfo.targetCollectionType().newInstance();
      }
      catch( Exception e )
      {
        log.warn( e );
      }
    }

    if( List.class.isAssignableFrom( collectionType ) )
      return new ArrayList();
    if( Set.class.isAssignableFrom(collectionType) )
      return new HashSet();
    
    throw new IllegalArgumentException( "Does NOT support collection type: " + collectionType.getName() );
  }
  
  @SuppressWarnings(  {"rawtypes","unchecked" } )
  public Collection convertCollection( Collection srcCollection, Collection destCollection, CollectionConvertInfo collectionConvertInfo )
  {
    for( Object item : srcCollection )
    {
      if( item == null )
        destCollection.add( null );
      else
      {
        Class<?> destItemType = getConvertedToTypeForCollectionItem( item, collectionConvertInfo );
        Object destItemValue = convertObject( item, destItemType, collectionConvertInfo );
        destCollection.add( destItemValue );
      }
    
    }
    return destCollection;
    
  }
  
  protected Class<?> getConvertedToTypeForCollectionItem( Object srcItemObj, CollectionConvertInfo collectionConvertInfo )
  {
    if( collectionConvertInfo != null )
      return collectionConvertInfo.targetItemType();
    
    return getConvertedToTypeForCollectionItem( srcItemObj );
  }
  
  protected Class<?> getConvertedToTypeForCollectionItem( Object srcItemObj )
  {
    return srcItemObj.getClass();
  }
  
  
  public <T> T convertObjectByCopingProperties( Object srcObj, Class<T> destType )
  {
    try
    {
      T destObj = destType.newInstance();
      copyProperties( srcObj, destObj );
      
      return destObj;
    }
    catch( Exception e )
    {
      log.warn( e );
      return null;
    }
  }
  
  @Override
  public void copyProperties( Object srcObj, Object destObj )
  {
    if( srcObj == null || destObj == null )
      return;
    
    Set< Method > srcGetters = ReflectionUtil.getGetters( srcObj.getClass() );
    Set< Method > destSetters = ReflectionUtil.getSetters( destObj.getClass() );
    if( srcGetters == null || destSetters == null || srcGetters.isEmpty() || destSetters.isEmpty() )
      return;
    
    for( Method srcGetter : srcGetters )
    {
      String srcGetterName = srcGetter.getName();
      String destSetterName = ReflectionUtil.SETTER_PREFIX + srcGetterName.substring( ReflectionUtil.SETTER_PREFIX.length() );
      
      List< Method > potentialSetters = null;
      for( Method method : destSetters )
      {
        if( method.getName().equals( destSetterName ) )
        {
          if( potentialSetters == null )
            potentialSetters = new ArrayList< Method >();
          potentialSetters.add( method );
        }
      }
      
      if( potentialSetters != null )
        copyProperty( srcObj, srcGetter, destObj, potentialSetters );
    }
  }
  
  /**
   * 
   * @param srcObj
   * @param srcGetter
   * @param destObj
   * @param destSetter
   * @return true if copy success.
   */
  @Override
  public boolean copyProperty( Object srcObj, Method srcGetter, Object destObj, Collection<Method> potentialSetters )
  {
    if( srcObj == null || destObj == null || srcGetter == null || potentialSetters == null || potentialSetters.isEmpty() )
      return false;
    try
    {
      Object getterReturnValue = srcGetter.invoke( srcObj );
      if( getterReturnValue == null )
      {
        //all the setters with same property are ok
        Method setter = (Method)potentialSetters.toArray()[0];
        setter.invoke(destObj, getterReturnValue);
        return true;
      }
      
      CollectionConvertInfo collectionConvertInfo = srcGetter.getAnnotation( CollectionConvertInfo.class );

      Object setterParameterValue = null;
      Method theSetter = null;
      
      // if there are only one setter, convert the getterReturnValue to setterParameterValue;
      if( potentialSetters.size() == 1 )
      {
        theSetter = (Method)potentialSetters.toArray()[0];
        Class<?> setterParameterType = theSetter.getParameterTypes()[0];
        setterParameterValue = convertObject( getterReturnValue, setterParameterType, collectionConvertInfo );
      }
      else
      {
        //there are more than one setter
        //go through the setters and get the first one which parameter matches
        for( Method setter : potentialSetters )
        {
          try
          {
            Class<?> setterParameterType = setter.getParameterTypes()[0];
            if( getterReturnValue == null || setterParameterType.isAssignableFrom( getterReturnValue.getClass() ) )
            {
              setterParameterValue = convertObject( getterReturnValue, setterParameterType, collectionConvertInfo );
              if( setterParameterValue != null )
              {
                theSetter = setter;
                break;
              }
            }
          }catch( Exception e ){}
        }
        
        if( theSetter == null )
        {
          //try to convert the return value of getter to parameter of setter
          for( Method setter : potentialSetters )
          {
            try
            {
              Class<?> setterParameterType = setter.getParameterTypes()[0];
              setterParameterValue = convertObjectByCopingProperties( getterReturnValue, setterParameterType );
              if( setterParameterValue != null )
              {
                //found the setter and converted setterParameterValue
                theSetter = setter;
                break;
              }
            }
            catch( Exception e ){}
          }
        }
      }
      
      if( theSetter == null )
      {
        log.warn( "copyProperty() of getter " + srcGetter.getName() 
                + " failed: No proper setter found. " ); 
        return false;
      }

      theSetter.invoke(destObj, setterParameterValue);
      return true;
    }
    catch( Exception e )
    {
      log.warn( e );
      return false;
    }
  }
}