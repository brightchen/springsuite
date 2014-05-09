package my.bc.studio.model;


public enum AccountLockReason
{
  // the id should not be modified
  OTHER( 10, "other reason" );
  
  private Long id;
  private String description;
  
  private AccountLockReason( long id, String description )
  {
    this.id = id;
    this.description = description;
  }
  
  public Long getId()
  {
    return id;
  }
  public void setId( Long id )
  {
    this.id = id;
  }

  public String getDescription()
  {
    return description;
  }
  
  public static AccountLockReason fromId( long id )
  {
    for( AccountLockReason lockReason : AccountLockReason.values() )
    {
      if( lockReason.getId() == id )
        return lockReason;
    }
    return null;
  }

}
