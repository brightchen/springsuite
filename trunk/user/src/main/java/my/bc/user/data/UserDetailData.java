package my.bc.user.data;

import java.io.Serializable;
import java.util.Set;

import my.bc.common.convert.CollectionConvertInfo;
import my.bc.user.model.User;

public class UserDetailData  extends UserSimpleData implements Serializable
{
  private static final long serialVersionUID = -8608161497567470547L;

  //User.setPassword will validate password which depended on username.
  //so, should setUsername/setName before setPassword
  private String authType;
  
  private String password;
  private String phone;
  private String other;

  private Set< RoleSimpleData > roles;
  
  @Override
  protected void beforeCopyPropertiesToBean( User user )
  {
    user.setName( getName() );
  }
  
  
  public String getAuthType()
  {
    return authType;
  }
  public void setAuthType(String authType)
  {
    this.authType = authType;
  }

  public String getPassword()
  {
    return password;
  }
  public void setPassword(String password)
  {
    this.password = password;
  }
  public String getPhone()
  {
    return phone;
  }
  public void setPhone(String phone)
  {
    this.phone = phone;
  }
  public String getOther()
  {
    return other;
  }
  public void setOther(String other)
  {
    this.other = other;
  }

  //we don't want to change the Bean, so, this annotation only apply to data class, 
  //and it must apply to getter method 
  @CollectionConvertInfo( targetItemType = RoleSimpleData.class )
  public Set<RoleSimpleData> getRoles()
  {
    return roles;
  }

  public void setRoles(Set<RoleSimpleData> roles)
  {
    this.roles = roles;
  }

}
