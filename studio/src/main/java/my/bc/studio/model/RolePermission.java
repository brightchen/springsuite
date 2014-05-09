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

import my.bc.studio.model.IEntity;

/*
 * This is a relationship table between Role and Permission( Feature, Operation )
 */

@Entity
@Table( name = "ROLE_PERMISSION")
public class RolePermission implements IEntity
{
  @SequenceGenerator( name = "ROLE_PERMISSION_SEQ" , sequenceName = "ROLE_PERMISSION_SEQ")
  @Id
  @GeneratedValue( strategy = javax.persistence.GenerationType.AUTO , generator = "ROLE_PERMISSION_SEQ")
  @Column( name = "ID" )
  private Long   id;
  
  @ManyToOne( fetch=FetchType.LAZY ) 
  @JoinColumn( name="ROLE_ID" ) 
  private Role role;
  
  //Feature is not a typical entity yet. change Feature into entity or use featureId
  @Column( name="FEATURE_ID", nullable=false )
  private Long featureId;

  @Column( name="OPERATION_ID", nullable=false )
  @Enumerated
  private Operation operation;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Role getRole()
  {
    return role;
  }

  public void setRole( Role role )
  {
    this.role = role;
  }

  public Feature getFeature()
  {
    Feature feature = new Feature();
    feature.setId( getFeatureId() );
    return feature;
  }

  public void setFeature( Feature feature )
  {
    setFeatureId( feature.getId() );
  }

  public Long getFeatureId()
  {
    return featureId;
  }

  public void setFeatureId( Long featureId )
  {
    this.featureId = featureId;
  }

  public Operation getOperation()
  {
    return operation;
  }

  public void setOperation( Operation operation )
  {
    this.operation = operation;
  }
  
  public Permission getPermission()
  {
    return new Permission( getFeature(), getOperation() );
  }
}
