package my.bc.smartdbtool.criteria;

import java.util.Map;

/**
 * the IQueryCriteria represent the criteria of the query instead of the whole query.
 * @author bchen
 *
 */
public interface IQueryCriteria
{
  /**
   * get the hsql for the criteria
   * for example sum( user1.age between ( 10, 20 )
   * @return: the generated hsql
   */
  public String getHsql();
  
  /*
   * parameter aliases map ( alias ==> beanClass )
   * use addAliases instead of getAliases can improve the performance and eliminate the memory frictions.
   */
  @SuppressWarnings( "rawtypes" )
  public void addAliases( Map< String, Class > aliases ); 
}
