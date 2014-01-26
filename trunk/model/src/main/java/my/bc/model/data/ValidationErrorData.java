package my.bc.model.data;

import javax.xml.bind.ValidationException;

public class ValidationErrorData
{
  private String field;   //it is optional
  private String errorCode;   //maybe the server should return errorCode and the client translate the erroCode
  private String message;
  
  public ValidationErrorData( String field, String errorCode )
  {
    setField( field );
    setErrorCode( errorCode );
  }

  public ValidationErrorData( String field, String errorCode, String errorMessage )
  {
    this( field, errorCode );
    setMessage( errorMessage );
  }
  
  public String getField()
  {
    return field;
  }
  public void setField(String field)
  {
    this.field = field;
  }
  public String getErrorCode()
  {
    return errorCode;
  }
  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }
  public String getMessage()
  {
    return message;
  }
  public void setMessage(String message)
  {
    this.message = message;
  }

  @Override
  public boolean equals( Object obj )
  {
    if( obj == this )
      return true;
    if( obj == null )
      return false;
    if( !(obj instanceof ValidationException) )
      return false;
    
    ValidationErrorData other = (ValidationErrorData)obj;
    if( getField() != null )
      return getField().equalsIgnoreCase( other.getField() );
    
    //field is null;
    if( other.getField() != null )
      return false;
    
    if( getErrorCode() == other.getErrorCode() )
      return true;
    if( getErrorCode() != null )
      return getErrorCode().equalsIgnoreCase( other.getErrorCode() );
    
    //message is null;
    return ( other.getErrorCode() == null );
  }
}
