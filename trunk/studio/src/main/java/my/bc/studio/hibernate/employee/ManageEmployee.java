package my.bc.studio.hibernate.employee;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import my.bc.studio.hibernate.employee.model.Employee;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.MetaAttribute;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

public class ManageEmployee
{
  private static SessionFactory factory;

  public static void main( String[] args )
  {
    Configuration configuration = null;
    try
    {
      // for hibernate 3.6 or later, just use new Configuration()
      configuration = new AnnotationConfiguration(); // new Configuration();
      configuration = configuration.configure();

      factory = configuration.buildSessionFactory();
    }
    catch ( Throwable ex )
    {
      System.err.println( "Failed to create sessionFactory object." + ex );
      throw new ExceptionInInitializerError( ex );
    }
    ManageEmployee me = new ManageEmployee();
    me.showClassMappings( configuration );
    
    me.showTableMappings( configuration );
    
    // /* Add few employee records in database */
    // Integer empID1 = me.addEmployee( "Zara", "Ali", 1000 );
    // Integer empID2 = me.addEmployee( "Daisy", "Das", 5000 );
    // Integer empID3 = me.addEmployee( "John", "Paul", 10000 );
    //
    // /* List down all the employees */
    // me.listEmployees();
    //
    // /* Update employee's records */
    // me.updateEmployee( empID1, 5000 );
    //
    // /* Delete an employee from the database */
    // me.deleteEmployee( empID2 );
    //
    // /* List down new list of the employees */
    // me.listEmployees();
  }

  public void showClassMappings( Configuration configuration )
  {
    for ( Iterator classIter = configuration.getClassMappings(); classIter.hasNext(); )
    {
      PersistentClass persistentClass = (PersistentClass) classIter.next();
      for ( Iterator propIter = persistentClass.getPropertyIterator(); propIter.hasNext(); )
      {
        Property property = (Property) propIter.next();
        String className = persistentClass.getClassName();
        String propertyName = property.getName();
        outputInfo( "className="+className + "; propertyName="+propertyName );
        
        if( property.getColumnIterator().hasNext() )
        {
          Column column = ( (Column) property.getColumnIterator().next() );
          outputInfo( "column=" + column.getName() );
        }
        
        Map< String, MetaAttribute > attributes = property.getMetaAttributes();
        if( attributes != null )
        {
          for( String key : attributes.keySet() )
          {
            MetaAttribute attribute = attributes.get( key );
            outputInfo( key + " = " + attribute.toString() );
          }
        }
        
        outputInfo( "=============================================" );
      }
    }
    
  }
  
  /**
   * the table employee_role was created implicitly, it will not show in configuration.getClassMappings()
   * role_id,  referenced to table trole
   * employee_id,  referenced to table employee
   * @param configuration
   */
  public void showTableMappings( Configuration configuration )
  {
    for ( Iterator tableIter = configuration.getTableMappings(); tableIter.hasNext(); )
    {
      Table table = (Table)tableIter.next();
      for( Iterator foreignKeyIter = table.getForeignKeyIterator(); foreignKeyIter.hasNext(); )
      {
        ForeignKey foreignKey = (ForeignKey)foreignKeyIter.next();
        
        List<Column> columns = foreignKey.getColumns();
        Table referencedTable = foreignKey.getReferencedTable();
        StringBuilder colNames = new StringBuilder();
        for( Column col : columns )
        {
          colNames.append( col.getName() ).append( ", " );
        }
        outputInfo( colNames + " referenced to table " + referencedTable.getName() );
      }
    }
  }
  
  public void outputInfo( String info )
  {
    System.out.println( info );
  }

  /* Method to CREATE an employee in the database */
  public Integer addEmployee( String fname, String lname, int salary )
  {
    Session session = factory.openSession();
    Transaction tx = null;
    Integer employeeID = null;
    try
    {
      tx = session.beginTransaction();
      Employee employee = new Employee( fname, lname, salary );
      employeeID = (Integer) session.save( employee );
      tx.commit();
    }
    catch ( HibernateException e )
    {
      if ( tx != null )
        tx.rollback();
      e.printStackTrace();
    }
    finally
    {
      session.close();
    }
    return employeeID;
  }

  /* Method to READ all the employees */
  public void listEmployees()
  {
    Session session = factory.openSession();
    Transaction tx = null;
    try
    {
      tx = session.beginTransaction();
      List employees = session.createQuery( "FROM Employee" ).list();
      for ( Iterator iterator = employees.iterator(); iterator.hasNext(); )
      {
        Employee employee = (Employee) iterator.next();
        System.out.print( "First Name: " + employee.getFirstName() );
        System.out.print( "  Last Name: " + employee.getLastName() );
        System.out.println( "  Salary: " + employee.getSalary() );
      }
      tx.commit();
    }
    catch ( HibernateException e )
    {
      if ( tx != null )
        tx.rollback();
      e.printStackTrace();
    }
    finally
    {
      session.close();
    }
  }

  /* Method to UPDATE salary for an employee */
  public void updateEmployee( Integer EmployeeID, int salary )
  {
    Session session = factory.openSession();
    Transaction tx = null;
    try
    {
      tx = session.beginTransaction();
      Employee employee = (Employee) session.get( Employee.class, EmployeeID );
      employee.setSalary( salary );
      session.update( employee );
      tx.commit();
    }
    catch ( HibernateException e )
    {
      if ( tx != null )
        tx.rollback();
      e.printStackTrace();
    }
    finally
    {
      session.close();
    }
  }

  /* Method to DELETE an employee from the records */
  public void deleteEmployee( Integer EmployeeID )
  {
    Session session = factory.openSession();
    Transaction tx = null;
    try
    {
      tx = session.beginTransaction();
      Employee employee = (Employee) session.get( Employee.class, EmployeeID );
      session.delete( employee );
      tx.commit();
    }
    catch ( HibernateException e )
    {
      if ( tx != null )
        tx.rollback();
      e.printStackTrace();
    }
    finally
    {
      session.close();
    }
  }
}