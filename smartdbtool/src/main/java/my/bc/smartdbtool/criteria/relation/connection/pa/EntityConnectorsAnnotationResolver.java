package my.bc.smartdbtool.criteria.relation.connection.pa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import my.bc.common.classproperty.ClassProperty;
import my.bc.common.classproperty.ClassPropertyExt;
import my.bc.common.classproperty.ClassPropertyUtil;
import my.bc.smartdbtool.criteria.relation.EntityLookup;
import my.bc.smartdbtool.criteria.relation.connection.EntityConnector;
import my.bc.smartdbtool.criteria.relation.connection.EntityConnectorAbstractResolver;

/**
 * resolve the entity connectors by parsing the entity persistent annotations
 * this implementation has a lot of limitations, for example, the relation among entities must be represented by persistent annotations
 * and there are some problem such as implicit entities which was introduced by manyToMany mapping
 * 
 * @author brightchen
 * 
 */
@SuppressWarnings( "rawtypes" )
public class EntityConnectorsAnnotationResolver extends EntityConnectorAbstractResolver
{
  private static EntityConnectorsAnnotationResolver defaultInstance;
  protected Map< Class, Set< EntityConnector > > cachedEntityConnectorMap = new HashMap< Class, Set< EntityConnector > >();
  
  public static EntityConnectorsAnnotationResolver defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( EntityConnectorsAnnotationResolver.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new EntityConnectorsAnnotationResolver();
        }
      }
    }
    return defaultInstance;
  }

  private EntityConnectorsAnnotationResolver(){}
  
  @Override
  public boolean isRelationMutual()
  {
    return false;
  }

  /**
   * get connectors( entity, property ) which directly connected to the entity
   * @param entity
   * @return the list of connectors which directly connected to the entity 
   */
  @Override
  public Set< EntityConnector > getDirectConnectors( Class entity )
  {
    if( cachedEntityConnectorMap.containsKey( entity ) )
    {
      //this entity already cached
      cachedEntityConnectorMap.get( entity );
    }
    
    Set< EntityConnector > entityConnectors = new HashSet< EntityConnector >();
    
    //check all the relationship annotations
    Set< ClassProperty >  properties = ClassPropertyUtil.getClassProperties( entity );
    if( properties == null || properties.isEmpty() )
    {
      //better to throw exception?
      return Collections.emptySet();
    }
    
    for( ClassProperty property : properties )
    {
      ClassPropertyExt propertyExt = ClassPropertyUtil.toClassPropertyExt( property );
      
      Field field = propertyExt.getField();
      EntityConnector entityConnector = getEntityConnectorFrom( propertyExt, field );
      
      if( entityConnector == null )
      {
        Method getter = propertyExt.getGetter();
        entityConnector = getEntityConnectorFrom( propertyExt, getter );
        
        if( entityConnector == null )
        {
          Method setter = propertyExt.getSetter();
          entityConnector = getEntityConnectorFrom( propertyExt, setter );
        }
      }
      
      if( entityConnector != null )
        entityConnectors.add( entityConnector );
    }
    
    cachedEntityConnectorMap.put( entity, entityConnectors );
    return entityConnectors;
  }
  
  public EntityConnector getEntityConnectorFrom( ClassProperty property, final Field field )
  {
    if( field == null )
      return null;
    return getEntityConnectorFrom( property, 
                                   new RelationAnnotationProvider()
                                    {
                                      @Override
                                      public OneToOne getOneToOne()
                                      {
                                        return field.getAnnotation( OneToOne.class );
                                      }

                                      @Override
                                      public OneToMany getOneToMany()
                                      {
                                        return field.getAnnotation( OneToMany.class );
                                      }

                                      @Override
                                      public ManyToOne getManyToOne()
                                      {
                                        return field.getAnnotation( ManyToOne.class );
                                      }

                                      @Override
                                      public ManyToMany getManyToMany()
                                      {
                                        return field.getAnnotation( ManyToMany.class );
                                      }
                                      
                                    });
  }
  
  public EntityConnector getEntityConnectorFrom( ClassProperty property, final Method getterOrSetter )
  {
    if( getterOrSetter == null )
      return null;
    return getEntityConnectorFrom( property, 
                                   new RelationAnnotationProvider()
                                    {
                                      @Override
                                      public OneToOne getOneToOne()
                                      {
                                        return getterOrSetter.getAnnotation( OneToOne.class );
                                      }

                                      @Override
                                      public OneToMany getOneToMany()
                                      {
                                        return getterOrSetter.getAnnotation( OneToMany.class );
                                      }

                                      @Override
                                      public ManyToOne getManyToOne()
                                      {
                                        return getterOrSetter.getAnnotation( ManyToOne.class );
                                      }

                                      @Override
                                      public ManyToMany getManyToMany()
                                      {
                                        return getterOrSetter.getAnnotation( ManyToMany.class );
                                      }
                                    });
  }
  
  public EntityConnector getEntityConnectorFrom( ClassProperty property, RelationAnnotationProvider annotationProvider )
  {
    EntityConnector entityConnector;
    
    {
      OneToOne o2o = annotationProvider.getOneToOne();
      if( o2o != null )
      {
        entityConnector = getEntityConnectorFor( property, o2o );
        if( entityConnector != null )
          return entityConnector;
      }
    }
    
    {
      OneToMany o2m = annotationProvider.getOneToMany();
      if( o2m != null )
      {
        entityConnector = getEntityConnectorFor( property, o2m );
        if( entityConnector != null )
          return entityConnector;
      }
    }

    {
      ManyToOne m2o = annotationProvider.getManyToOne();
      if( m2o != null )
      {
        entityConnector = getEntityConnectorFor( property, m2o );
        if( entityConnector != null )
          return entityConnector;
      }
    }

    {
      ManyToMany m2m = annotationProvider.getManyToMany();
      if( m2m != null )
      {
        entityConnector = getEntityConnectorFor( property, m2m );
        if( entityConnector != null )
          return entityConnector;
      }
    }
    return null;
  }

  
  public EntityConnector getEntityConnectorFor( ClassProperty property, OneToOne o2o )
  {
    if( o2o == null )
      return null;
    //this property connected to field type's id property
    return new EntityConnector( property, getIdProperty( property.getPropertyRawType() ) );
  }

  public EntityConnector getEntityConnectorFor( ClassProperty property, OneToMany o2m )
  {
    //should we support it?
    return null;
  }

  public EntityConnector getEntityConnectorFor( ClassProperty property, ManyToOne m2o )
  {
    if( m2o == null )
      return null;
    //this property connected to field type's id property
    return new EntityConnector( property, getIdProperty( property.getPropertyRawType() ) );
  }

  public EntityConnector getEntityConnectorFor( ClassProperty property, ManyToMany m2m )
  {
    if( m2m == null )
      return null;
    //get the type of another entity
    Class targetEntityType = m2m.targetEntity();
    if( targetEntityType == null )
    {
      //get the target type according to the property type
      Class<?> fieldType = property.getPropertyRawType();
      if( !Collection.class.isAssignableFrom( fieldType ) )
      {
        throw new RuntimeException( "the field type of ManyToMany should be a Collection." );
      }
      
      //get the type of collection
      if( !Collection.class.isAssignableFrom( property.getPropertyRawType() ) )
      {
        //it should be error
      }
      Type[] typeArguments = property.getTypeArguments();
      if( typeArguments == null || typeArguments.length != 1 )
      {
        //error
      }
      targetEntityType = (Class)typeArguments[0];
    }
    return new EntityConnector( property, getIdProperty( targetEntityType ) );
  }

  
  /**
   * get all entities of this resolver
   * @return
   */
  @Override
  public Set< Class > getAllEntities()
  {
    return EntityLookup.defaultInstance().getAllEntityClasses();
  }

  protected ClassProperty getIdProperty( Class< ? > entityClass )
  {
    ClassProperty property = new ClassProperty();
    property.setDeclaringClass( entityClass );
    property.setName( "id" );
    property.setPropertyRawType( Long.class );
    return property;
  }
}
