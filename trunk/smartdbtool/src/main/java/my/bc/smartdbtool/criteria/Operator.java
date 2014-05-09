package my.bc.smartdbtool.criteria;

public enum Operator
{
  Equal( "=" ),
  Between( "between", NumberOfParameter.Two ),
  GreatThan( ">" ),
  GreatEqual( ">=" ),
  LessThan( "<" ),
  LessEqual( "<=" ),
  Like( "like" ),
  In( "in", NumberOfParameter.Variable )
  ;
  
  private String keyword;
  private NumberOfParameter numberOfParameter;
  
  Operator( String keyword )
  {
    //default is one parameter;
    this( keyword, NumberOfParameter.One );
  }
  
  Operator( String keyword, NumberOfParameter numberOfParameter )
  {
    setKeyword( keyword );
    setNumberOfParameter( numberOfParameter );
  }

  
  public String getKeyword()
  {
    return keyword;
  }

  public void setKeyword( String keyword )
  {
    this.keyword = keyword;
  }

  public NumberOfParameter getNumberOfParameter()
  {
    return numberOfParameter;
  }

  public void setNumberOfParameter( NumberOfParameter numberOfParameter )
  {
    this.numberOfParameter = numberOfParameter;
  }
  
  
}
