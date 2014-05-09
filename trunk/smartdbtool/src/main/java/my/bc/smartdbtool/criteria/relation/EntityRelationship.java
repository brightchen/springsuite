package my.bc.smartdbtool.criteria.relation;

import java.util.ArrayList;
import java.util.List;

public class EntityRelationship
{
  // beanRoute is a route from the start bean to the end bean
  private List< EntityConnector > entityRoute = new ArrayList< EntityConnector >();
  
  public String toSqlRelationshipClause()
  {
    return null;
  }
  
  public List< EntityConnector > getEntityRoute()
  {
    return entityRoute;
  }
  
  public void addConnector( EntityConnector entityConnector )
  {
    entityRoute.add( entityConnector );
  }
  
}
