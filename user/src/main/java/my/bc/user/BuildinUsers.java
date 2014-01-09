package my.bc.user;

import my.bc.user.model.User;

/**
 * Buildin Users are the users which embeded in the system
 * 
 * @author Bright Chen
 *
 */
public class BuildinUsers
{
  public static final User ADMIN = new User();

  static
  {
    ADMIN.setName( "ADMIN" );
    ADMIN.setPassword( "admin123" );
    ADMIN.setEnabled(true);
    ADMIN.setEmail( "ADMIN@123.com" );
    ADMIN.setFirstName( "ADMIN" );
    ADMIN.setLastName( "buildin" );
  }

  private static final User users[] = { ADMIN };
  
  public static User[] getBuildinUsers()
  {
    return users;
  }
  
  public static boolean isBuildinUser( String userName )
  {
    for( User user : getBuildinUsers() )
    {
      if( user.getName().equalsIgnoreCase(userName) )
        return true;
    }
    return false;
  }
  
  public static User getUserByName( String userName )
  {
    for( User user : getBuildinUsers() )
    {
      if( user.getName().equalsIgnoreCase(userName) )
        return user;
    }
    return null;
  }
  
}
