package my.bc.common.classproperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;


//import javassist.Modifier;
import my.bc.common.util.CollectionUtil;
import my.bc.common.reflection.ReflectionUtil;

public class ClassPropertyUtil
{
  // where the property come from
  public static enum PropertyCriteria
  {
    Field,            //has field
    Getter,           //has getter method
    Setter,           //has setter method
    GetterAndSetter,  //has both getter and setter
    GetterOrSetter,   //has either getter or setter
    FieldOrGetterOrSetter
  }
  
  public static PropertyCriteria defaultPropertyCriteria = PropertyCriteria.GetterAndSetter;

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static Set< ClassProperty > getClassProperties( Class clazz )
  {
    return getClassProperties( clazz, clazz );
  }
  
  public static <T> Set< ClassProperty > getClassProperties( Class<T> clazz, Class< ? super T > rootSuperClass )
  {
    return getClassProperties( clazz, rootSuperClass, defaultPropertyCriteria );
  }
  
  // return true if the criteria depended on getter
  public static boolean dependedOnGetter( PropertyCriteria criteria )
  {
    return criteria.name().indexOf( PropertyCriteria.Getter.name() ) >= 0;
  }
  public static boolean dependedOnSetter( PropertyCriteria criteria )
  {
    return criteria.name().indexOf( PropertyCriteria.Setter.name() ) >= 0;
  }
  public static boolean dependedOnField( PropertyCriteria criteria )
  {
    return criteria.name().indexOf( PropertyCriteria.Field.name() ) >= 0;
  }

  /*
   * get properties from class clazz and its super classes until rootSuperClass.
   * the reflection getMethod() will get all the methods defined in class hierarchy
   */
  public static <T> Set< ClassProperty > getClassProperties( Class<T> clazz, Class< ? super T > rootSuperClass, PropertyCriteria criteria )
  {
    Set< ClassProperty > getters = null;
    if( dependedOnGetter( criteria ) )
    {
      getters = getClassGetterProperties( clazz, rootSuperClass );
      if( PropertyCriteria.Getter.equals( criteria ) )
        return getters;
    }
    
    Set< ClassProperty > setters = null;
    if( dependedOnSetter( criteria ) )
    {
      setters = getClassSetterProperties( clazz, rootSuperClass );
      if( PropertyCriteria.Setter.equals( criteria ) )
          return setters;
    }

    Set< ClassProperty > fields = null;
    if( dependedOnField( criteria ) )
    {
      fields = getClassFieldProperties( clazz, rootSuperClass );
      if( PropertyCriteria.Field.equals( criteria ) )
          return fields;
    }

    if( PropertyCriteria.GetterAndSetter.equals( criteria ) )
      CollectionUtil.retainAllByValue( getters, setters );
    else if( PropertyCriteria.GetterOrSetter.equals( criteria ) ) // PropertyCriteria.GetterOrSetter
      CollectionUtil.addAllByValue( getters, setters );
    else //FieldOrGetterOrSetter
    {
      CollectionUtil.addAllByValue( getters, setters );
      CollectionUtil.addAllByValue( getters, fields );
    }
    return getters;
  }
  
  public static <T> Set< ClassProperty > getClassGetterProperties( Class<T> clazz, Class< ? super T > rootSuperClass )
  {
    Set< Method > getterMethods = ReflectionUtil.getMethods( clazz, rootSuperClass, ReflectionUtil.GETTER_PATTERN, new Class<?>[]{}, Modifier.PUBLIC );
    return getPropertiesOfGetters( getterMethods );
  }

  /*
   * we don't care about the parameters list of setter right now
   * TODO: check the parameter list of setter
   */
  public static <T> Set< ClassProperty > getClassSetterProperties( Class<T> clazz, Class< ? super T > rootSuperClass )
  {
    Set< Method > setterMethods = ReflectionUtil.getMethods( clazz, rootSuperClass, ReflectionUtil.SETTER_PATTERN, null, Modifier.PUBLIC );
    return getPropertiesOfSetters( setterMethods );
  }
  
  public static <T> Set< ClassProperty> getClassFieldProperties( Class<T> clazz, Class< ? super T > rootSuperClass )
  {
    Set< Field > fields = ReflectionUtil.getFields( clazz, rootSuperClass );
    return getPropertiesOfFields( fields );
  }
  
  public static Set< ClassProperty > getPropertiesOfGetters( Set< Method > methods )
  {
    Set< ClassProperty > properties = new HashSet< ClassProperty >();
    for( Method method : methods )
    {
      ClassProperty property = new ClassProperty( getPropertyName( method ), method.getDeclaringClass(), method.getGenericReturnType() );
      properties.add( property );
    }
    return properties;
  }

  public static Set< ClassProperty > getPropertiesOfSetters( Set< Method > methods )
  {
    Set< ClassProperty > properties = new HashSet< ClassProperty >();
    for( Method method : methods )
    {
      ClassProperty property = new ClassProperty( getPropertyName( method ), method.getDeclaringClass(), method.getParameterTypes()[0] );
      properties.add( property );
    }
    return properties;
  }

  public static Set< ClassProperty > getPropertiesOfFields( Set< Field > fields )
  {
    Set< ClassProperty > properties = new HashSet< ClassProperty >();
    for( Field field : fields )
    {
      ClassProperty property = new ClassProperty( field.getName(), field.getDeclaringClass(), field.getType() );
      properties.add( property );
    }
    return properties;
    
  }
  
  /*
   * get the property name from setter/getter;
   */
  public static String getPropertyName( Method method )
  {
    String methodName = method.getName();
    String propertyName = methodName.substring( 3 );
    return propertyName.substring( 0, 1 ).toLowerCase() + propertyName.substring( 1 );
  }
  
  public static String[] getPotentialGetterNames( String propertyName )
  {
    final String postFix = propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 );
    return new String[]{ ReflectionUtil.GETTER_PREFIXES[0] + postFix, ReflectionUtil.GETTER_PREFIXES[1] + postFix };
  }

  public static String getSetterName( String propertyName )
  {
    return ReflectionUtil.SETTER_PREFIX + propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 );
  }
  
  public static ClassPropertyExt toClassPropertyExt( ClassProperty property )
  {
    if( (property instanceof ClassPropertyExt) && ((ClassPropertyExt)property).valueHasSet() )
    {
      return (ClassPropertyExt)property;
    }
    
    ClassPropertyExt classPropertyExt = new ClassPropertyExt();
    property.cloneTo( classPropertyExt );
    
    //get the getter/setter/field from declaring class 
    Class<?> declaringClass = property.getDeclaringClass();
    Class<?> type = property.getPropertyRawType();
                             
    String propertyName = property.getName();
    
    //get the getter/setter/field from the declaring class
    {
      String[] potentialGetterNames = getPotentialGetterNames( propertyName );
      for( String getterName : potentialGetterNames )
      {
        Method getter = ReflectionUtil.getMethod( declaringClass, getterName, ReflectionUtil.NO_PARAMETER );
        if( getter != null )
        {
          classPropertyExt.setGetter( getter );
          break;    //use the first match
        }
      }
    }
    
    {
      Method setter = ReflectionUtil.getMethod( declaringClass, getSetterName( propertyName ), new Class<?>[]{ type } );
      if( setter != null && ReflectionUtil.isParameterTypeCompatible( setter.getReturnType(), type ) )
        classPropertyExt.setSetter( setter );
    }
    
    {
      Field field = ReflectionUtil.getField( declaringClass, propertyName, type );
      if( field != null )
        classPropertyExt.setField( field );
    }
    
    return classPropertyExt;
  }
  


}
