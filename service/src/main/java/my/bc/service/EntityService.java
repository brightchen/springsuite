package my.bc.service;

import my.bc.model.BaseEntity;

public interface EntityService
{
  public <T extends BaseEntity> T getEntityById( Class<T> entityClass, long id );
  
  /**
   * load entity which identified by <entity> by entity's id
   * @param entity
   * @return
   */
  public <T extends BaseEntity> T loadEntity( T entity );
  
  /**
   * load entities which directly related to <entity>
   * @param entity
   * @return
   */
  public <T extends BaseEntity> T loadDirectlyRelatedEntities( T entity );

}
