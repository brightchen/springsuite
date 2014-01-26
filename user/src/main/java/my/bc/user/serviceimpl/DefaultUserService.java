package my.bc.user.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.bc.service.DefaultGenericService;
import my.bc.user.BuildinUsers;
import my.bc.user.dao.UserDao;
import my.bc.user.model.User;
import my.bc.user.service.UserService;

@Service
public class DefaultUserService extends DefaultGenericService< User, UserDao > implements UserService
{
  @Autowired
  public void setDao( UserDao userDao )
  {
    super.setDao(userDao);
  }
  
  @Override
  public User getUserByName( String userName )
  {
    //check buildin users
    User user = BuildinUsers.getUserByName( userName );
    if( user != null )
      return user;
    
    //check user from database;
    return getByName(userName);
  }
  
  public List<User> getAllUsers()
  {
    List< User > users = new ArrayList< User >();
    Collections.addAll( users, BuildinUsers.getBuildinUsers() );
    users.addAll( getDao().getAll() );
    return users;
  }
  
  public User getUserById( long id )
  {
    return getDao().getById(id);
  }
  
  public void validateUser( User user )
  {
    
  }
  
  public User saveUser( User user )
  {
    validateUser( user );
    user = getDao().save( user );
    return user;
  }
  
  public void removeUser( long userId )
  {
    getDao().remove(userId);
  }
  
  public void disableUser( long userId )
  {
    
  }
  
  public void enableUser( long userId )
  {
    
  }

}
