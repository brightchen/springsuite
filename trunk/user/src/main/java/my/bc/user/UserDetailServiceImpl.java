package my.bc.user;

import my.bc.user.dao.UserDao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {  
  
private UserDao userDao;  
public UserDao getUsersDao() {  
    return userDao;  
}  

public void setUsersDao(UserDao userDao) {  
    this.userDao = userDao;  
}  
  
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
    System.out.println("username is " + username);  
    Users users = this.userDao.findByName(username);  
    if(users == null) {  
        throw new UsernameNotFoundException(username);  
    }  
    Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(users);  
      
    boolean enables = true;  
    boolean accountNonExpired = true;  
    boolean credentialsNonExpired = true;  
    boolean accountNonLocked = true;  
      
    User userdetail = new User(users.getAccount(), users.getPassword(), enables, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuths);  
    return userdetail;  
}  
  
//取得用户的权限  
private Set<GrantedAuthority> obtionGrantedAuthorities(Users user) {  
    Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();  
    Set<Roles> roles = user.getRoles();  
      
    for(Roles role : roles) {  
        Set<Resources> tempRes = role.getResources();  
        for(Resources res : tempRes) {  
            authSet.add(new GrantedAuthorityImpl(res.getName()));  
s           }  
    }  
    return authSet;  
}  
}  