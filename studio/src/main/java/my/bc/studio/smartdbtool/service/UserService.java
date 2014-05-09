package my.bc.studio.smartdbtool.service;

import java.util.List;

import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;

public class UserService implements IUserService
{
  @Override
  public User findUserByName( String name )
  {
    return findEntityByName( User.class, name, false );
  }

  @Override
  public Account findAccountByName( String name )
  {
    return findEntityByName( Account.class, name, false );
  }

  @Override
  public Role findRoleByName( String roleName )
  {
    return findEntityByName( Role.class, roleName, false );
  }
  
  @Override
  public List< User > findUsers( UserSearchCriteria criteria )
  {
    // most of property is equals ignore case
    IQueryCriteria queryCriteria = QueryCriteriaBuilder.defaultInstance().buildCriteria( User.class, criteria, IQueryCriteriaBuilder.QueryRelation.LikeIgnoreCase );
    String query = SmartQueryBuilder.defaultInstance().buildSearchHsql( User.class, queryCriteria );
    @SuppressWarnings( "unchecked" )
    List<User> users = (List<User>)getEntityManager().createQuery( query  ).getResultList();
    return ViewUtil.entitiesToReadableViews( users, UserSearchView.class );
  }


}
