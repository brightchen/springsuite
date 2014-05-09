package my.bc.studio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import my.bc.studio.model.IEntity;

/*
 * explicitly define the AccountRole relationship table 
 */
@Entity
@Table(name="ACCOUNT_ROLE")
public class AccountRole implements IEntity
{
  @SequenceGenerator(name="ACCOUNT_ROLE_SEQ",sequenceName="ACCOUNT_ROLE_SEQ")
  @Id 
  @GeneratedValue(strategy=javax.persistence.GenerationType.AUTO,generator="ACCOUNT_ROLE_SEQ")
  @Column(name="ID")
  private Long id;
  
  @ManyToOne( fetch=FetchType.LAZY ) 
  @JoinColumn( name="ACCOUNT_ID" ) 
  private Account account;

  @ManyToOne( fetch=FetchType.LAZY ) 
  @JoinColumn( name="ROLE_ID" ) 
  private Role role;

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

  public Account getAccount()
  {
    return account;
  }

  public void setAccount( Account account )
  {
    this.account = account;
  }

  public Role getRole()
  {
    return role;
  }

  public void setRole( Role role )
  {
    this.role = role;
  }

}
