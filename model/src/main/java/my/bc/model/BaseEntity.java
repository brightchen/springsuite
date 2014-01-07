package my.bc.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * BaseEntity is an entity with id
 * @author Bright Chen
 *
 */
@MappedSuperclass
public class BaseEntity implements Serializable
{
  private static final long serialVersionUID = 1246037378202923907L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  
}
