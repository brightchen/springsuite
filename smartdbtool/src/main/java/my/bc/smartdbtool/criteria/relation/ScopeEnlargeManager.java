package my.bc.smartdbtool.criteria.relation;

import java.util.HashSet;
import java.util.Set;

import my.bc.common.util.CollectionUtil;
import my.bc.smartdbtool.criteria.relation.connection.EntityConnector;
import my.bc.smartdbtool.criteria.relation.connection.IEntityConnectorsResolver;

/**
 * this class manages the scope enlarge for my.bc.query.relation.EntityNetwork.resolveNetworkByEnlargeScope(EntityNetwork, Set<Class>, IEntityConnectorsResolver)
 * @author bchen
 *
 */
@SuppressWarnings( "rawtypes" )
public class ScopeEnlargeManager
{
  private static ScopeEnlargeManager defaultInstance;
  
  public static ScopeEnlargeManager defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( ScopeEnlargeManager.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new ScopeEnlargeManager();
        }
      }
    }
    return defaultInstance;
  }
  
  private ScopeEnlargeManager(){}
  
  /**
   * enlarge the scope ( resolvedNetwork or resolvingEntities )
   * This method should be called by buildEntityNetwork.
   * assume: there should no any entity in resolvingEntities can directly connect to resolvedNetwork
   * 
   * @param resolvedNetwork the network which entities going to be added to
   * @param resolvingEntities the entities are being resolved
   * @param connectorsResolver can get the connectors 
   * @return the entity which just added to the network to enlarge the scope
   */
  public Class enlargeScope( EntityNetwork resolvedNetwork, Set< Class > resolvingEntities, IEntityConnectorsResolver connectorsResolver )
  {
    //all entities of the connectors
    Set< Class > entities = connectorsResolver.getAllEntities();
    
    //remove all the entities which already added into the network
    if( resolvedNetwork != null )
      entities.removeAll( resolvedNetwork.getEntities() );
    
    if( resolvingEntities != null && !resolvingEntities.isEmpty() )
      entities.removeAll( resolvingEntities );
    
    if( entities.isEmpty() )
    {
      // can't enlarge the scope
      return null;  
    }

    //first, pick one which can directly add to the resolvedNetwork
    if( resolvedNetwork != null )
    {
      for( Class entity : entities )
      {
        if( resolvedNetwork.addDirectlyConnectedEntity( entity, connectorsResolver ) )
          return entity;
      }
    }
      
    //second, pick one which can directly add to any of the entities of resolvingEntities
    //this way, we in fact enlarge the resolvingEntities instead of network
    if( resolvingEntities != null )
    {
      for( Class entity : entities )
      {
        Set< EntityConnector > connectors = connectorsResolver.getDirectConnectors( entity );
        if( connectors == null || connectors.isEmpty() )
          continue;
        
        Set< Class > intersectionEntities = CollectionUtil.shallowCloneTo( resolvingEntities, new HashSet<Class>() );
        intersectionEntities.retainAll( EntityRelationUtil.getAllEntities( connectors ) );
        if( !intersectionEntities.isEmpty() )
          return entity;
      }
    }
    
    return null;
  }
}
