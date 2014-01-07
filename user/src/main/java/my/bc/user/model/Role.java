package my.bc.user.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import my.bc.model.NamedEntity;

@Entity
@Table(name = "trole")
public class Role extends NamedEntity
{
  private static final long serialVersionUID = 2036126595901499152L;

}
