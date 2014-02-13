package my.bc.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import my.bc.common.util.StringUtil;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

/**
 * this is the default implementation of ResourceAccessService
 * basically, it's mixture of Resource Object and access/operation.
 * 
 * @author Bright Chen
 *
 */
@Service
public class DefaultResourceAccessService implements ResourceAccessService
{
  public Collection<ConfigAttribute> getResourceAccessAttributes( FilterInvocation filterInvocation )
  {
//    String fullRequestUrl = filterInvocation.getFullRequestUrl();
    String requestUrl = filterInvocation.getRequestUrl();

    HttpServletRequest request = filterInvocation.getHttpRequest();
    AccessOperation accessOperation = getAccessOperation( request.getMethod() );
    
    Collection<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
    attributes.add( new ResourceAccessConfigAttribute( requestUrl, accessOperation ) );
    return attributes;
  }
  
  protected AccessOperation getAccessOperation( String httpMethod )
  {
    if( StringUtil.isNullOrEmpty( httpMethod ) )
    {
      throw new IllegalArgumentException( "Invalid http method: " + httpMethod );
    }
    if( httpMethod.equalsIgnoreCase( "GET" ) )
      return AccessOperation.get;
    if( httpMethod.equalsIgnoreCase( "POST" ) )
      return AccessOperation.add;
    if( httpMethod.equalsIgnoreCase( "PUT" ) )
      return AccessOperation.edit;
    if( httpMethod.equalsIgnoreCase( "DELETE" ) )
      return AccessOperation.delete;
    
    throw new IllegalArgumentException( "Invalid http method: " + httpMethod );
  }
          
}
