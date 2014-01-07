package my.bc.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * NamedEntity is a BaseEntity with an unique and non-empty name
 * 
 * @author Bright Chen
 *
 */
@MappedSuperclass
public class NamedEntity extends BaseEntity
{
  private static final long serialVersionUID = -5637504817752540310L;

  @Column(nullable = false, unique = true)
  private String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  
}
