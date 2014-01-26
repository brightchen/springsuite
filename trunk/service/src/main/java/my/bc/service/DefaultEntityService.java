package my.bc.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.persistence.Transient;

import my.bc.model.BaseEntity;
import my.bc.persistent.dao.FlexibleDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultEntityService implements EntityService
{
  private static final Log log = LogFactory.getLog( DefaultEntityService.class );
  
  @Autowired
  private FlexibleDao flexibleDao;
  
  public <T extends BaseEntity> T getEntityById( Class<T> entityClass, long id )
  {
    if( entityClass == null )
      throw new IllegalArgumentException( "The entityClass should NOT null." );
    return (T)flexibleDao.getEntityById( entityClass, id );
  }
  
  /**
   * only reload entity itself, it will not touch any related bean
   * @param entity
   * @return
   */
  @SuppressWarnings( "unchecked" ) 
  @Override
  public <T extends BaseEntity> T loadEntity( T entity )
  {
    if( entity == null )
      return entity;
    
    Long id = entity.getId();
    if( id == null )
      return entity;
    
    entity = (T)flexibleDao.getEntityById( entity.getClass(), id );
    return entity;
  }
  
  /**
   * reload all entity which directly related to the input entity.
   * it will not reload the entity itself
   * @param entity
   * @return
   */
  @Override
  @SuppressWarnings( "rawtypes" ) 
  public <T extends BaseEntity> T loadDirectlyRelatedEntities( T entity )
  {
    Class entityClass = entity.getClass();
    Field[] allFields = entityClass.getDeclaredFields();
    if( allFields == null || allFields.length == 0 )
      return entity;
    
    for( Field field : allFields )
    {
      if( isInvalidEntity( field ) )
        continue;
      
      Class fieldType = field.getType();
      if( BaseEntity.class.isAssignableFrom( fieldType ) )
      {
        loadEntityField( field, entity );
        continue;
      }
      if( Collection.class.isAssignableFrom( fieldType ) )
      {
        loadEntityCollectionField( field, entity );
        continue;
      }
    }
    
    return entity;
  }
  
  boolean isInvalidEntity( Field field )
  {
    if( field.isSynthetic() )
      return true;
    int modifiers = field.getModifiers();
    if( Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) )
      return true;
    if( field.getAnnotation( Transient.class ) != null )
      return true;
    
    return false;
  }
  
  /**
   * load the 
   * @param field
   * @param containerEntity the entity this field belongs to
   */
  protected <T extends BaseEntity> void loadEntityField( Field field, T containerEntity )
  {
    if( isInvalidEntity( field ) )
      return;

    //change directly to field instead of getter, as getter might change the value;
    boolean fieldAccessiable = field.isAccessible();

    try
    {
      if( !fieldAccessiable )
        field.setAccessible(true);
      
      BaseEntity fieldValue = (BaseEntity)field.get( containerEntity );
      fieldValue = loadEntity( fieldValue );
      field.set( containerEntity, fieldValue );
      
    }
    catch( Exception e )
    {
      log.warn( e );
      return;
    }
    finally
    {
      if( !fieldAccessiable )
        field.setAccessible(false);
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected <T extends BaseEntity> void loadEntityCollectionField( Field collectionField, T containerEntity )
  {
    if( isInvalidEntity( collectionField ) )
      return;
    
    Class fieldType = collectionField.getType();
    if( !Collection.class.isAssignableFrom( fieldType ) )
    {
      return;
    }

    //change directly to field instead of getter, as getter might change the value;
    boolean fieldAccessiable = collectionField.isAccessible();

    try
    {
      if( !fieldAccessiable )
        collectionField.setAccessible(true);
      
      Collection fieldValue = (Collection)collectionField.get( containerEntity );
      if( fieldValue == null || fieldValue.isEmpty() )
        return;
      
      Object[] itemArray = fieldValue.toArray();
      if( !( itemArray[0] instanceof BaseEntity ) )
      {
        //check type of first item is enough as the type suppose be same.
        return;
      }
      
      fieldValue.clear();
      
      for( Object itemValue : itemArray )
      {
        BaseEntity entity = loadEntity( (BaseEntity)itemValue );
        fieldValue.add(entity);
      }
    }
    catch( Exception e )
    {
      log.warn( e );
      return;
    }
    finally
    {
      if( !fieldAccessiable )
        collectionField.setAccessible(false);
    }
  }
}
