package my.bc.studio.smartdbtool.searchcrieria;


import java.io.Serializable;

import my.bc.studio.model.UserStatus;


public class UserSearchCriteria implements Serializable
{
  private static final long serialVersionUID = -1436323169886517734L;

  private String name;
  private String firstName;
  private String lastName;
  private UserStatus status;
  private String phone;
  private String email;
  
  public String getName()
  {
    return name;
  }
  public void setName( String name )
  {
    this.name = name;
  }
  public String getFirstName()
  {
    return firstName;
  }
  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }
  public String getLastName()
  {
    return lastName;
  }
  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }
  public UserStatus getStatus()
  {
    return status;
  }
  public void setStatus( UserStatus userStatus )
  {
    this.status = userStatus;
  }
  public void setStatus( String status )
  {
    for( UserStatus userStatus : UserStatus.values() )
    {
      if( userStatus.name().equals( status ) )
      {
        setStatus( userStatus );
        break;
      }
    }
  }
  
  
  public String getPhone()
  {
    return phone;
  }
  public void setPhone( String phone )
  {
    this.phone = phone;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail( String email )
  {
    this.email = email;
  }
}