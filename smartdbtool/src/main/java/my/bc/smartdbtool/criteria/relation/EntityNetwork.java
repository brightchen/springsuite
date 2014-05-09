package my.bc.smartdbtool.criteria.relation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import my.bc.common.classproperty.ClassProperty;
import my.bc.common.util.CollectionUtil;
import my.bc.common.util.ObjectUtil;

/*
 * EntityNetwork keeps the relationship among beans
 * it can be partial of WholeEntityNetwork
 */
@SuppressWarnings( "rawtypes" )
public class EntityNetwork extends EntityConnectorAbstractResolver
{
  private String name;
  
  protected Map< Class, Set< EntityConnector > > network = new HashMap< Class, Set< EntityConnector > >();
  
  //use the default entity connector resolver by default
  private IEntityConnectorsResolver entityConnectorsResolver = getDefaultEntityConnectorsResolver();

  /*
   * precondition: at least one entity of this connector is already inside the network
   */
  protected boolean addEntityConnector( EntityConnector connector )
  {
    Class[] entities = connector.getEntities();
    Set< Class > allEntities = getEntities();
    if( !allEntities.contains( entities[0] ) && !allEntities.contains( entities[1] ) )
    {
      //this connector should not be added into network as it will be orphan it added
      return false;
    }

    addEntityConnectorPartially( connector, connector.getEntityProperty1() );
    addEntityConnectorPartially( connector, connector.getEntityProperty2() );
    
    return true;
  }
  
  /*
   * precondition: at least one entity of this connector is already inside the network
   */
  private void addEntityConnectorPartially( EntityConnector connector, ClassProperty classProperty )
  {
    Class entity = classProperty.getDeclaringClass();
    Set< EntityConnector > connectors = network.get( entity );
    if( connectors == null )
    {
      //this entity is not in the network, add it into the network
      Set< EntityConnector > conns = new HashSet< EntityConnector >();
      conns.add( connector );
      network.put( entity, conns );
    }
    else
    {
      connectors.add( connector );
    }
  }
  
  /**
   * add the entity to this network if the network is empty or the network already contains this entity
   * @param entity
   * @return
   */
  protected boolean addEntityToEmptyOrContainerNetwork( Class entity )
  {
    Set< Class > entities = network.keySet();
    if( entities.contains( entity ) )
      return true;
    if( entities.isEmpty() )
    {
      // this network was empty, simply add this entity, no network yet
      Set< EntityConnector > connectors = new HashSet< EntityConnector >();    //should not use Collections.emptySet(), as it can add connectors
      network.put( entity, connectors );
      return true;
    }
    return false;
  }

  /**
   * add the entity which directly connected to this network into it.
   * this method will check if the entity directly connected to the network, 
   * do nothing and return false if entity doesn't directly connected to the network
   * 
   * @param entity the entity going to add to this EntityNetwork 
   * @param containerNetwork the network which contains entity and its connectors, we can get this entity's connectors from containerNetwork
   * @return return true if add entity successful
   */
  protected boolean addDirectlyConnectedEntity( Class entity, IEntityConnectorsResolver connectorsResolver )
  {
    if( addEntityToEmptyOrContainerNetwork( entity ) )
      return true;
    
    if( connectorsResolver == null )
      return false;
    
    Set< Class > networkEntities = network.keySet();
    Set< EntityConnector > entityConnectors = connectorsResolver.getDirectConnectors(  entity );
    //we only add the connectors which connect to the entities of network instead of all the connectors of this entity
    Set< EntityConnector > connectors = new HashSet< EntityConnector >();
    // get the connectors which connects to the entity and check if the another entity is already inside the network
    if( entityConnectors != null )
    {
      for( EntityConnector connector : entityConnectors )
      {
        Class anotherEntity = connector.getPropertyOfAnotherEntity( entity ).getDeclaringClass();
        
        //if the connector's another entity already inside the network, this entity can be added to the network.
        if( networkEntities.contains( anotherEntity ) )
          connectors.add( connector );
      }
    }

    if( !connectors.isEmpty() )
    {
      // found connectors of this entity directly connected to network
      return addEntityToNetwork( entity, connectors, connectorsResolver );
    }
    
    boolean isRelationMutual = connectorsResolver.isRelationMutual();
    if( isRelationMutual )
    {
      // can't the connectors of this entity directly connected to network
      // and the relationship is mutual, which mean no entity of network directly connected to this entity neither.
      return false;
    }
    
    // relationship is not mutual, also have to check if there are any entities inside network connected to this entity
    // the connectors of the entities inside of network should get from the connectorsResolver instead of this network
    // as this network in fact is Refined ( namely, all the connectors of the entities of the network just connect to these entities ) 
    for( Class networkEntity : networkEntities )
    {
      Set< EntityConnector > networkEntityConnectors = connectorsResolver.getDirectConnectors( networkEntity );
      if( networkEntityConnectors == null || networkEntityConnectors.isEmpty() )
        continue;
      
      for( EntityConnector networkEntityConnector : networkEntityConnectors )
      {
        Class[] connectorEntities = networkEntityConnector.getEntities();
        if( entity.equals( connectorEntities[0] ) || entity.equals( connectorEntities[1] ) )
        {
          //this is the correct connector
          connectors.add( networkEntityConnector );
        }
      }
    }
    
    if( !connectors.isEmpty() )
    {
      return addEntityToNetwork( entity, connectors, connectorsResolver );
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
    return true;
  }

  /**
   * add entity and its connector into the network.
   * do nothing and return false if the entity doesn't directly connected to the network. 
   *
   * @param entity the entity going to add to this network
   * @param connectedEntities the entities which the adding entity directly connected with
   * @return true if the entity add to this network successful.
   */
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
  
  /**
   * add the entity to this network if the network is empty or the network already contains this entity
   * @param entity
   * @return
   */
//  protected boolean addEntityToEmptyOrContainerNetwork( Class entity )
//  {
//    Set< Class > entities = network.keySet();
//    if( entities.contains( entity ) )
//      return true;
//    if( entities.isEmpty() )
//    {
//      // this network was empty, simply add this entity, no network yet
//      network.put( entity, null );
//      return true;
//    }
//    return false;
//  }

  
  /**
   * get all connectors of this network
   * @return
   */
  public Set< EntityConnector > getAllConnectors()
  {
    Set< EntityConnector > connectors = new HashSet< EntityConnector >();
    for( Map.Entry< Class, Set< EntityConnector > > entry : network.entrySet() )
    {
      connectors.addAll( entry.getValue() );
    }
    return connectors;
  }
  
  /**
   * get the connectors which directly connected to the entity from this EntityNetwork.
   * 
   * @param entity
   * @return the EntityConnector set which directly connect to entity 
   */
  @Override
  public Set< EntityConnector > getDirectConnectors( Class entity )
  {
    return network.get( entity );
  }

  @Override
  public boolean isRelationMutual()
  {
    return true;
  }
  
  /**
   * get all entities of this resolver
   * @return
   */
  public Set< Class > getAllEntities()
  {
    return network.keySet();
  }

  
  public static IEntityConnectorsResolver getDefaultEntityConnectorsResolver()
  {
    return EntityConnectorsAnnotationResolver.defaultInstance();
  }
  
  public IEntityConnectorsResolver getEntityConnectorsResolver()
  {
    return entityConnectorsResolver;
  }

  public void setEntityConnectorsResolver( IEntityConnectorsResolver entityConnectorsResolver )
  {
    this.entityConnectorsResolver = entityConnectorsResolver;
  }

  /**
   * build a network which covers all the entities.
   * the built network will be added into this manager
   * @param entities
   * @return: the built network 
   */
  public boolean buildEntityNetwork( Set< Class > entities )
  {
    return resolveNetwork( this, entities, entityConnectorsResolver );

  }

  /**
   * this interface is used when there are no same entity in the criteria
   * resolveNetwork will find a network which includes all the entities which are being resolved, 
   * and use this network's relationship to build a refined entity network. 
   *
   * @param entitiesToResolve
   * @return the EntityNetwork which keep the relationship of all the entities to be resolved; return null if the Network can't be resolved
   */
  public RefinedEntityNetwork resolveNetwork( Set< Class > entitiesToResolve )
  {
    if( entitiesToResolve == null || entitiesToResolve.size() < 2 )
      return null;    //no relationship if less than 2 entities
    
    //check if all the entitiesToResolve already in this network, if not, we have to merge these entities into network
    Set< Class > entities = network.keySet();
    if( !entities.containsAll( entitiesToResolve ) )
    {
      // this EntityNetwork is not complete enough to resolve the resolving entities, 
      // just return null here instead of merge it as we don't want to change this EntityNetwork silently.
      return null;
    }
    
    // resolve network in fact is building a RefinedEntityNetwork which covers the resolving entities 
    // ( get the connectors relationship from this EntityNetork )
    RefinedEntityNetwork resolvedNetwork = new RefinedEntityNetwork();
    return resolveNetwork( resolvedNetwork, entitiesToResolve, this ) ? resolvedNetwork : null;
      
  }
  

  /**
   * 
   * @param resolvedNetwork the EntityNetwork which partial of entities have been resolved
   * @param resolvingEntities the entities which haven't been resolved and going to be resolved
   * @return return true if resolve successful
   */
  protected boolean resolveNetwork( EntityNetwork resolvedNetwork, Set< Class > resolvingEntities, IEntityConnectorsResolver connectorsResolver  )
  {
    if( resolvingEntities.isEmpty() )
      return true;
    
    // the resolving process will change the resolvingEntities, we'd better keep the original one as it is
    Set< Class > resolvingEntitiesCopy = CollectionUtil.shallowCloneTo( resolvingEntities, new HashSet< Class >() );
    
    //find the direct connected entities and add them into resolvedNetwork
    if( resolveDirectConnectedEntities( resolvedNetwork, resolvingEntitiesCopy, connectorsResolver ) )
      return true;
    
    // introduce a bridge entity from this network to resolve the entities
    if( resolveBridgeConnectedEntities( resolvedNetwork, resolvingEntitiesCopy, connectorsResolver ) )
      return true;
    
    // add any of the remained entity which connected to any of entity of resolved and resolve
    if( resolveNetworkByEnlargeScope( resolvedNetwork, resolvingEntitiesCopy, connectorsResolver ) )
      return true;

    return false;
  }
  
  /*
   * this method resolve the entities by put the entities of resolvingEntities which directly connected to the resolvedNetwork
   * return whether the entitiesToResolve being completely resolved
   */
  protected boolean resolveDirectConnectedEntities( EntityNetwork resolvedNetwork, Set< Class > resolvingEntities, IEntityConnectorsResolver connectorsResolver )
  {
    if( resolvingEntities == null || resolvingEntities.isEmpty() )
      return true;
  
    int numOfResolvedEntity;
    do
    {
      Set< Class > thisResolvedEntities = new HashSet< Class >();  //the entities resolved this run
      for( Class resolvingEntity : resolvingEntities )
      {
        boolean added = resolvedNetwork.addDirectlyConnectedEntity( resolvingEntity, connectorsResolver );
        if( added )
        {
          thisResolvedEntities.add( resolvingEntity );
        }
      }
      numOfResolvedEntity = thisResolvedEntities.size();
      if( numOfResolvedEntity > 0 )
        resolvingEntities.removeAll( thisResolvedEntities );
    }while( numOfResolvedEntity > 0 && !resolvingEntities.isEmpty() );
    
    return resolvingEntities.isEmpty();
  }
  
  /*
   * in this step, all the entities of entitiesToResolve are not directly connected to resolvedNetwork
   * we should try to find a bridge entity from this EntityNetwork to connect both resolvedNetwork and entitiesToResolve
   */
  protected boolean resolveBridgeConnectedEntities( EntityNetwork resolvedNetwork, Set< Class > resolvingEntities, IEntityConnectorsResolver connectorsResolver )
  {
    if( resolvingEntities.isEmpty() )
      return true;
    
    if( connectorsResolver == null )
      return false;
    
    // get the entity from the network which doesn't belong to resolvedEntities/entitiesToResolve, 
    // and which connect to both resolvedEntities and entitiesToResolve
    Set< Class > otherEntities = new HashSet< Class >();  // the entities which not belongs to resolvedEntities/entitiesToResolve
    otherEntities = connectorsResolver.getAllEntities();
    otherEntities.removeAll( resolvedNetwork.getEntities() );
    otherEntities.removeAll( resolvingEntities );
    if( otherEntities.isEmpty() )
    {
      return false;
    }
    
    int numOfResolvedEntity;
    do
    {
      numOfResolvedEntity = 0;
      for( Class entity : otherEntities )
      {
        //check whether the entity connected to both resolvedNetwork and entitiesToResolve
        //namely, the entity's connected entities has intersection with both resolvedNetwork and entitiesToResolve
        Set< EntityConnector > connectedEntites = connectorsResolver.getDirectConnectors( entity );

        Set< Class > resolvedEntitiesCopy = new HashSet< Class >();
        resolvedEntitiesCopy.addAll( resolvedNetwork.getEntities() );
        resolvedEntitiesCopy.retainAll( EntityRelationUtil.getAllEntities( connectedEntites ) );
        if( resolvedEntitiesCopy.isEmpty() )
        {
          continue;   // this entity doesn't connect to any of entity of resolvedEntities
        }

        Set< Class > entitiesToResolveCopy = new HashSet< Class >();
        entitiesToResolveCopy.addAll( resolvingEntities );
        entitiesToResolveCopy.retainAll( EntityRelationUtil.getAllEntities( connectedEntites ) );
        if( entitiesToResolveCopy.isEmpty() )
        {
          continue;  // this entity doesn't connect to any of entity of entitiesToResolveCopy
        }
        
        //this entity is a bridge entity, add this entity and all the entities of entitiesToResolve which connected to this entity
        resolvedNetwork.addDirectlyConnectedEntity( entity, this );
        for( Class entityToResolve : entitiesToResolveCopy )
        {
          resolvedNetwork.addDirectlyConnectedEntity( entityToResolve, this );
        }

        resolvingEntities.removeAll( entitiesToResolveCopy );

        if( resolvingEntities.isEmpty() )
        {
          // all the entities of entitiesToResolve have been resolved.
          return true;
        }

        ++numOfResolvedEntity;
      }
    }
    while( numOfResolvedEntity > 0 );
    
    return false;
  }
  
  /*
   * resolve connected entities by adding more entities into resolvedNetwork/resolvingEntities.
   * this method should be called after directly resolving and bridge resolving
   * so, add only one entity to resolvedNetwork can not be resolved by directly resolving
   *   ( if can, it can be resolved by bridge resolving without adding entity to resolvedNetwork
   */
  protected boolean resolveNetworkByEnlargeScope( EntityNetwork resolvedNetwork, Set< Class > resolvingEntities, IEntityConnectorsResolver connectorsResolver )
  {
    //pick any entity from this network which connected to resolvedNetwork and add to it
    Class addedEntity = ScopeEnlargeManager.defaultInstance().enlargeScope( resolvedNetwork, resolvingEntities, connectorsResolver );
    if( addedEntity == null )
    {
      throw new RuntimeException( "the entities can't be resolved" );
    }
    
    if( resolveBridgeConnectedEntities( resolvedNetwork, resolvingEntities, connectorsResolver ) )
      return true;
    
    return resolveNetworkByEnlargeScope( resolvedNetwork, resolvingEntities, connectorsResolver );
  }
  
  /**
   * 
   * @param entity
   * @return the map( entity ==> entity connectors ) this entity directed to 
   */
  protected  Map< Class, EntityConnector > getConnectedEntities( Class entity )
  {
    Set< EntityConnector > connectors = network.get( entity );
    Map< Class, EntityConnector > connectedEntities = new HashMap< Class, EntityConnector >();
    for( EntityConnector connector : connectors )
    {
      connectedEntities.put( connector.getPropertyOfAnotherEntity( entity ).getDeclaringClass(), connector );
    }
    return connectedEntities;
  }
  
  public Set< Class > getEntities()
  {
    return network.keySet();
  }
  
  public String getName()
  {
    return name;
  }
  public void setName( String name )
  {
    this.name = name;
  }
  
  @Override
  public boolean equals( Object other )
  {
    if( this == other )
      return true;
    if( !( other instanceof EntityNetwork ) )
      return false;
    EntityNetwork otherNetwork = ( EntityNetwork )other;
    Set< Class > entities = network.keySet();
    Set< Class > otherEntities = otherNetwork.network.keySet();
    if( !ObjectUtil.equals( entities, otherEntities ) )
      return false;
    
    //the entities are same, as the entities class is class, should be same reference
    for( Class entity : entities )
    {
      Set< EntityConnector > connectors = network.get( entity );
      Set< EntityConnector > otherConnectors = otherNetwork.network.get( entity );
      if( !ObjectUtil.equals( connectors, otherConnectors ) )
        return false;
    }
    return true;
  }

}
