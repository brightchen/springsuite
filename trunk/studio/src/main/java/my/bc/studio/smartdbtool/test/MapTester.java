package my.bc.studio.smartdbtool.test;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import my.bc.studio.model.User;

import org.hibernate.cfg.Configuration;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class MapTester
{
  @Autowired
  private ApplicationContext applicationContext;
  
  private Configuration configuration;

  private EntityManager entityManager;
  
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  
  @PersistenceContext//( unitName = "studio-resourceLocal" )
  public void setEntityManager(EntityManager entityManager)
  {
    this.entityManager = entityManager;
  }

  public static void main( String[] argvs )
  {
    ApplicationContext appContext = SpringBeanConfigurator.DEFAULT.getApplicationContext();
    MapTester tester = appContext.getBean( MapTester.class );

    tester.prepareEnvironment();
    tester.showTableMappings( tester.configuration );
    
    SpringBeanConfigurator.DEFAULT.closeApplicationContext();
  }

  public void prepareEnvironment()
  {
//  LocalSessionFactoryBean localSessionFactoryBean = (LocalSessionFactoryBean) tester.applicationContext.getBean( "&sessionFactory" );
//  LocalSessionFactoryBean sessionFactoryBean = tester.applicationContext.getBean( LocalSessionFactoryBean.class );
//  SessionFactoryImpl sessionFactory = applicationContext.getBean( SessionFactoryImpl.class );
//  LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = tester.applicationContext.getBean( LocalContainerEntityManagerFactoryBean.class );
  
  return;
  
//  configuration = sessionFactory.getSettings().get.getCo.getConfiguration();
  }
  

  /**
   * the table employee_role was created implicitly, it will not show in configuration.getClassMappings()
   * role_id,  referenced to table trole
   * employee_id,  referenced to table employee
   * @param configuration
   */
  @SuppressWarnings( "unchecked")
  public void showTableMappings( Configuration configuration )
  {
    for ( Iterator<Table> tableIter = configuration.getTableMappings(); tableIter.hasNext(); )
    {
      Table table = tableIter.next();
      showTableRelation( table );
    }
  }
  
  public void showTableRelation( Table table )
  {
    for( Iterator<ForeignKey> foreignKeyIter = table.getForeignKeyIterator(); foreignKeyIter.hasNext(); )
    {
      ForeignKey foreignKey = foreignKeyIter.next();
      
      List<Column> columns = foreignKey.getColumns();
      Table referencedTable = foreignKey.getReferencedTable();
      StringBuilder colNames = new StringBuilder();
      for( Column col : columns )
      {
        colNames.append( col.getName() ).append( ", " );
      }
      outputInfo( colNames + " of table " + table.getName() + " referenced to table " + referencedTable.getName() );
    }
  }
  
  public void showJoins( Configuration configuration )
  {
    for ( Iterator<PersistentClass> classIter = configuration.getClassMappings(); classIter.hasNext(); )
    {
      PersistentClass persistentClass = classIter.next();
      for( Iterator<Join> joinIter = persistentClass.getJoinIterator(); joinIter.hasNext(); )
      {
        Join join = joinIter.next();
        outputInfo( "=========new join===========" );
        for( Iterator<Property> propertyIter = join.getPropertyIterator(); propertyIter.hasNext(); )
        {
          outputProperty( propertyIter.next() );
        }
      }
    }
  }
  
  public void outputProperty( Property property )
  {
    PersistentClass persistentClass = property.getPersistentClass();
    String className = persistentClass.getClassName();
    String propertyName = property.getName();
    outputInfo( "className="+className + "; propertyName="+propertyName );
    
  }
  
  public void outputInfo( String info )
  {
    System.out.println( info );
  }

}
