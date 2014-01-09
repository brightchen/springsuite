package my.bc.persistent.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import my.bc.model.BaseEntity;
import my.bc.model.NamedEntity;
import my.bc.persistent.dao.FlexibleDao;

public class FlexibleDaoJpa implements FlexibleDao
{
  protected final Log log = LogFactory.getLog(FlexibleDaoJpa.class);

  protected EntityManager entityManager;

  @PersistenceContext()
  public void setEntityManager(EntityManager entityManager)
  {
    this.entityManager = entityManager;
    this.entityManager.setFlushMode(FlushModeType.COMMIT);

  }

  public EntityManager getEntityManager()
  {
    return this.entityManager;
  }

  @Override
  public <T extends BaseEntity> T getEntityById(Class<T> type, long id)
  {
    T entity = entityManager.find(type, id);

    if (entity == null)
    {
      String msg = type.getName() + "' object with id '" + id + "' not found...";
      log.warn(msg);
      throw new EntityNotFoundException(msg);
    }

    return entity;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public <T extends NamedEntity> T getEntityByName( Class<T> type, String name )
  {
    if( name == null || name.isEmpty() )
      throw new IllegalArgumentException( "Input parameter name should NOT empty." );
    
    Query query = entityManager.createQuery( "from " + type.getName() + " obj where lower(name) = '" + name.toLowerCase() + "'" );
    return (T)query.getSingleResult();
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public <T extends BaseEntity> T loadEntity( T entity )
  {
    if( entity.getId() != null )
      return (T)getEntityById( entity.getClass(), entity.getId() );
    
    if( (entity instanceof NamedEntity) )
    {
      NamedEntity namedEntity = (NamedEntity)entity;
      String name = namedEntity.getName();
      if( name != null && !name.isEmpty() )
        return (T)getEntityByName( namedEntity.getClass(), name );
    }
    
    throw new IllegalArgumentException( "Can only load entity by id or name( for NamedEntity )." );
  }
}
