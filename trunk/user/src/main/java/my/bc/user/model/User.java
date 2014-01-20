package my.bc.user.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import my.bc.model.NamedEntity;

@Entity
@Table(name = "tuser")
public class User extends NamedEntity implements UserDetails
{
  private static final long serialVersionUID = 3258805813786186060L;

  private String password;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String notes;
  
  //user status
  private boolean enabled;
  private boolean locked;
  private boolean expired;
  
  
  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
  @JoinTable(joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
  //@ForeignKey(name = "FKUserRolesOwner", inverseName = "FKUserRoles")
  private Set<Role> roles = new HashSet<Role>();

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public String getNotes()
  {
    return notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  public boolean isEnabled()
  {
    return enabled;
  }

  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  public Set<Role> getRoles()
  {
    return roles;
  }

  public void setRoles(Set<Role> roles)
  {
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    // TODO Auto-generated method stub
    return null;
  }

  // UserDetails interface
  @Override
  public String getUsername()
  {
    return getName();
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return (!expired);
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return (!locked);
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return (!expired);
  }

  
}
