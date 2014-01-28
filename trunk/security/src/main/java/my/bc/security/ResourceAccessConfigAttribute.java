package my.bc.security;

import my.bc.common.util.StringUtil;

import org.springframework.security.access.ConfigAttribute;

/**
 * this class keep a mixture of Resource Object and access/operation.
 * 
 * @author Bright Chen
 *
 */
public class ResourceAccessConfigAttribute implements ConfigAttribute
{
  private static final long serialVersionUID = 5840943572979434782L;

  public static final String SEPERATOR = "_";
  public static final String ALL = "*";
  
  private String resourceName;
  private AccessOperation accessOperation;
  
  public ResourceAccessConfigAttribute(){}
  
  public ResourceAccessConfigAttribute( String resourceName, AccessOperation accessOperation )
  {
    setResourceName( resourceName );
    setAccessOperation( accessOperation );
  }
  
  @Override
  public String getAttribute()
  {
    return composeConfigAttribute();
  }

  /**
   * the format of ConfigAttribute is <Resource>_<Access>
   * 
   * @return
   */
  protected String composeConfigAttribute()
  {
    String attribute = StringUtil.isNullOrEmpty( resourceName ) ? ALL : resourceName;
    attribute += SEPERATOR;
    attribute += accessOperation == null ? ALL : accessOperation.name();
    return attribute;
  }
  
  public String getResourceName()
  {
    return resourceName;
  }

  public void setResourceName(String resourceName)
  {
    this.resourceName = resourceName;
  }

  public AccessOperation getAccessOperation()
  {
    return accessOperation;
  }

  public void setAccessOperation(AccessOperation accessOperation)
  {
    this.accessOperation = accessOperation;
  }

  
}
