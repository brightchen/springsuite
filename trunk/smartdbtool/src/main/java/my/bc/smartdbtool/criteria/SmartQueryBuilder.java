package my.bc.smartdbtool.criteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import my.bc.common.classproperty.ClassProperty;
import my.bc.common.util.EntityUtil;
import my.bc.common.util.StringUtil;
import my.bc.smartdbtool.criteria.relation.EntityConnector;
import my.bc.smartdbtool.criteria.relation.EntityNetwork;
import my.bc.smartdbtool.criteria.relation.EntityNetworkManager;

/*
 * this class provides service to build query according to the QueryCriteria
 */
@SuppressWarnings( "rawtypes" )
public class SmartQueryBuilder
{
  private static SmartQueryBuilder defaultInstance;
  
  public static SmartQueryBuilder defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( SmartQueryBuilder.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new SmartQueryBuilder();
        }
      }
    }
    return defaultInstance;
  }
  
  private SmartQueryBuilder(){}
  
  /*
   * ObjectiveClass: the class of the entity which we want to retrieve
   * criteria: the criteria for the query
   */
  public <E> String buildSearchHsql( Class< ? > objectiveClass, IQueryCriteria criteria )
  {
    final String objectiveAlias = getAlias( objectiveClass );
    Map< String, Class > aliasMap = buildAliasMap( criteria );
    
    String whereClause = buildRelationClause( getAllEntityAliasMap( aliasMap, objectiveAlias, objectiveClass ) ) + buildCriteriaClause( criteria );
    return "select " + objectiveAlias + " from " + buildAliasList( aliasMap )
        + (  StringUtil.isNullOrEmpty( whereClause ) ? "" : ( " where " + whereClause ) ); 
  }
  
  
  public String getAlias( Class entity )
  {
    String simpleName = entity.getSimpleName();
    return simpleName.substring( 0, 1 ).toLowerCase() + simpleName.substring( 1 );
  }

  
  /*
   * build the alias list of search query
   * for example: User user, Account account, Role role
   */
  public String buildAliasList( IQueryCriteria criteria )
  {
    return buildAliasList( buildAliasMap( criteria ) );
  }
  
  protected Map< String, Class > buildAliasMap( IQueryCriteria criteria )
  {
    Map< String, Class > aliasMap = new HashMap< String, Class >();
    criteria.addAliases( aliasMap );
    return aliasMap;
  }
  
  protected String buildAliasList( Map< String, Class > aliasMap )
  {
    StringBuilder aliasesList = new StringBuilder();
    for( Map.Entry< String, Class > entry : aliasMap.entrySet() )
    {
      aliasesList.append( String.format( "%s %s, ", entry.getValue().getName(), entry.getKey() ) );
    }
    
    //remove the last ", " 
    return aliasesList.substring( 0, aliasesList.length() - 2 );
  }

  protected Map< String, Class > getAllEntityAliasMap( Map< String, Class > aliasMap, String objectiveAlias, Class objectiveBean )
  {
    aliasMap.put( objectiveAlias, objectiveBean );
    return aliasMap;
  }
  
  public String buildRelationClause( Set< Class > entities )
  {
    Map< String, Class > aliasEntityMap = new HashMap< String, Class >();
    for( Class entity : entities )
    {
      aliasEntityMap.put( EntityUtil.getPropertyStyleName( entity ), entity );
    }
    return buildRelationClause( aliasEntityMap );
  }
  
  /**
   * build the relationship of the entity classes
   * @param aliasEntityMap the map ( alias ==> entity class ) of entities of the query result
   * @return the relationship clause string, for example "role.account = account.id and account.user = user.id"
   */
  public String buildRelationClause( Map< String, Class > aliasEntityMap )
  {
    // relation clause is empty if number of entities less than 2
    if( aliasEntityMap == null || aliasEntityMap.size() == 0 || aliasEntityMap.size() == 1 )
      return "";
    
    //resolve the network, and then transform the network into relationship clause.
    EntityNetwork network = EntityNetworkManager.defaultInstance().resolveNetwork( aliasEntityMap );
    
    Set< EntityConnector > connectors = network.getAllConnectors();
    if( connectors == null || connectors.isEmpty() )
      return "";
    StringBuilder relationClause = new StringBuilder();
    for( EntityConnector connector : connectors )
    {
      relationClause.append( buildRelationClause( connector ) ).append( " and " );
    }
    int length = relationClause.length();
    if( length >= 4 )
      relationClause.delete( length - 4, length );
    return relationClause.toString();
  }
  
  /**
   * build the relation clause for the connector
   * @param connector: the connector which the relation clause corresponding to
   * @return
   */
  public String buildRelationClause( EntityConnector connector )
  {
    if( connector == null )
      return "";
    
    StringBuilder relationClause = new StringBuilder();
    
    {
      ClassProperty property1 = connector.getEntityProperty1();
      relationClause.append(  getAlias( property1.getDeclaringClass() ) + "." + property1.getName() );
    }
    
    relationClause.append( " = " );
    
    {
      ClassProperty property2 = connector.getEntityProperty2();
      relationClause.append(  getAlias( property2.getDeclaringClass() ) + "." + property2.getName() );
    }
    return relationClause.toString();
  }
  
  /**
   * build the sql criteria clause according to IQueryCriteria
   * @param criteria
   * @return
   */
  public String buildCriteriaClause( IQueryCriteria criteria )
  {
    return criteria.getHsql();
  }
}
