package my.bc.studio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import my.bc.studio.model.INamedEntity;

/*
 * don't define the Account role relationship here as the AccountRole is explicitly defined to keep the relationship 
 */

@Entity
@Table( name = "TROLE")
public class Role implements INamedEntity
{
  @SequenceGenerator( name = "TROLE_SEQ" , sequenceName = "TROLE_SEQ")
  @Id
  @GeneratedValue( strategy = javax.persistence.GenerationType.AUTO , generator = "TROLE_SEQ")
  @Column( name = "ID" )
  private Long   id;

  @Column( name = "NAME", length = 50, nullable = false, unique = true, updatable = true )
  private String name;

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
}
