package my.bc.studio.model;

public enum Operation implements INamedEntity
{
  CREATE, READ, UPDATE, REMOVE;

  @Override
  public Long getId()
  {
    return (long) ordinal();
  }

  @Override
  public void setId( Long id )
  {
    throw new RuntimeException( "Operation is an enum and not allowed to setId()" );
  }

  @Override
  public String getName()
  {
    return name();
  }

  @Override
  public void setName( String name )
  {
    throw new RuntimeException( "Operation is an enum and not allowed to seName()" );
  }

}
