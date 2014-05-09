package my.bc.studio.smartdbtool.test;

import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;

public class SimpleTest1
{
  public static void main( String[] argvs )
  {
    UserSearchCriteria searchCriteria = new UserSearchCriteria();
    searchCriteria.setFirstName( "firstname" );
    searchCriteria.setLastName( "lastname" );
    IUserService userService = new UserService();
    userService.findUsers( searchCriteria );
  }

}
