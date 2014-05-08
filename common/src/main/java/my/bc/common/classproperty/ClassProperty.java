package my.bc.common.classproperty;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import my.bc.common.util.ObjectUtil;

/*
 * class property reflects the attributes/getter/setter of a class
 */
@SuppressWarnings( "rawtypes" )
public class ClassProperty
{
  private String name;
  private Class<?> declaringClass;
  private Class<?> propertyRawType;    //A for A< String, Integer >
  private Type[] typeArguments;     //[String, Integer] for A< String, Integer >
  
  public ClassProperty(){}
  
  public ClassProperty( String name, Class declaringClass, Type genericPropertyType )
  {
    setName( name );
    setDeclaringClass( declaringClass );
    setGenericType( genericPropertyType );
  }

  public ClassProperty( String name, Class declaringClass, Class propertyRawType, Type[] typeArguments )
  {
    setName( name );
    setDeclaringClass( declaringClass );
    setPropertyRawType( propertyRawType );
    setTypeArguments( typeArguments );
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Class< ? > getDeclaringClass()
  {
    return declaringClass;
  }

  public void setDeclaringClass( Class< ? > declaringClass )
  {
    this.declaringClass = declaringClass;
  }

  public void setGenericType( Type genericType )
  {
    if( genericType instanceof ParameterizedType )
    {
      setPropertyRawType( (Class<?>)( (ParameterizedType)genericType).getOwnerType() );
      setTypeArguments( ((ParameterizedType)genericType).getActualTypeArguments() );
    }
    else if( genericType instanceof Class )
    {
      setPropertyRawType( (Class<?>)genericType );
    }
  }

  public Class< ? > getPropertyRawType()
  {
    return propertyRawType;
  }

  public void setPropertyRawType( Class< ? > propertyRawType )
  {
    this.propertyRawType = propertyRawType;
  }

  
  public Type[] getTypeArguments()
  {
    return typeArguments;
  }

  public void setTypeArguments( Type[] typeArguments )
  {
    this.typeArguments = typeArguments;
  }

  @Override
  public boolean equals( Object obj )
  {
    if( obj == null )
      return false;
    
    if( obj == this )
      return true;
    
    if( !( obj instanceof ClassProperty ) )
      return false;
    ClassProperty otherProperty = ( ClassProperty )obj;
    
    return ObjectUtil.equals( otherProperty.name, name ) && ObjectUtil.equals( otherProperty.declaringClass, declaringClass ) 
        && ObjectUtil.equals( otherProperty.propertyRawType, propertyRawType );
  }
  
  @Override
  public ClassProperty clone()
  {
    return new ClassProperty( name, declaringClass, propertyRawType, typeArguments );
  }
  
  public void cloneTo( ClassProperty classProperty )
  {
    classProperty.setName( getName() );
    classProperty.setDeclaringClass( getDeclaringClass() );
    classProperty.setPropertyRawType( getPropertyRawType() );
    classProperty.setTypeArguments( getTypeArguments() );
  }
}
