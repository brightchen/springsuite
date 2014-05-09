package my.bc.studio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import my.bc.studio.model.INamedEntity;

/*
 * don't define the Account role relationship here as the AccountRole is explicitly defined to keep the relationship 
 */

@Entity
@Table(name="TACCOUNT")
public class Account implements INamedEntity
{
  @SequenceGenerator(name="TACCOUNT_SEQ",sequenceName="TACCOUNT_SEQ")
  @Id 
  @GeneratedValue(strategy=javax.persistence.GenerationType.AUTO,generator="TACCOUNT_SEQ")
  @Column(name="ID")
  private Long id;

  @Column( name="NAME", length = 50, nullable = false, unique = true, updatable = false )
  private String name;

  @Column( name="PASSWORD", length = 50 )
  private String password;

  @ManyToOne( fetch=FetchType.LAZY ) 
  @JoinColumn( name="USER_ID" ) 
  private User user;

  @Column( name = "STATUS", nullable = false )
  @Enumerated
  private AccountStatus status;
  
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

  public String getPassword()
  {
    return password;
  }
  public void setPassword( String password )
  {
    this.password = password;
  }

  public User getUser()
  {
    return user;
  }
  public void setUser( User user )
  {
    this.user = user;
  }

  public AccountStatus getStatus()
  {
    return status;
  }
  public void setStatus( AccountStatus status )
  {
    this.status = status;
  }

}
