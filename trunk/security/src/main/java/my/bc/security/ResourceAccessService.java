package my.bc.security;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;

/**
 * this interface declare interfaces for Resource Access.
 * 
 * @author Bright Chen
 *
 */
public interface ResourceAccessService
{
  public Collection<ConfigAttribute> getResourceAccessAttributes( FilterInvocation filterInvocation );
}
