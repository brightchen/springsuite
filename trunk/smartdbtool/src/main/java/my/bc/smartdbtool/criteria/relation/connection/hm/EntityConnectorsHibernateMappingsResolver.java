package my.bc.smartdbtool.criteria.relation.connection.hm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.MetaAttribute;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import my.bc.smartdbtool.criteria.relation.connection.EntityConnector;
import my.bc.smartdbtool.criteria.relation.connection.EntityConnectorAbstractResolver;
import my.bc.smartdbtool.criteria.relation.connection.UnsupportedException;
import my.bc.smartdbtool.criteria.relation.connection.pa.EntityConnectorsAnnotationResolver;

/**
 * this class try to resolve the relationship among entities via hibernate internal mappings
 * Hibernate should already built the relationship among entities if the application use hibernate to manage the entities.
 * 
 * The ManyToMany relationship was implemented by adding a implicit relationship table, 
 * this relationship don't have corresponding persistent class. 
 *  
 * @author brightchen
 *
 */
public class EntityConnectorsHibernateMappingsResolver extends EntityConnectorAbstractResolver
{
  @Autowired
  private ApplicationContext applicationContext;
  
  private Configuration configuration;
  protected Map< Class, Set< EntityConnector > > entityConnectorMap = new HashMap< Class, Set< EntityConnector > >();

  private EntityConnectorsHibernateMappingsResolver(){}

  private void init()
  {
    if( entityConnectorMap != null )
      return;
    
    LocalSessionFactoryBean localSessionFactoryBean = (LocalSessionFactoryBean) applicationContext.getBean( "&sessionFactory" );
    configuration = localSessionFactoryBean.getConfiguration();
    
    buildEntityConnectorMap();
  }
  
  protected void buildEntityConnectorMap()
  {
    for ( Iterator<Table> tableIter = configuration.getTableMappings(); tableIter.hasNext(); )
    {
      Table table = tableIter.next();
      for( Iterator<ForeignKey> foreignKeyIter = table.getForeignKeyIterator(); foreignKeyIter.hasNext(); )
      {
        ForeignKey foreignKey = foreignKeyIter.next();
        
        List<Column> fKColumns = foreignKey.getColumns();
        if( fKColumns.size() > 1 )
        {
          throw new UnsupportedException( "Have Not support composite foreign key yet." );
        }
        Column fKColumn = fKColumns.get( 0 );
        
        Table referencedTable = foreignKey.getReferencedTable();
        StringBuilder colNames = new StringBuilder();
        for( Column col : fKColumns )
        {
          colNames.append( col.getName() ).append( ", " );
        }
      }
    }
  }
  
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
