package my.bc.studio.model;

/*
 * the feature can be an enum if only for UserManagemet module as we know how many features can be when developing. 
 * but for the multiple modules environment, we don't the features of other modules.
 * we should have a mechanism to support register features when the system startup. 
 * And the id of the features should not be changed even if new module added.
 * 
 * feature is a common model, it can be used by all modules
 */

public class Feature implements INamedEntity
{
  private Long id;
  
  private String name;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }
}