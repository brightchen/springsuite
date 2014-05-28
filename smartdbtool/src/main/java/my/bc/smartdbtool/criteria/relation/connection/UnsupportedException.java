package my.bc.smartdbtool.criteria.relation.connection;

public class UnsupportedException extends RuntimeException
{
  private static final long serialVersionUID = -430539588159049528L;
  
  public UnsupportedException( String message )
  {
    super( message );
  }
}
