package my.bc.studio.smartdbtool.test;

import java.util.List;

import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;
import my.bc.studio.smartdbtool.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Tester1
{
  @Autowired( required = true )
  private IUserService userService;

  public static void main( String[] argvs )
  {
    ApplicationContext appContext = SpringBeanConfigurator.DEFAULT.getApplicationContext();
    Tester1 tester = appContext.getBean( Tester1.class );
    
    tester.prepareData();
    List<User> users = tester.queryInsideEntity();
    tester.cleanupData( users );
    
    SpringBeanConfigurator.DEFAULT.closeApplicationContext();
  }
  
  public void prepareData()
  {
    try
    {
      User user = new User();
      user.setFirstName( "firstname" );
      user.setLastName( "lastname" );
      user.setName( "firstnamelastname" );
      user.setEmail( "firstnamelastname@company.com" );
      userService.saveEntity( user );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    System.out.println( "----------------prepareData() done-------------" );
  }
  
  public List<User> queryInsideEntity()
  {
    System.out.println( "----------------queryInsideEntity() started-------------" );
    UserSearchCriteria searchCriteria = new UserSearchCriteria();
    searchCriteria.setFirstName( "firstname" );
    searchCriteria.setLastName( "lastname" );
    List<User> users = userService.findUsers( searchCriteria );
    System.out.println( "got users: " + users.size() );
    for( User user : users )
    {
      System.out.println( user.getFirstName() + " " + user.getLastName() );
    }
    System.out.println( "----------------queryInsideEntity() done-------------" );
    
    return users;
  }
  
  public void cleanupData( List<User> users )
  {
    if( users == null )
      return;
    for( User user : users )
    {
      userService.removeEntityById( User.class, user.getId() );
    }
  }
}
