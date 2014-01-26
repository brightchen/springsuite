package my.bc.model.data;

import java.util.HashSet;
import java.util.Set;

public class ErrorDatas
{
  // the success is always false, used by the client side to distinguish this is a error
  final private boolean success = false;
  
  private Set< ValidationErrorData > errors = new HashSet< ValidationErrorData >();

  public ErrorDatas( Set<ValidationErrorData> errors )
  {
    setErrors( errors );
  }
  
  public Set<ValidationErrorData> getErrors()
  {
    return errors;
  }

  public void setErrors(Set<ValidationErrorData> errors)
  {
    this.errors = errors;
  }

  public boolean isSuccess()
  {
    return success;
  }
}
