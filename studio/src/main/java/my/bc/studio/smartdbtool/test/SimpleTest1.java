package my.bc.studio.smartdbtool.test;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;
import my.bc.studio.smartdbtool.service.IUserService;
import my.bc.studio.smartdbtool.service.UserService;

@RunWith(JUnit4.class)
public class SimpleTest1
{
  private static ApplicationContext applicationContext;
  
  @Autowired( required = true )
  private IUserService userService;
  
  @BeforeClass
  public static void classSetUp() 
  {
    applicationContext = SpringBeanConfigurator.DEFAULT.getApplicationContext();
  }

  @AfterClass
  public static void classTearDown() 
  {
    SpringBeanConfigurator.DEFAULT.closeApplicationContext();
  }

  @Before
  public void prepareData()
  {
    
  }
  
  @After
  public void cleanData()
  {
    
  }
  
  @Test
  public void queryInsideEntity()
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
