package my.bc.user.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import my.bc.model.NamedEntity;

@Entity
@Table(name = "tuser")
public class User extends NamedEntity
{
  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
  @JoinTable(joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
  //@ForeignKey(name = "FKUserRolesOwner", inverseName = "FKUserRoles")
  private Set<Role> roles = new HashSet<Role>();

}
