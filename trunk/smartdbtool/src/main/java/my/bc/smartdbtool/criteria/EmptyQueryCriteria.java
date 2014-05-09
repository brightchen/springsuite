package my.bc.smartdbtool.criteria;

import java.util.Map;

/**
 * this class represents an empty query criteria
 * @author bchen
 *
 */
public class EmptyQueryCriteria implements IQueryCriteria
{
  private static EmptyQueryCriteria instance;
  
  public static EmptyQueryCriteria instance()
  {
    if( instance == null )
    {
      synchronized( EmptyQueryCriteria.class )
      {
        if( instance == null )
        {
          instance = new EmptyQueryCriteria();
        }
      }
    }
    return instance;
  }

  @Override
  public String getHsql()
  {
    return "";
  }

  @SuppressWarnings( "rawtypes")
  @Override
  public void addAliases( Map< String, Class > aliases )
  {
  }
}
