package my.bc.persistent.dao;

import my.bc.model.BaseEntity;
import my.bc.model.NamedEntity;

/**
 * FlexibleDao doesn't bind to certain entity, but the entity type is pass by parameter
 * 
 * @author Bright Chen
 *
 */
public interface FlexibleDao
{
  public <T extends BaseEntity> T getEntityById( Class<T> type, long id );
  
  public <T extends NamedEntity> T getEntityByName( Class<T> type, String name );
  
  public <T extends BaseEntity> T loadEntity( T entity );
}
