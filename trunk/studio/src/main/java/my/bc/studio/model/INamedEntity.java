package my.bc.studio.model;

//typical entity is a type of entity which has name
//the name maybe unique or maybe not
//don't assume it is unique
public interface INamedEntity extends IEntity
{
  public String getName();
  public void setName( String name );
}
