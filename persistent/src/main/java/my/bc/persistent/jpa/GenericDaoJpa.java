package my.bc.persistent.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.bc.model.BaseEntity;
import my.bc.persistent.dao.GenericDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenericDaoJpa<T extends BaseEntity> implements GenericDao<T>
{
  protected final Log log = LogFactory.getLog(getClass());

  protected EntityManager entityManager;

  protected Class<T> persistentClass;

  public GenericDaoJpa()
  {
  }

  public GenericDaoJpa(Class<T> persistentClass)
  {
    persistentClass = persistentClass;
  }

  @PersistenceContext()
  public void setEntityManager(EntityManager entityManager)
  {
    entityManager = entityManager;
    entityManager.setFlushMode(FlushModeType.COMMIT);

  }

  public EntityManager getEntityManager()
  {
    return entityManager;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> getAll()
  {
    return entityManager.createQuery("select obj from " + persistentClass.getName() + " obj").getResultList();
  }

  @Override
  public long countAll()
  {
    return (Long)entityManager.createQuery("select count(*) from " + persistentClass.getName() + " obj").getSingleResult();
  }

  @Override
  public T getById(long id)
  {
    T entity = (T) entityManager.find(persistentClass, id);

    if (entity == null)
    {
      String msg = persistentClass.getName() + "' object with id '" + id + "' not found...";
      log.warn(msg);
      throw new EntityNotFoundException(msg);
    }

    return entity;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T getByName( String name )
  {
    if( name == null || name.isEmpty() )
      throw new IllegalArgumentException( "Input parameter name should NOT empty." );
    
    Query query = entityManager.createQuery( "from " + persistentClass.getName() + " obj where lower(name) = '" + name.toLowerCase() + "'" );
    return (T)query.getSingleResult();

  }
  
  @Override
  public boolean exists(long id)
  {
    T entity = (T) entityManager.find(persistentClass, id);

    return (entity != null) ? true : false;
  }

  @Override
  public T save(T object)
  {
    return entityManager.merge(object);
  }

  @Override
  public void add(T object)
  {
    entityManager.persist(object);
  }

  @Override
  public void remove(long id)
  {
    entityManager.remove(getById(id));
  }

  @Override
  public void removeAll()
  {
    entityManager.createQuery("delete from " + persistentClass.getName()).executeUpdate();
  }

}