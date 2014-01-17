package my.bc.common.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil
{
//  private static final Log log = LogFactory.getLog( ReflectionUtil.class );
  
  public static final String GETTER_PREFIX_GET = "get";
  public static final String GETTER_PREFIX_IS  = "is";
  public static final String[] GETTER_PREFIXES = { GETTER_PREFIX_GET, GETTER_PREFIX_IS };
  public static final String SETTER_PREFIX = "set";
  
  public static Set< Method > getGetters( Class<?> clazz )
  {
    Set< Method > allMethods = getBeanMethodsWithPrefix( clazz, GETTER_PREFIXES );

    Set< Method > invalidMethods = new HashSet< Method >();
    for( Method method : allMethods )
    {
      //getter should have no parameters
      if( method.getParameterTypes().length != 0 || method.getReturnType().equals( Void.class ) )
      {
        invalidMethods.add( method );
        continue;
      }
      if( method.getName().startsWith( GETTER_PREFIX_IS ) 
              && !method.getReturnType().equals( Boolean.class ) 
              && !method.getReturnType().getName().equals("boolean") )
      {
        invalidMethods.add( method );
      }
    }

    allMethods.removeAll( invalidMethods );
    
    return allMethods;
  }

  public static Set< Method > getSetters( Class<?> clazz )
  {
    return getBeanMethodsWithPrefix( clazz, SETTER_PREFIX );
  }
  
  public static Set< Method > getBeanMethodsWithPrefix( Class<?> clazz, String methodPrefix )
  {
    return getBeanMethodsWithPrefix( clazz, new String[]{methodPrefix} );
  }
  
  public static Set< Method > getBeanMethodsWithPrefix( Class<?> clazz, String ... methodPrefixes )
  {
    Method[] allMethods = clazz.getMethods();
    
    Set< Method > methods = new HashSet< Method >();
    
    for( Method method : allMethods )
    {
      //bean method should be public, not static and not abstract
      int modifiers = method.getModifiers();
      if( !Modifier.isPublic( modifiers ) || Modifier.isAbstract(modifiers) || Modifier.isStatic(modifiers) 
              || method.isSynthetic() )
        continue;
      
      String methodName = method.getName();
      
      if( methodPrefixes == null || methodPrefixes.length == 0 )
        methods.add( method );
      else
      {
        for( String methodPrefix : methodPrefixes )
        {
          if( methodName.startsWith( methodPrefix ) )
          {
            methods.add( method );
            break;
          }
        }
      }
    }
    
    return methods;
  }
  
//  @SuppressWarnings("unchecked")
//  public static Method getGetter( Field field, Class containerClass )
//  {
//    String getterName = getGetterName( field.getName() );
//    try
//    {
//      return containerClass.getMethod( getterName );
//    }
//    catch( Exception e )
//    {
//      return null;
//    }
//  }
//  
//  public static String getGetterName( String fieldName )
//  {
//    return GETTER_PREFIX + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
//  }
  public static String getSetterName( String fieldName )
  {
    return SETTER_PREFIX + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
  }

  
}
