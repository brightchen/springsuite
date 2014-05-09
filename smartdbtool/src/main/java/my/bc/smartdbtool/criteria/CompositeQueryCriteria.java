package my.bc.smartdbtool.criteria;

import java.util.Map;

/*
 * this class implement the query which composed by two queries
 */
public class CompositeQueryCriteria implements IQueryCriteria
{
  private IQueryCriteria criteria1;
  private IQueryCriteria criteria2;
  private CriteriaOperator criteriaOperator;

  /**
   * build CompositeQueryCriteria for criterias with operator criteriaOperator
   * @param criteriaOperator: the operator among the criterias
   * @param criterias: the criterias, which length should be great than 2
   * @return
   */
  public static CompositeQueryCriteria buildQuery( CriteriaOperator criteriaOperator, IQueryCriteria ... criterias )
  {
    if( criterias == null || criteriaOperator == null )
      return null;
    if( criterias.length < 2 )
      throw new IllegalArgumentException( "the length of criterias should great than or equal to 2" );
    
    CompositeQueryCriteria compositeCriteria = new CompositeQueryCriteria( criterias[0], criterias[1], criteriaOperator );
    for( int index = 2; index < criterias.length; ++index )
    {
      compositeCriteria = new CompositeQueryCriteria( compositeCriteria, criterias[index], criteriaOperator );
    }
    return compositeCriteria;
  }
  
  public CompositeQueryCriteria(){}
  
  public CompositeQueryCriteria( IQueryCriteria criteria1, IQueryCriteria criteria2, CriteriaOperator criteriaOperator )
  {
    setCriteria1( criteria1 );
    setCriteria2( criteria2 );
    setCriteriaOperator( criteriaOperator );
  }
  
  @Override
  public String getHsql()
  {
    return String.format( "( %s " + criteriaOperator.name() + " %s )", criteria1.getHsql(), criteria2.getHsql() );
  }
  
  @Override
  @SuppressWarnings( "rawtypes" )
  public void addAliases( Map< String, Class > aliases ) 
  {
    criteria1.addAliases( aliases );
    criteria2.addAliases( aliases );
  }


  public IQueryCriteria getCriteria1()
  {
    return criteria1;
  }

  public void setCriteria1( IQueryCriteria criteria1 )
  {
    this.criteria1 = criteria1;
  }

  public IQueryCriteria getCriteria2()
  {
    return criteria2;
  }

  public void setCriteria2( IQueryCriteria criteria2 )
  {
    this.criteria2 = criteria2;
  }

  public CriteriaOperator getCriteriaOperator()
  {
    return criteriaOperator;
  }

  public void setCriteriaOperator( CriteriaOperator criteriaOperator )
  {
    this.criteriaOperator = criteriaOperator;
  }
  
  
}
