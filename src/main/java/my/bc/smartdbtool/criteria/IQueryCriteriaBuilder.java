package my.bc.smartdbtool.criteria;

public interface IQueryCriteriaBuilder
{
  // there are some query relation which needs function and change the property value
  public static enum QueryRelation
  {
    EqualIgnoreCase,
    Like,
    LikeIgnoreCase
  }

  /**
   * build the composite query criteria from the criteria and entity
   * @param entityClass: the class of the entity which can get the field of query criteria  
   * @param criteria: the criteria which can get the value of query criteria
   * @param operator: the operator of the query, it must be only parameter operator
   * @return: IQueryCriteria
   */
  @SuppressWarnings( "rawtypes")
  public IQueryCriteria buildCriteria( Class entityClass, Object criteria, Operator operator );
  
  /**
   * build the composite query criteria from the criteria and entity
   * @param entityClass: the class of the entity which can get the field of query criteria  
   * @param criteria: the criteria which can get the value of query criteria
   * @param queryRelation: the relation of the property and property value, it must be only parameter operator
   * @return: IQueryCriteria
   */  
  @SuppressWarnings( "rawtypes")
  public IQueryCriteria buildCriteria( Class entityClass, Object criteria, QueryRelation queryRelation );
}
