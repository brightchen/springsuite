package my.bc.smartdbtool.criteria.relation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import my.bc.common.classproperty.ClassProperty;

@SuppressWarnings( "rawtypes")
public class EntityRelationUtil
{
  /*
   * get the direct connected entities of entity 
   */
  public static Set< Class > getDirectConnectedEntities( Class entity, Set< EntityConnector > connectors )
  {
    if( connectors == null || connectors.isEmpty() )
      return Collections.emptySet();
    
    Set< Class > entities = new HashSet< Class >();
    for( EntityConnector connector : connectors )
    {
      ClassProperty otherProperty = connector.getPropertyOfAnotherEntity( entity );
      if( otherProperty == null )
        continue;
      entities.add( otherProperty.getDeclaringClass() );
    }
    return entities;
  }
  
  public static Set< Class > getAllEntities( Collection< EntityConnector > connectors )
  {
    if( connectors == null || connectors.isEmpty() )
      return Collections.emptySet();
    
    Set< Class > entities = new HashSet< Class >();
    for( EntityConnector connector : connectors )
    {
      entities.addAll( Arrays.asList( connector.getEntities() ) );
    }
    return entities;
  }
}
