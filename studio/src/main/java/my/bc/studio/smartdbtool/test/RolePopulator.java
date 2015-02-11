package my.bc.studio.smartdbtool.test;

import java.util.HashSet;
import java.util.List;




import java.util.Set;

import my.bc.studio.model.Role;
import my.bc.studio.model.RoleEnum;
import my.bc.studio.smartdbtool.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolePopulator
{
  
  @Autowired( required = true )
  private IUserService userService;

  public void populateRoles()
  {
    List<Role> roles = userService.getEntities( Role.class );
    
    Set<Role> roleSet = new HashSet<Role>();
    roleSet.addAll( roles );
    
    for( RoleEnum roleEnum : RoleEnum.values() )
    {
      roleSet.add( toRole( roleEnum ) );
    }
    
    for( Role role : roleSet )
    {
      userService.saveEntity( role );
    }
  }
  
  public static Role toRole( RoleEnum roleEnum )
  {
    Role role = new Role();
    role.setName( roleEnum.name() );
    return role;
  }
}
