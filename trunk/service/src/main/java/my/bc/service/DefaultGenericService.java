package my.bc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import my.bc.model.BaseEntity;
import my.bc.persistent.dao.GenericDao;

public class DefaultGenericService<T extends BaseEntity, D extends GenericDao<T> > implements GenericService<T>
{
  protected D dao;
  
  /**
   * the descendant can override this set method to provide its dao implementation 
   * @param dao
   */
  @Autowired
  public void setDao( D dao )
  {
    this.dao = dao;
  }
  public D getDao()
  {
    return dao;
  }
  
  @Override
  public List<T> getAll()
  {
    return dao.getAll();
  }

  @Override
  public long countAll()
  {
    return dao.countAll();
  }

  @Override
  public T getById(long id)
  {
    return dao.getById(id);
  }

  @Override
  public T getByName(String name)
  {
    return dao.getByName(name);
  }

  @Override
  public boolean exists(long id)
  {
    return dao.exists(id);
  }

  @Override
  public T save(T object)
  {
    return dao.save(object);
  }

  @Override
  public void add(T object)
  {
    dao.add(object);
  }

  @Override
  public void remove(long id)
  {
    dao.remove(id);
  }

  @Override
  public void removeAll()
  {
    dao.removeAll();
  }

}
