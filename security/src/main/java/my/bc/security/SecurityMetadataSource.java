package my.bc.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

public class SecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource
{
  private static LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>>();

  @Autowired
  private ResourceAccessService resourceAccessService;
  
  public SecurityMetadataSource()
  {
    super( initRequestMap() );
  }
  
  /**
   * the order is important, only match the first matched.
   * @return
   */
  private static LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>> initRequestMap()
  {
//    <http    pattern="/**/*.jpg"    security="none" />
//    <http    pattern="/**/*.png"    security="none" />
//    <http    pattern="/**/*.gif"    security="none" />
//    <http    pattern="/**/*.css"    security="none" />
//    <http    pattern="/**/*.js"     security="none" />

    requestMap.put( new AntPathRequestMatcher( "/views/*.jsp" ), 
            Collections.singletonList( (ConfigAttribute)new SecurityConfig( "ROLE_USER" ) ) );
    requestMap.put( new AntPathRequestMatcher( "/views/*.html" ), 
        Collections.singletonList( (ConfigAttribute)new SecurityConfig( "ROLE_USER" ) ) );


    final Collection<ConfigAttribute> emptyAttribute = Collections.emptyList();
    requestMap.put( new AntPathRequestMatcher( "/**/*.jpg" ), emptyAttribute );
    requestMap.put( new AntPathRequestMatcher( "/**/*.png" ), emptyAttribute );
    requestMap.put( new AntPathRequestMatcher( "/**/*.gif" ), emptyAttribute );
    requestMap.put( new AntPathRequestMatcher( "/**/*.css" ), emptyAttribute );
    requestMap.put( new AntPathRequestMatcher( "/**/*.js" ), emptyAttribute );
    requestMap.put( new AntPathRequestMatcher( "/**/*.html" ), emptyAttribute );
//            Collections.singletonList( (ConfigAttribute)new SecurityConfig( "" ) ) );

    
    return requestMap;
  }

//  @Override
//  public boolean supports(Class<?> clazz)
//  {
//    return true;
//  }
//
//  @Override
//  public Collection<ConfigAttribute> getAllConfigAttributes()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }
  
  // return the permission required to access the resource
  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException
  {
    Collection<ConfigAttribute> attributes = super.getAttributes( object );
    return attributes;
    //return resourceAccessService.getResourceAccessAttributes( (FilterInvocation)object );
  }


}