package my.bc.smartdbtool.criteria.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import my.bc.common.util.ObjectUtil;
import my.bc.common.util.StringUtil;


/**
 * This class manages EntityNetworks, such creation/search etc
 * @author bchen
 *
 */
@SuppressWarnings( "rawtypes" )
public class EntityNetworkManager
{
  private static EntityNetworkManager defaultInstance;
  private Map< Class, WholeEntityNetwork > wholeEntityNetworkMap = new HashMap< Class, WholeEntityNetwork >();
  private Set< EntityNetwork > entityNetworks = new HashSet< EntityNetwork >();
  
  public static EntityNetworkManager defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( EntityNetworkManager.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new EntityNetworkManager();
        }
      }
    }
    return defaultInstance;
  }

  private EntityNetworkManager(){}

  
  /**
   * get the WholeEntityNetwork instance which cover the entity 
   * @param entity
   * @return the WholeEntityNetwork instance which cover the entity, return null if the WholeEntityNetwork instance can't be found/built
   */
  public WholeEntityNetwork getWholeEntityNetwork( Class entity )
  {
    WholeEntityNetwork wholeEntityNetwork = wholeEntityNetworkMap.get( entity );
    if( wholeEntityNetwork != null )
      return wholeEntityNetwork;
    
    //try to build it
    wholeEntityNetwork = buildWholeEntityNetwork( entity );
    if( wholeEntityNetwork != null )
    {
      //cache it
      this.addEntityNetwork( wholeEntityNetwork );
      return wholeEntityNetwork;
    }
    
    return null;
  }

  /**
   * the WholeEntityNetworks should not have any intersection.
   * So we can lookup the WholeEntityNetwork by any entity which supposes inside of it. 
   * @param Entity: the entity which inside the WholeEntityNetwork which we are lookup
   * @return: the WholeEntityNetwork which includes the entity, returns null if no WholeEntityNetwork found
   */
  public WholeEntityNetwork lookupWholeEntityNetwork( Class entity )
  {
    return wholeEntityNetworkMap.get( entity );
  }
  
  /**
   * lookup the networks which covers all the entities
   * @param entities: the entities which the networks should cover
   * @return: the networks which meet the criteria
   */
  public EntityNetwork[] lookupEntityNetworks( Set< Class > entities )
  {
    if( entityNetworks == null || entityNetworks.isEmpty() )
      return null;
    
    //use list instead of set as entityNetworks already make sure there is no duplicate, list can increase the performance
    List< EntityNetwork > networks = new ArrayList< EntityNetwork >();
    for( EntityNetwork network : entityNetworks )
    {
      if( network.getEntities().containsAll( entities ) )
        networks.add( network );
    }
    return networks.toArray( new EntityNetwork[ networks.size() ] );
  }
  
  public EntityNetwork getEntityNetworkByName( String name )
  {
    if( StringUtil.isNullOrEmpty( name ) )
      return null;
    for( EntityNetwork entityNetwork : entityNetworks )
    {
      if( name.equals( entityNetwork.getName() ) )
        return entityNetwork;
    }
    return null;
  }

  /**
   * add an EntityNetwork into the EntityNetworkManager
   * @param entityNetwork
   */
  public void addEntityNetwork( EntityNetwork entityNetwork )
  {
    entityNetworks.add( entityNetwork );
  }
  
  /**
   * add a WholeEntityNetwork into the EntityNetworkManager
   * the wholeEntityNetwork maybe already added
   * we should check if the adding WholeEntityNetwork has intersection with the others
   * @param wholeEntityNetwork: the entity network which going to be added into the manager
   */
  public void addWholeEntityNetwork( WholeEntityNetwork wholeEntityNetwork )
  {    
    Set< Class > networkEntities = wholeEntityNetwork.getEntities();
    Set< Class > wholeNetworkEntities = wholeEntityNetworkMap.keySet();
    if( wholeNetworkEntities.retainAll( networkEntities ) )
    {
      // the wholeNetworkEntities changed, so they have intersection
      if( ObjectUtil.equals( wholeEntityNetwork, networkEntities ) )
      {
        // the adding network already added to the manager
        return;
      }
      // the adding network has intersection with the manager
      throw new EntityNetworkResolvingException( "the adding network has intersection with the manager." );
    }
    for( Class entity : networkEntities )
    {
      wholeEntityNetworkMap.put( entity, wholeEntityNetwork );
    }
    addEntityNetwork( wholeEntityNetwork );
  }
  
  /**
   * build a network which covers all the entities.
   * this method get connectors of entities from the EntityConnectorsResolver instead of another EntityNetwork.
   * the built network will be added into this manager
   * @param entities
   * @return: the built network 
   */
  protected WholeEntityNetwork buildWholeEntityNetwork( Class entity )
  {
    WholeEntityNetwork buildingNetwork = new WholeEntityNetwork();
    if( buildingNetwork.buildWholeEntityNetwork( entity ) )
      return buildingNetwork;
    else 
      return null;
  }
  
  /**
   * resolve the network of the alias entity map
   * resolveNetwork will find a network which includes all the entities which are being resolved, 
   * and use this network's relationship to build a refined entity network. 
   * @param aliasEntityMap
   * @return the EntityNetwork which keep the relationship of aliasEntityMap
   */
  public RefinedEntityNetwork resolveNetwork( Map< String, Class > aliasEntityMap )
  {
    //we haven't supported user specified alias yet
    //just delegate to resolveNetwork( Set< Class > entities )
    
    //get all entity set and delegate to resolveNetwork( Set< Class > entities )
    Set< Class > entities = new HashSet< Class >();
    for( Map.Entry< String, Class > entry : aliasEntityMap.entrySet()  )
    {
      entities.add( entry.getValue() );
    }
    return resolveNetwork( entities );
  }

  /**
   * 
   * @param entities
   * @return EntityNetwork which keep the relationship of aliasEntityMap
   */
  public RefinedEntityNetwork resolveNetwork( Set< Class > entities )
  {
    //search the cache
    EntityNetwork[] networks = lookupEntityNetworks( entities );
    
    EntityNetwork containerNetwork = null;
    if( networks == null || networks.length == 0 )
    {
      // there is no network which covers all the entities
      // try to build the network which covers all the entities
      containerNetwork = buildWholeEntityNetwork( entities.iterator().next() );
    }
    else
    {
      //find the proper network, prefer RefinedEntityNetwork
      for( EntityNetwork network : networks )
      {
        if( network instanceof RefinedEntityNetwork )
          return (RefinedEntityNetwork)network;   //found the network which meets the criteria
      }
      
      //can't find the RefinedEntityNetwork, use the first network
      containerNetwork = networks[0];
    }
    
    if( containerNetwork == null )
      return null;
    
    return containerNetwork.resolveNetwork( entities );
    
  }

}
