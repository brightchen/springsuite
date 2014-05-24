package my.bc.smartdbtool.criteria.relation.connection.hm;

import java.util.Set;

import my.bc.smartdbtool.criteria.relation.connection.EntityConnector;
import my.bc.smartdbtool.criteria.relation.connection.EntityConnectorAbstractResolver;

/**
 * this class try to resolve the relationship among entities via hibernate internal mappings
 * Hibernate should already built the relationship among entities if the application use hibernate to manage the entities.
 *  
 * @author brightchen
 *
 */
public class EntityConnectorsHibernateMappingsResolver extends EntityConnectorAbstractResolver
{

  @Override
  public Set< EntityConnector > getDirectConnectors( Class entity )
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isRelationMutual()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set< Class > getAllEntities()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
