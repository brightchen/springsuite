package my.bc.common.util;

import java.lang.reflect.Method;

import my.bc.common.reflection.ReflectionUtil;

public class EntityUtil
{
  /**
   * copy the entity data from source to destination
   * precondition: the source object and destination object have the same getter/setter method 
   *
   * @param source: the entity which the data copy from
   * @param dest: the entity which the data copy to
   */
  public static void shallowCopyEntity( Object source, Object dest )
  {
    if( source == null || dest == null )
      return;
    
    Class<?> sourceClass = source.getClass();
    Method[] sourceMethods = sourceClass.getMethods();
    
    Class<?> destClass = dest.getClass();

    for( Method sourceGetter : sourceMethods )
    {
      if( !ReflectionUtil.isTypicalGetterMethod( sourceGetter ) )
        continue;
      String destSetterName = ReflectionUtil.getCorrespondingSetMethodName( sourceGetter.getName() );
      try
      {
        Object data = sourceGetter.invoke( source, (Object[])null );
//        if( data == null )
//          continue;   //don't have to call set if data is null;
        Method destSetter = ReflectionUtil.getMethod( destClass, destSetterName, ReflectionUtil.getParameterTypes( new Object[]{ data } ) ); //destClass.getMethod( setMethodName, (Class<?>[])null );
        if( destSetter == null )
          continue;
        destSetter.invoke( dest, data );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }      
    }
  }
  
  
  /**
   *
   * @param entity
   * @return: the property style entity name( first character small case )
   */
  @SuppressWarnings( "rawtypes" )
  public static String getPropertyStyleName( Class entity )
  {
    String simpleName = entity.getSimpleName();
    return simpleName.substring( 0, 1 ).toLowerCase() + simpleName.substring( 1 );
  }

}
