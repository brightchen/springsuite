package my.bc.studio.smartdbtool.service;

import java.util.List;

import my.bc.smartdbtool.criteria.IQueryCriteria;
import my.bc.studio.model.IEntity;
import my.bc.studio.model.INamedEntity;

public interface IGenericDaoService
{
  public <T extends IEntity> T findEntityById( Class<T> entityClass, Long id );
  public <T extends INamedEntity> T findEntityByName( Class<T> entityClass, String name );
  public <T extends INamedEntity> T findEntityByName( Class<T> entityClass, String name, boolean caseSensitive );
  
  public < T extends IEntity > T saveEntity( T entity );
  public < T extends IEntity > void removeEntityById( Class< T > entityClass, Long id );
  
  public < T extends IEntity > List<T> getEntities( Class<T> entityClass );

  /**
   * find a list of entity by the query criteria.
   * @param entityClass
   * @param queryCriteria
   * @return
   */
  public <T extends IEntity> List<T> findEntities( Class<T> entityClass, IQueryCriteria queryCriteria );
}
