package my.bc.studio.smartdbtool.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
    userService.findUsers( searchCriteria );
  }
}
