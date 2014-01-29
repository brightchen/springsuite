package my.bc.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AccessDecisionManagerImpl implements AccessDecisionManager
{

  public void decide(Authentication authentication, 
          Object object, Collection<ConfigAttribute> configAttributes) 
                  throws AccessDeniedException, InsufficientAuthenticationException
  {
    // configAttributes is null or empty means don't need any permission to access the resource.
    if ( configAttributes == null || configAttributes.isEmpty() )
    {
      return;
    }
    
    for( ConfigAttribute configAttribute : configAttributes )
    {
      if( hasAuthority( authentication, configAttribute ) )
        return;
    }
    throw new AccessDeniedException( "Access Denied. Principal: " + authentication.getName() );
  }

  
  protected boolean hasAuthority( Authentication authentication, ConfigAttribute configAttribute )
  {
    // required permission
    String needPermission = configAttribute.getAttribute();

    // the permissions for this authentication
    for (GrantedAuthority ga : authentication.getAuthorities())
    {
      // simple string compare, we need other logic
      if (needPermission.equalsIgnoreCase( ga.getAuthority()) )
      {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean supports(ConfigAttribute attribute)
  {
    // TODO Auto-generated method stub
    return true;
  }

  public boolean supports(Class<?> clazz)
  {
    // TODO Auto-generated method stub
    return true;
  }

}