package my.bc.studio.smartdbtool.test;

import java.util.List;

import my.bc.studio.model.User;
import my.bc.studio.smartdbtool.searchcrieria.UserSearchCriteria;
import my.bc.studio.smartdbtool.service.IUserService;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:my/bc/studio/studio-*.xml" })     //"file:src/test/"
public class TestCaseJoin
{
  @Autowired( required = true )
  private IUserService userService;
  
  private List<User> users;
  
  @BeforeClass
  public static void classSetUp() 
  {
  }

  @AfterClass
  public static void classTearDown() 
  {
  }

  @Before
  public void prepareData()
  {
    try
    {
      {
        User user = new User();
        user.setFirstName( "guest" );
        user.setLastName( "lastname" );
        user.setName( "guest" );
        user.setEmail( "guest@company.com" );
        user.addRole( RolePopulator.toRole( RolePopulator.RoleEnum.Guest ) );
        
        userService.saveEntity( user );
      }

      {
        User user = new User();
        user.setFirstName( "user" );
        user.setLastName( "lastname" );
        user.setName( "user" );
        user.setEmail( "user@company.com" );
        user.addRole( RolePopulator.toRole( RolePopulator.RoleEnum.User ) );
        
        userService.saveEntity( user );
      }

      {
        User user = new User();
        user.setFirstName( "admin" );
        user.setLastName( "lastname" );
        user.setName( "admin" );
        user.setEmail( "admin@company.com" );
        user.addRole( RolePopulator.toRole( RolePopulator.RoleEnum.Admin ) );
        
        userService.saveEntity( user );
      }

    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    System.out.println( "----------------prepareData() done-------------" );
  }
  
  @After
  public void cleanData()
  {
    if( users == null )
      return;
    for( User user : users )
    {
      userService.removeEntityById( User.class, user.getId() );
    }

  }
  
  @Test
  public void queryInsideEntity()
  {
    try
    {
      System.out.println( "----------------queryInsideEntity() started-------------" );
      UserSearchCriteria searchCriteria = new UserSearchCriteria();
      
      //TODO: search by role
      searchCriteria.setFirstName( "firstname" );
      searchCriteria.setLastName( "lastname" );
      users = userService.findUsers( searchCriteria );
      System.out.println( "got users: " + users.size() );
      for( User user : users )
      {
        System.out.println( user.getFirstName() + " " + user.getLastName() );
      }
      System.out.println( "----------------queryInsideEntity() done-------------" );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}
