package my.bc.common.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtil
{
//  private static final Log log = LogFactory.getLog( ReflectionUtil.class );
  public static final String GETTER_PATTERN = "get.+|is.+";
  public static final String SETTER_PATTERN = "set.+";

  public static final String GETTER_PREFIX_GET = "get";
  public static final String GETTER_PREFIX_IS  = "is";
  public static final String[] GETTER_PREFIXES = { GETTER_PREFIX_GET, GETTER_PREFIX_IS };
  public static final String SETTER_PREFIX = "set";
  
  public static final Class<?>[] NO_PARAMETER = new Class<?>[]{};
  public static final Object[] IGNORE_PARAMETERS = null;     //don't care about the parameters


  public static Set< Method > getMethods( Class<?> clazz, String methodNamePattern, Class<?>[] expectedParameterTypes, int expectedModifiers )
  {
    return getMethods( clazz, null, methodNamePattern, expectedParameterTypes, expectedModifiers );
  }
  
  /*
   * get the methods which match following criteria from class clazz
   * 1. the methods is declared in the class hierarchy [ clazz, rootSuperClass ] inclusively
   * 2. the methods' name is match methodNamePattern
   * 3. the parameter type list is compatible with expectedParameterTypes
   * 4. the methods' modifiers are match expectedModifiers
   */
  public static <T> Set< Method > getMethods( Class<T> clazz, Class< ? super T > rootSuperClass, String methodNamePattern, 
                                              Class<?>[] expectedParameterTypes, int expectedModifiers ) 
  {
    Set< Method > methods = new HashSet< Method >(); 
    
    //Class.getMethods() only returns the public methods
    Method[] allMethods = clazz.getMethods();
    if( Modifier.isProtected( expectedModifiers ) || Modifier.isPrivate( expectedModifiers ) )
    {
      //FIXME: getDeclaredMethods() only returns the methods declared in this class but not the super classes.
      //allMethods = clazz.getDeclaredMethods();    
    }
    if( allMethods == null || allMethods.length == 0 )
      return methods;
    
    for( Method method : allMethods )
    {
      if( rootSuperClass != null )
      {
        // the qualify method should be declared in the class which is sub-class of rootSuperClass inclusively
        if( !rootSuperClass.isAssignableFrom( method.getDeclaringClass() ) )
          continue;
      }
      if( isMethodMetch( method, methodNamePattern, expectedParameterTypes, expectedModifiers ) )
        methods.add( method );
    }
    return methods;
  }

  
  /*
   * get the method from class which the method name is <methodName>, and the parameters are compatible to <parameters> 
   * parameters - the parameter list instead of parameter type list
   */
  public static Method getMethod( Class<?> clazz, String methodName, Class<?>[] parameterTypes )
  {
    Set< Method > methods = getMethods( clazz, methodName, parameterTypes, Modifier.PUBLIC );
    return ( methods == null || methods.size() == 0 ) ? null : methods.iterator().next();
  }
  
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

  public static boolean isMethodMetch( Method method, String methodNamePattern, Class<?>[] expectedParameterTypes, int expectedModifiers )
  {
    if( methodNamePattern != null && methodNamePattern.length() > 0 && !method.getName().matches( methodNamePattern ) )
      return false;
    
    if( !isModifiedMatch( method.getModifiers(), expectedModifiers ) )
      return false;
    
    if( !isParameterTypesCompatible( method.getParameterTypes(), expectedParameterTypes) )
      return false;
    
    return true;
  }
  

  /*
   * check the accessible modifier and others
   */
  public static boolean isModifiedMatch( int modifiers, int expectedModifiers )
  {
    // accessible
    if( Modifier.isPrivate( expectedModifiers ) && !Modifier.isPrivate( modifiers ) )
      return false;
    if( Modifier.isProtected( expectedModifiers ) && !Modifier.isProtected( modifiers ) )
      return false;
    if( Modifier.isPublic( expectedModifiers ) && !Modifier.isPublic( modifiers ) )
      return false;

    //abstract
    if( Modifier.isAbstract( expectedModifiers ) && !Modifier.isAbstract( modifiers ) )
      return false;
    
    //static 
    if( Modifier.isStatic( expectedModifiers ) && !Modifier.isStatic( modifiers ) )
      return false;

    return true;
  }
  
  /*
   * if expectedParameterTypes is null: means the caller don't care about the parameter types
   * if length of expectedParameterTypes is 0: means the method should not have parameter.
   */
  public static boolean isParameterTypesCompatible( Class<?>[] parameterTypes, Class<?>[] expectedParameterTypes )
  {
    if( expectedParameterTypes == null )
    {
      return true;
    }
    if( expectedParameterTypes.length == 0 )
      return ( parameterTypes.length == 0 );
    
    if( expectedParameterTypes == null || expectedParameterTypes.length == 0 )
      return false;
    if( parameterTypes.length != expectedParameterTypes.length )
      return false;
    
    for( int index = 0; index < parameterTypes.length; ++index )
    {
      if( !isParameterTypeCompatible( parameterTypes[index], expectedParameterTypes[index] ) )
        return false;
    }
    return true;
  }
  
  /*
   * precondition: parameterType should NOT be null;
   */
  public static boolean isParameterTypeCompatible( Class<?> parameterType, Class<?> expectedParameterType )
  {
    //we can call set(null)
    //so, isParameterTypeCompatible( any-type, null ) returns true;
    if( expectedParameterType == null )
      return true;
    if( parameterType == null )
      throw new IllegalArgumentException( "Neither parameter type nor expected parameter type should be null." );

    //TODO: need to check primitive etc
    return expectedParameterType.isAssignableFrom( parameterType );
  }
  
  
  public static <T> Set< Field > getFields( Class<T> clazz, Class< ? super T > rootSuperClass )
  {
    Set< Field > allFields = new HashSet< Field >();
    for( Class< ? super T > curClazz = clazz; rootSuperClass.isAssignableFrom( curClazz ); curClazz = curClazz.getSuperclass() )
    {
      //most fields are declared as private, use getDeclaredFields() instead of getFields()
      Field[] fields = clazz.getDeclaredFields();
      allFields.addAll( Arrays.asList( fields ) );
    }
    return allFields;
  }

  @SuppressWarnings( "unchecked")
  public static Field getField( Class<?> clazz, String propertyName, Class<?> type )
  {
    return getField( (Class)clazz, (Class)clazz, propertyName, type );
  }

  public static <T> Field getField( Class<T> clazz, Class< ? super T > rootSuperClass, String propertyName, Class<?> type )
  {
    return getField( getFields( clazz, rootSuperClass ), propertyName, type );
  }
  
  public static <T> Field getField( Set< Field > fields, String propertyName, Class<?> type )
  {
    if( fields == null || fields.size() == 0 )
      return null;
    for( Field  field : fields )
    {
      if( field.getName().equals( propertyName ) && isParameterTypeCompatible( field.getType(), type ) )
        return field;
    }
    return null;
  }
  
  
  public static boolean isTypicalGetterMethod( Method method )
  {
    if( !isGetterMethod( method ) )
      return false;
    if( !isValidPublicMethod( method ) )
      return false;
    
    //getter shouldn't take parameter and have return value
    Class<?>[] parameterTypes = method.getParameterTypes();
    if( parameterTypes != null && parameterTypes.length != 0 )
      return false;
    
    return ( method.getReturnType() != null );
    
  }


  public static boolean isGetterMethod( Method method )
  {
    String methodName = method.getName();
    return methodName.matches( GETTER_PATTERN );
  }

  public static boolean isSetterMethod( Method method )
  {
    String methodName = method.getName();
    return methodName.matches( SETTER_PATTERN );
  }

  public static boolean isValidPublicMethod( Method method )
  {
    int modifiers = method.getModifiers();
    if( Modifier.isAbstract( modifiers ) || !Modifier.isPublic( modifiers ) )
      return false;
    return true;
  }
  public static boolean isTypicalSetterMethod( Method method )
  {
    if( !isSetterMethod( method ) )
      return false;
    if( !isValidPublicMethod( method ) )
      return false;
    
    //setter method should have one parameter and without return type
    Class<?>[] parameterTypes = method.getParameterTypes();
    if( parameterTypes == null || parameterTypes.length != 1 )
      return false;
    
    return ( method.getReturnType() == null );
    
  }
  
  public static String getCorrespondingSetMethodName( String getMethodName )
  {
    for( String prefix : GETTER_PREFIXES )
    {
      if( getMethodName.startsWith( prefix ) )
        return getMethodName.replaceFirst( prefix, SETTER_PREFIX );
    }
    return null;
  }
//  public static String getCorrespondingGetMethodName( String setMethodName )
//  {
//    return setMethodName.replaceFirst( SETTER_PREFIX, GET_METHOD_PREFIX );
//  }

  public static Class<?>[] getParameterTypes( Object[] parameters )
  {
    Class<?>[] parameterTypes = null; 
    if( parameters != null && parameters.length > 0 )
    {
      parameterTypes = new Class<?>[ parameters.length ];
      for( int index = 0; index < parameters.length; ++index )
      {
        Object parameter = parameters[ index ];
        parameterTypes[ index ] = ( parameter == null ? null : parameter.getClass() );
      }
    }
    return parameterTypes;

  }
}
