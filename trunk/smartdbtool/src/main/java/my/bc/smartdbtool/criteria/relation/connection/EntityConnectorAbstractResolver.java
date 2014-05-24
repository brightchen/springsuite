package my.bc.smartdbtool.criteria.relation.connection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings( "rawtypes" )
public abstract class EntityConnectorAbstractResolver implements IEntityConnectorsResolver
{
  /**
   * 
   * @param entities: the entities which need to get the connectors
   * @return: a map of entity to its directly connectors
   */
  @Override
  public Map< Class, Set< EntityConnector > > getDirectConnectors( Set< Class > entities )
  {
    if( entities == null || entities.isEmpty() )
      return Collections.emptyMap();
    
    Map< Class, Set< EntityConnector > > entitiesConnectors = new HashMap< Class, Set< EntityConnector > >();
    for( Class entity : entities )
    {
      entitiesConnectors.put( entity, getDirectConnectors( entity ) );
    }
    return entitiesConnectors;
  }

}
