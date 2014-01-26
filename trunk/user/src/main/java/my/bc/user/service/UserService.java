package my.bc.user.service;

import java.util.List;

import my.bc.user.model.User;

public interface UserService
{
  public User getUserByName( String userName );
  public List<User> getAllUsers();
  public User getUserById( long id );
  public void validateUser( User user );
  public User saveUser( User user );
  public void removeUser( long userId );
  public void disableUser( long userId );
  public void enableUser( long userId );
}
