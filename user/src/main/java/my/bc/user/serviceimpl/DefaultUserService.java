package my.bc.user.serviceimpl;

import org.springframework.stereotype.Service;

import my.bc.user.BuildinUsers;
import my.bc.user.model.User;
import my.bc.user.service.UserService;

@Service
public class DefaultUserService implements UserService
{
  @Override
  public User findUserByName( String userName )
  {
    //check buildin users
    User user = BuildinUsers.getUserByName( userName );
    if( user != null )
      return user;
    
    //check user from database;
    return null;
  }
}
