package my.bc.studio.smartdbtool.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import my.bc.smartdbtool.criteria.IQueryCriteria;
import my.bc.smartdbtool.criteria.SmartQueryBuilder;
import my.bc.studio.model.IEntity;
import my.bc.studio.model.INamedEntity;
import my.bc.studio.model.User;


public class GenericJpaDaoService implements IGenericDaoService
{
  private EntityManager entityManager;
  
  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) 
  {
    this.entityManager = entityManager;
  }

  protected EntityManager getEntityManager()
  {
    return entityManager;
  }
  
  @Override
  @SuppressWarnings( "unchecked" )
  public < T extends IEntity > T findEntityById( Class< T > entityClass, Long id )
  {
    String hsql = String.format( "select o from %s o where o.id = %d", entityClass.getName(), id );
    try
    {
      return (T)getEntityManager().createQuery( hsql  ).getSingleResult();
    }
    catch( NoResultException noResult )
    {
      return null;
    }
  }

  public <T extends INamedEntity> T findEntityByName( Class<T> entityClass, String name )
  {
    return findEntityByName( entityClass, name, false );
  }
  
  //precondition: the entity's name field is name
  @Override
  @SuppressWarnings( "unchecked" )
  public <T extends INamedEntity> T findEntityByName( Class<T> entityClass, String name, boolean caseSensitive )
  {
    try
    {
      String hsql;
      if( caseSensitive )
        hsql = String.format( "select o from %s o where o.name = \'%s\'", entityClass.getName(), name );
      else
        hsql = String.format( "select o from %s o where upper( o.name ) = \'%s\'", entityClass.getName(), name.toUpperCase() );
      return (T)getEntityManager().createQuery( hsql  ).getSingleResult();
    }
    catch( NoResultException e )
    {
      return null;
    }
  }

  /*
   * when called by client code, saveEntity() is transactional
   * @see cg.usermanagement.api.IGenericDaoService#saveEntity(cg.model.api.IEntity)
   */
  @Override
  @Transactional( readOnly = false )
  public < T extends IEntity > T saveEntity( T entity )
  {
    return saveEntityInternal( entity );
  }

  protected < T extends IEntity > T saveEntityInternal( T entity )
  {
    entity = getEntityManager().merge( entity );
    getEntityManager().persist( entity );
    return entity;
  }

  @Override
  @Transactional( readOnly = false )
  public <T extends IEntity > void removeEntityById( Class< T > entityClass, Long id )
  {
    removeEntityByIdInternal( entityClass, id );
  }
  
  protected <T extends IEntity > void removeEntityByIdInternal( Class< T > entityClass, Long id )
  {
    T entity = findEntityById( entityClass, id );
    if( entity == null )
      return;
    getEntityManager().remove( entity );
  }

  @SuppressWarnings( "unchecked")
  public < T extends IEntity > List<T> getEntities( Class<T> entityClass )
  {
    return getEntityManager().createQuery( "from " + entityClass.getName()  ).getResultList();
  }
  
  @SuppressWarnings( "unchecked")
  public <T extends IEntity> List<T> findEntities( Class<T> entityClass, IQueryCriteria queryCriteria )
  {
    String query = SmartQueryBuilder.defaultInstance().buildSearchHsql( entityClass, queryCriteria );
    return getEntityManager().createQuery( query  ).getResultList();
  }
}