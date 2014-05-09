package my.bc.smartdbtool.criteria.relation;

import java.util.HashSet;
import java.util.Set;

import my.bc.common.util.CollectionUtil;

/*
 * WholeEntityNetwork is an EntityNetwork which includes all connected entities at a certain time.
 * namely, the WholeEntityNetwork can't be and more entity into it.
 * A project may have multiple separated WholeEntityNetwork
 * 
 * this network will add all the connected entities into the network when adding an entity into it
 */
@SuppressWarnings( "rawtypes" )
public class WholeEntityNetwork extends EntityNetwork
{
  
  /**
   * build a network which covers all the entities.
   * the built network will be added into this manager
   * the buildEntityNetwork of WholeEntityNetwork is greedy
   * @param entities
   * @return: the built network 
   */
  @Override
  public boolean buildEntityNetwork( Set< Class > entities )
  {
    // the resolving process will change the resolvingEntities, we'd better keep the original one as it is
    Set< Class > entitiesCopy = CollectionUtil.shallowCloneTo( entities, new HashSet< Class >() );
    
    boolean networkChanged = false;
    do
    {
      networkChanged = false;
      
      //find the direct connected entities and add them into resolvedNetwork
      if( entitiesCopy != null && !entitiesCopy.isEmpty() )
      {
        int entitiesSize = entitiesCopy.size();
        resolveDirectConnectedEntities( this, entitiesCopy, getEntityConnectorsResolver() );
        networkChanged = ( entitiesSize > entitiesCopy.size() );
      }
      
      Class addedEntity = null;
      do
      {
        //pick any entity from this network which connected to resolvedNetwork and add to it
        addedEntity = ScopeEnlargeManager.defaultInstance().enlargeScope( this, null, getEntityConnectorsResolver() );
        networkChanged = ( networkChanged || addedEntity != null );
      }
      while( addedEntity != null );
    }
    while( networkChanged );
    
    //need a criteria to test if the network build success or not
    return ( entitiesCopy == null || entitiesCopy.isEmpty() );
  }
  
  public boolean buildWholeEntityNetwork( Class entity )
  {
    Set< Class > entities = new HashSet< Class >();
    entities.add( entity );
    return buildEntityNetwork( entities );
  }

  /**
   * the WholeEntityNetwork suppose expanded as large as possible
   * @return
   */
//  protected boolean completeWholeEntityNetwork()
//  {
//    // add all entities of connectors into the network
//    Set< EntityConnector > entityConnectors = getAllConnectors();
//    
//    return true;
//  }
  

  /**
   * add the entity which directly connected to this network into it.
   * this method will check if the entity directly connected to the network, 
   * do nothing and return false if entity doesn't directly connected to the network
   * we should make sure the entity network is valid after this method.
   * 
   * @param entity the entity going to add to this EntityNetwork 
   * @param containerNetwork the network which contains entity and its connectors, we can get this entity's connectors from containerNetwork
   * @return return true if add entity successful
   */
  @Override
  protected boolean addDirectlyConnectedEntity( Class entity, IEntityConnectorsResolver connectorsResolver )
  {
    //the entity simply added to the network when network was empty or the network already contain this entity.
    //we didn't get the connectors of this entity when connectorsResolver is EntityConnectorsAnnotationResolver
    Set< Class > networkEntities = network.keySet();
    if( networkEntities.contains( entity ) )
      return true;

    if( connectorsResolver == null )
      return false;
    
    //call connectorsResolver.getDirectConnectors even if network was empty to notify the connectorsResolver this entity
    Set< EntityConnector > entityConnectors = connectorsResolver.getDirectConnectors(  entity );
    if( networkEntities.isEmpty() )
    {
      return addEntityToNetwork( entity, entityConnectors, connectorsResolver );
    }

    boolean isEntityDirectlyConnectToNetwork = false;
    // check if the entity directly connect to network;
    for( EntityConnector connector : entityConnectors )
    {
      Class anotherEntity = connector.getPropertyOfAnotherEntity( entity ).getDeclaringClass();
      
      //if the connector's another entity already inside the network, this entity can be added to the network.
      if( networkEntities.contains( anotherEntity ) )
      {
        isEntityDirectlyConnectToNetwork = true;
        break;
      }
    }
    
    boolean isRelationMutual = connectorsResolver.isRelationMutual();
    if( isRelationMutual )
    {
      // the relationship is mutual, which mean no entity of network directly connected to this entity neither.
      if( !isEntityDirectlyConnectToNetwork )
        return false;
      return addEntityToNetwork( entity, entityConnectors, connectorsResolver );
    }


    // relationship is not mutual, also have to check if there are any entities inside network connected to this entity
    // the connectors of the entities inside of network can get from this network
    // as this network in fact is WholeEntityNetwork ( namely, all the connectors of the entities of the network have been added ) 
//    Set< EntityConnector > connectors = new HashSet< EntityConnector >();

    Set< EntityConnector > networkEntityConnectors = getAllConnectors();
    if( networkEntityConnectors != null && !networkEntityConnectors.isEmpty() )
    {
      for( EntityConnector networkEntityConnector : networkEntityConnectors )
      {
        Class[] connectorEntities = networkEntityConnector.getEntities();
        if( entity.equals( connectorEntities[0] ) || entity.equals( connectorEntities[1] ) )
        {
          //this is the correct connector
          entityConnectors.add( networkEntityConnector );
          isEntityDirectlyConnectToNetwork = true;
        }
      }
    }
    
    if( isEntityDirectlyConnectToNetwork )
    {
      return addEntityToNetwork( entity, entityConnectors, connectorsResolver );
    }
    
    return false;
  }
  
  
  /**
   * add entity and its connectors into the network
   * @precondition the caller should make sure that entityConnectors connect to the entity and this entity can add to the network
   * @param entity
   * @param entityConnectors
   * @return true if add entity successful
   */
  protected boolean addEntityToNetwork( Class entity, Set< EntityConnector > entityConnectors, IEntityConnectorsResolver connectorsResolver )
  {
    network.put( entity, entityConnectors );

    // make sure the mutual relation of entities of the network
    if( entityConnectors != null && !entityConnectors.isEmpty() )
    {
      for( EntityConnector connector : entityConnectors )
      {
        addEntityConnector( connector );
      }
    }
    
    // make sure the network integrity.    
    // get all entities which inside the connectors but haven't added into the network 
    Set< Class > entities = EntityRelationUtil.getAllEntities( entityConnectors );
    Set< Class > entitiesOfNetwork = getAllEntities();
    entities.removeAll( entitiesOfNetwork );
    
    return resolveNetwork( this, entities, connectorsResolver );
  }
  
  /**
   * add entity and its connector into the network.
   * do nothing and return false if the entity doesn't directly connected to the network. 
   *
   * @param entity the entity going to add to this network
   * @param connectedEntities the entities which the adding entity directly connected with
   * @return true if the entity add to this network successful.
   */
//  @Override
//  protected boolean addDirectlyConnectedEntity( Class entity, Map< Class, EntityConnector > connectedEntities )
//  {
//    if( connectedEntities == null )
//      return false;
//    
//    //the entities of this network
//    Set< Class > entities = network.keySet();
//    entities.retainAll( connectedEntities.keySet() );
//    if( entities.isEmpty() )    //this network don't have intersection with connectedEntities
//      return false;
//    
//    //we only add the connectors which connect to the entities of network instead of all the connectors of this entity
//    Set< EntityConnector > connectors = new HashSet< EntityConnector >();
//    for( Class connectedEntity : entities )
//    {
//      connectors.add( connectedEntities.get( connectedEntity ) );
//    }
//    network.put( entity, connectors );
//    return true;
//    
//  }

}
