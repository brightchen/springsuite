package my.bc.user;

import java.util.Collections;
import java.util.Set;

import my.bc.user.model.User;
import my.bc.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService
{
  @Autowired
  private UserService userService;

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    System.out.println("username is " + username);
    User user = userService.getUserByName(username);
    if (user == null)
    {
      throw new UsernameNotFoundException(username);
    }

    // Collection<GrantedAuthority> grantedAuths =
    // obtionGrantedAuthorities(user);

    return user;
  }

  private Set<GrantedAuthority> obtionGrantedAuthorities(User user) 
  {
    return Collections.emptySet();
  }
  
  // 取得用户的权限
//  private Set<GrantedAuthority> obtionGrantedAuthorities(User user) {  
//    Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();  
//    Set<Roles> roles = user.getRoles();  
//      
//    for(Roles role : roles) {  
//        Set<Resources> tempRes = role.getResources();  
//        for(Resources res : tempRes) {  
//            authSet.add(new GrantedAuthorityImpl(res.getName()));  
//           }  
//    }  
//    return authSet;
//  }
}