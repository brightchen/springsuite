package my.bc.studio.smartdbtool.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import my.bc.smartdbtool.criteria.IQueryCriteria;
import my.bc.smartdbtool.criteria.IQueryCriteriaBuilder;
import my.bc.smartdbtool.criteria.QueryCriteriaBuilder;
import my.bc.smartdbtool.criteria.SmartQueryBuilder;
import my.bc.studio.model.Account;
import my.bc.studio.model.Role;
import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;

@Service
public class UserService implements IUserService
{
  private Session hibernateSession;
  
  public void setHibernateSession( Session hibernateSession )
  {
    this.hibernateSession = hibernateSession;
  }
  
  protected <T> T findByName( Class<T> persistentClass, String name )
  {
    Criteria criteria = hibernateSession.createCriteria( persistentClass ).add( Restrictions.eq( "name", name ) );
    List<T> entities = criteria.list();
    return entities.isEmpty() ? null : entities.get( 0 );
    
  }
  
  @Override
  public User findUserByName( String name )
  {
    return findByName( User.class, name );
  }

  @Override
  public Account findAccountByName( String name )
  {
    return findByName( Account.class, name );
  }

  @Override
  public Role findRoleByName( String name )
  {
    return findByName( Role.class, name );
  }
  
  @Override
  @SuppressWarnings( "unchecked" )
  public List< User > findUsers( UserSearchCriteria criteria )
  {
    // most of property is equals ignore case
    IQueryCriteria queryCriteria = QueryCriteriaBuilder.defaultInstance().buildCriteria( User.class, criteria, IQueryCriteriaBuilder.QueryRelation.LikeIgnoreCase );
    String query = SmartQueryBuilder.defaultInstance().buildSearchHsql( User.class, queryCriteria );
    List<User> users = (List<User>)getEntityManager().createQuery( query  ).getResultList();
    
    return users;
  }


}
