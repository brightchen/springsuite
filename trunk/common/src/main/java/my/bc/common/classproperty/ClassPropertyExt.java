package my.bc.common.classproperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassPropertyExt extends ClassProperty
{
  private Method getter;
  private Method setter;
  private Field field;
  
  public ClassPropertyExt(){}
  
  /**
   * get the property value
   * @param object: the class instance this property belongs to
   */
  public Object getPropertyValue( Object object )
  {
    if( !object.getClass().isAssignableFrom( getDeclaringClass()  ) )
    {
      throw new IllegalArgumentException( "The object should be the instanceof " + getDeclaringClass().getName() );
    }
    
    // get the null getter/field and get value
    Object value = null;
    if( getter != null )
    {
      try
      {
        value = getter.invoke( object );
      }
      catch( Exception e )
      {
      }
    }
    if( field != null )
    {
      field.setAccessible( true );
      try
      {
        value = field.get( object );
      }
      catch( Exception e )
      {
      }
    }
    return value;
  }
  
  public boolean valueHasSet()
  {
    return ( getter != null || setter != null || field != null );
  }
  
  public Method getGetter()
  {
    return getter;
  }
  public void setGetter( Method getter )
  {
    this.getter = getter;
  }
  public Method getSetter()
  {
    return setter;
  }
  public void setSetter( Method setter )
  {
    this.setter = setter;
  }
  public Field getField()
  {
    return field;
  }
  public void setField( Field field )
  {
    this.field = field;
  }
  
  
}
