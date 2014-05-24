package my.bc.smartdbtool.criteria.relation.connection;

import java.util.Map;
import java.util.Set;

/**
 * the connectors in fact keeps all the entities.
 * these entities may form one or several network.
 * 
 * @author brightchen
 *
 */
@SuppressWarnings( "rawtypes" )
public interface IEntityConnectorsResolver
{
  /**
   * get connectors( entity, property ) which directly connected to the entity
   * @param entity
   * @return the list of connectors which directly connected to the entity 
   */
  public Set< EntityConnector > getDirectConnectors( Class entity );

  /**
   * @param entities: the entities which need to get the connectors
   * @return: a map of entity to its directly connectors
   */
  public Map< Class, Set< EntityConnector > > getDirectConnectors( Set< Class > entities );
  
  /**
   * is the relationship mutual, namely, A connect to B then B must connect to A 
   * @return return true if relation is mutual
   */
  public boolean isRelationMutual();
  
  /**
   * get all entities of this resolver
   * @return
   */
  public Set< Class > getAllEntities();
}
