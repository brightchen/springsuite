package my.bc.studio.model;

//all entity has id, the id should be Long type
public interface IEntity
{
  //when implementing, the mapping field should be "id", or use the getter mapping
  public Long getId();
  public void setId( Long id );
}
