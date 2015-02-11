package my.bc.studio.smartdbtool.test;

import java.util.List;




import my.bc.common.classproperty.ClassProperty;
import my.bc.smartdbtool.criteria.Operator;
import my.bc.smartdbtool.criteria.QueryCriteria;
import my.bc.studio.model.Role;
import my.bc.studio.model.RoleEnum;
import my.bc.studio.model.User;
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
        user.addRole( RolePopulator.toRole( RoleEnum.Guest ) );
        
        userService.saveEntity( user );
      }

      {
        User user = new User();
        user.setFirstName( "user" );
        user.setLastName( "lastname" );
        user.setName( "user" );
        user.setEmail( "user@company.com" );
        user.addRole( RolePopulator.toRole( RoleEnum.User ) );
        
        userService.saveEntity( user );
      }

      {
        User user = new User();
        user.setFirstName( "admin" );
        user.setLastName( "lastname" );
        user.setName( "admin" );
        user.setEmail( "admin@company.com" );
        user.addRole( RolePopulator.toRole( RoleEnum.Admin ) );
        
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
      QueryCriteria queryCriteria = new QueryCriteria( new ClassProperty( "name", Role.class, null ), Operator.Equal, "Admin" );
      
      List<User> users = userService.findEntities( User.class, queryCriteria );
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
