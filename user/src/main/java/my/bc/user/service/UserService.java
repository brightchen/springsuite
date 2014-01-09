package my.bc.user.service;

import my.bc.user.model.User;

public interface UserService
{
  public User findUserByName( String userName );
}
