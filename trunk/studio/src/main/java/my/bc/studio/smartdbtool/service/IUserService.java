package my.bc.studio.smartdbtool.service;

import java.util.List;

import my.bc.studio.model.Account;
import my.bc.studio.model.Role;
import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;

public interface IUserService
{
  public User findUserByName( String name );
  public Account findAccountByName( String accountId );
  public Role findRoleByName( String roleName );
  
  public List< User > findUsers( UserSearchCriteria criteria );


}
