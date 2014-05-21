package my.bc.studio.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import my.bc.studio.model.INamedEntity;

/*
 * A user can have 0..n accounts, the user's permission is the union of its accounts' permission
 * An actor can login as a User or as an account
 */
@Entity
@Table(name="TUSER")
public class User implements INamedEntity
{
  @SequenceGenerator(name="TUSER_SEQ",sequenceName="TUSER_SEQ")
  @Id 
  @GeneratedValue(strategy=javax.persistence.GenerationType.AUTO,generator="TUSER_SEQ")
  @Column(name="ID")
  private Long id;
  
  @Column( name="NAME", length = 50, nullable = false, unique = true, updatable = false )
  private String name;

  @Column( name="PASSWORD", length = 50 )
  private String password;

  @Column( name = "STATUS" )
  @Enumerated
  private UserStatus status;

  @Column( name="FIRST_NAME", length = 50 )
  private String firstName;

  @Column( name="MIDDLE_NAME", length = 50 )
  private String middleName;

  @Column( name="LAST_NAME", length = 50 )
  private String lastName;

  @Column( name="EMAIL", length = 50 )
  private String email;

  @Column( name="PHONE", length = 20 )
  private String phone;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
  @JoinTable(joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
  //@ForeignKey(name = "FKUserRolesOwner", inverseName = "FKUserRoles")
  private Set<Role> roles = new HashSet<Role>();

  @Override
  public Long getId()
  {
    return id;
  }
  @Override
  public void setId( Long id )
  {
    this.id = id;
  }

  @Override
  public String getName()
  {
    return name;
  }
  @Override
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

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public void setMiddle( String middleName )
  {
    this.middleName = middleName;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail( String email )
  {
    this.email = email;
  }
  public String getPhone()
  {
    return phone;
  }
  public void setPhone( String phone )
  {
    this.phone = phone;
  }
  public UserStatus getStatus()
  {
    return status;
  }
  public void setStatus( UserStatus status )
  {
    this.status = status;
  }
  
  public Set< Role > getRoles()
  {
    return roles;
  }
  public void setRoles( Set< Role > roles )
  {
    this.roles = roles;
  }
  public void addRole( Role role )
  {
    if( roles == null )
      roles = new HashSet<Role>();
    roles.add( role );
  }
  
}
