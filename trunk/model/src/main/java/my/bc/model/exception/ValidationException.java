package my.bc.model.exception;

import java.util.HashSet;
import java.util.Set;

import my.bc.model.data.ValidationErrorData;

/**
 * The ValidationException is usually used by client side to display to the user.
 * We
 * @author BrightChen
 *
 */
public class ValidationException extends RuntimeException
{
  private static final long serialVersionUID = 2782688379583325128L;
  
  private Set< ValidationErrorData > errors = new HashSet< ValidationErrorData >();
  
  public ValidationException( Set< ValidationErrorData > errors )
  {
    setErrors( errors );
  }
  
  public ValidationException( String field, String errorCode )
  {
    addError( new ValidationErrorData( field, errorCode ) );
  }

  public ValidationException( String field, String errorCode, String message )
  {
    addError( new ValidationErrorData( field, errorCode, message ) );
  }

  public Set<ValidationErrorData> getErrors()
  {
    return errors;
  }
  public void setErrors(Set<ValidationErrorData> errors)
  {
    this.errors = errors;
  }

  public void addError( ValidationErrorData error )
  {
    errors.add(error);
  }
  
}