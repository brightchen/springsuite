package my.bc.smartdbtool.criteria;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import my.bc.common.classproperty.ClassProperty;
import my.bc.common.classproperty.ClassPropertyExt;
import my.bc.common.classproperty.ClassPropertyUtil;
import my.bc.common.util.ConvertUtil;
import my.bc.common.util.ObjectUtil;
import my.bc.common.util.StringUtil;

public class QueryCriteriaBuilder implements IQueryCriteriaBuilder
{
  
  private static QueryCriteriaBuilder defaultInstance;
  
  public static QueryCriteriaBuilder defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( QueryCriteriaBuilder.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new QueryCriteriaBuilder();
        }
      }
    }
    return defaultInstance;
  }
  
  private QueryCriteriaBuilder(){}

  /**
   * build the composite query criteria from the criteria and entity
   * @param entityClass: the class of the entity which can get the field of query criteria  
   * @param criteria: the criteria which can get the value of query criteria
   * @param operator: the operator of the query, it must be only parameter operator
   * @return: IQueryCriteria
   */
  @Override
  @SuppressWarnings( { "rawtypes" } )  
  public IQueryCriteria buildCriteria( Class entityClass, Object criteria, Operator operator )
  {
    return buildCriteria( entityClass, criteria, new RelationWrapper( operator ) );
  }

  @Override
  @SuppressWarnings( { "rawtypes" } )  
  public IQueryCriteria buildCriteria( Class entityClass, Object criteria, QueryRelation queryRelation )
  {
    return buildCriteria( entityClass, criteria, new RelationWrapper( queryRelation ) );
  }

  @SuppressWarnings( "rawtypes")
  public IQueryCriteria buildCriteria( Class entityClass, Object criteria, RelationWrapper relationWrapper )
  {
    if( entityClass == null || criteria == null )
      return null;
    
    Set< ClassProperty > entityProperties = ClassPropertyUtil.getClassProperties( entityClass );
    Set< ClassProperty > criteriaProperties = ClassPropertyUtil.getClassProperties( criteria.getClass() );
    if( entityProperties == null || entityProperties.isEmpty() || criteriaProperties == null || criteriaProperties.isEmpty() )
      return null;
    
    List< IQueryCriteria > queryCriterias = new ArrayList< IQueryCriteria >();
    for( ClassProperty entityProperty : entityProperties )
    {
      ClassProperty criteriaProperty = getCompatibleProperty( entityProperty, criteriaProperties );
      //build the query criteria for this property
      IQueryCriteria queryCriteria = buildCriteria( entityProperty, criteriaProperty, criteria, relationWrapper );
      if( queryCriteria != null )
        queryCriterias.add( queryCriteria );
    }
    final int queryCriteriasSize = queryCriterias.size();
    if( queryCriteriasSize == 0)
    {
      //no criteria, return empty criteria
      EmptyQueryCriteria.instance();
    }
    if( queryCriteriasSize == 1 )
    {
      //return simple criteria
      return queryCriterias.get( 0 );
    }
    
    return CompositeQueryCriteria.buildQuery( CriteriaOperator.And, queryCriterias.toArray( new IQueryCriteria[ queryCriterias.size() ] ) );
  }
  
  
  /**
   * build the criteria for a property
   * @param entityProperty: the entity property
   * @param criteriaProperty: the criteria property
   * @param criteria: the criteria which can get the criteria value
   * @param operator: the operator of the query, it must be only parameter operator
   * @return
   */
  protected IQueryCriteria buildCriteria( ClassProperty entityProperty, ClassProperty criteriaProperty, Object criteria, RelationWrapper relationWrapper )
  {
    if( criteriaProperty == null )
      return null;
    ClassPropertyExt criteriaPropertyExt = ClassPropertyUtil.toClassPropertyExt( criteriaProperty );
    Object propertyValue = criteriaPropertyExt.getPropertyValue( criteria );
    if( isIgnoreProperty( criteriaProperty, propertyValue ) )
      return null;  // propertyValue equals means not need to care this property;
    return buildCriteria( entityProperty, relationWrapper, propertyValue );    
  }
  
  
  protected IQueryCriteria buildCriteria( ClassProperty entityProperty, RelationWrapper relationWrapper, Object propertyValue )
  {
    //for operator
    {
      Operator operator = relationWrapper.getOperator();
      if( operator != null )
      {
        return buildCriteria( entityProperty, operator, propertyValue );
      }
    }
    
    //for query relation
    {
      QueryRelation queryRelation = relationWrapper.getQueryRelation();
      if( queryRelation == null )
        return null;
      
      //all following query relation requires property value is String
      if( !( propertyValue instanceof String ) )
      {
        throw new RuntimeException( "property value expected to be String for QueryRelation." );
      }
      
      if( QueryRelation.EqualIgnoreCase.equals( queryRelation ) )
      {
        return buildEqualIgnoreCaseCriteria( entityProperty, (String)propertyValue );
      }
      if( QueryRelation.Like.equals( queryRelation ) )
      {
        return buildLikeCriteria( entityProperty, (String)propertyValue );
      }
      if( QueryRelation.LikeIgnoreCase.equals( queryRelation ) )
      {
        return buildLikeIgnoreCaseCriteria( entityProperty, (String)propertyValue );
      }

      throw new RuntimeException( "invalid QueryRelation value." );
    
    }
    
  }
  
  protected IQueryCriteria buildCriteria( ClassProperty entityProperty, Operator operator, Object propertyValue )
  {
    return new QueryCriteria( entityProperty, operator, propertyValue );
  }

  /**
   * build the like query criteria for a property
   * @param entityProperty: the entity property
   * @param propertyValue: the criteria/property value
   * @return
   */
  public IQueryCriteria buildLikeCriteria( ClassProperty entityProperty, String propertyValue )
  {
    return new QueryCriteria( entityProperty, Operator.Like, propertyValue + "%" );
  }
  
  /**
   * build the like ignore case query criteria for a property
   * @param entityProperty: the entity property
   * @param propertyValue: the criteria/property value
   * @return
   */
  public IQueryCriteria buildLikeIgnoreCaseCriteria( ClassProperty entityProperty, String propertyValue )
  {
    return new QueryCriteria( entityProperty, Operator.Like, propertyValue.toUpperCase() + "%" )
                {
                  @Override
                  public String getFunctionForProperty( String beanAlias, String propertyName )
                  {
                    return String.format( "upper( %s.%s ) ", beanAlias, propertyName );
                  }
                };
  }
  
  
  /**
   * build the equal ignore case query criteria for a property
   * @param entityProperty: the entity property
   * @param propertyValue: the criteria/property value
   * @return
   */
  public IQueryCriteria buildEqualIgnoreCaseCriteria( ClassProperty entityProperty, String propertyValue )
  {
    return new QueryCriteria( entityProperty, Operator.Equal, propertyValue.toUpperCase() )
                {
                  @Override
                  public String getFunctionForProperty( String beanAlias, String propertyName )
                  {
                    return String.format( "upper( %s.%s ) ", beanAlias, propertyName );
                  }
                };
  }
  
  /**
   * this method give a chance to override the operator
   * @param entityProperty: the entity property
   * @param criteriaProperty: the criteria property
   * @param propertyValue: the value of the property
   * @param defaultOperator: the operator of the query, it must be only parameter operator
   * @param defaultOperator
   * @return
   */
  protected Operator getOperator( ClassProperty entityProperty, Object propertyValue, Operator defaultOperator )
  {
    return defaultOperator;
  }
  
  /**
   * should the system ignore this property when 
   * @param criteriaProperty
   * @param propertyValue
   * @return return true if this property/value should be ignored
   */
  protected boolean isIgnoreProperty( ClassProperty criteriaProperty, Object propertyValue )
  {
    if( propertyValue == null )
      return true;
    if( ( propertyValue instanceof String ) && StringUtil.isNullOrEmpty( (String)propertyValue ) )
    {
      return true;
    }
    
    return false;
  }
  
  /**
   * get the property form properties which compatible with compareProperty( same name, type compatible )
   * @param compareProperty: the comparing property 
   * @param properties:
   * @return: the property meet the criteria, return null if no property found
   */
  public ClassProperty getCompatibleProperty( ClassProperty compareProperty, Set< ClassProperty > properties )
  {
    if( properties == null || properties.isEmpty() )
      return null;
    final String propertyName = compareProperty.getName();
    final Type[] typeArguments = compareProperty.getTypeArguments();
    
    for( ClassProperty property : properties )
    {
      if( !propertyName.equals( property.getName() ) )
        continue;
      //check raw type
      if( !ConvertUtil.isConvertable( property.getPropertyRawType(), compareProperty.getPropertyRawType() ) )
        continue;
      
      Type[] theTypeArguments = property.getTypeArguments();
      if( ObjectUtil.equals( typeArguments, theTypeArguments ) )
      {
        return property;
      }
    }
    return null;
  }
  
  
  /**
   * the logic of handle operator and QueryRelation is almost same, so, create this wrapper class to simplify the code
   * @author bright
   *
   */
  protected static class RelationWrapper
  {
    private Operator operator;
    private QueryRelation queryRelation;
    
    public RelationWrapper( Operator operator )
    {
      this.operator = operator;
    }
    public RelationWrapper( QueryRelation queryRelation )
    {
      this.queryRelation = queryRelation;
    }
    
    public Operator getOperator()
    {
      return operator;
    }
    public QueryRelation getQueryRelation()
    {
      return queryRelation;
    }
    
    
  }
}
