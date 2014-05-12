package my.bc.studio.smartdbtool.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;
import my.bc.studio.smartdbtool.service.IUserService;
import my.bc.studio.smartdbtool.service.UserService;

@Component
public class SimpleTest1
{
  @Autowired( required = true )
  private IUserService userService;
  
  public static void main( String[] argvs )
  {
    final ApplicationContext context = SpringBeanConfigurator.DEFAULT.getApplicationContext();
    SimpleTest1 test = context.getBean( SimpleTest1.class );

    test.run();
  }

  public void run()
  {
    UserSearchCriteria searchCriteria = new UserSearchCriteria();
    searchCriteria.setFirstName( "firstname" );
    searchCriteria.setLastName( "lastname" );
    List<User> users = userService.findUsers( searchCriteria );
    System.out.println( "got users: " + users.size() );
    for( User user : users )
    {
      System.out.println( user.getFirstName() + " " + user.getLastName() );
    }
  }
}
