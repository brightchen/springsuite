package my.bc.smartdbtool.criteria.relation;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.reflections.Reflections;

import my.bc.common.reflection.ReflectionsBuilder;
import my.bc.common.util.CollectionUtil;

@SuppressWarnings( "rawtypes" )
public class EntityLookup
{
  private static EntityLookup defaultInstance;
  
  //cache all entities;
  private Set< Class > allEntities = null;
  
  public static EntityLookup defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( EntityConnectorsAnnotationResolver.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new EntityLookup();
        }
      }
    }
    return defaultInstance;
  }

  private EntityLookup(){}
  
  /**
   * 
   * @return the clone of the allEntities in case the client change the allEntities
   */
  public Set< Class > getAllEntityClasses()
  {
    if( allEntities != null )
      return CollectionUtil.shallowCloneTo( allEntities, new HashSet< Class >() );
    
    synchronized( this )
    {
      if( allEntities != null )
        return CollectionUtil.shallowCloneTo( allEntities, new HashSet< Class >() );
      
      allEntities = new HashSet< Class >();

      Reflections reflections = ReflectionsBuilder.defaultInstance().buildAnnotationReflections();
      
      Set< Class<?> > entityClasses = reflections.getTypesAnnotatedWith( Entity.class );
      for( Class<?> entityClass : entityClasses )
      {
        allEntities.add( entityClass );
      }
      return allEntities;
    }
  }
}
