package my.bc.studio.hibernate.employee.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "employee")
public class Employee
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String  firstName;
  private String  lastName;
  private Integer salary;
  
  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name="employee_role", 
              joinColumns={@JoinColumn(name="employee_id", referencedColumnName="id")}, 
              inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="id")})
  private Set<Role1> roles;

  @ManyToOne
  @JoinColumn(name="deparment_id", nullable=true, updatable=true)
  private Department department;
  
  public Employee()
  {
  }

  public Employee( String fname, String lname, int salary )
  {
    this.firstName = fname;
    this.lastName = lname;
    this.salary = salary;
  }

  public int getId()
  {
    return id;
  }

  public void setId( int id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String first_name )
  {
    this.firstName = first_name;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String last_name )
  {
    this.lastName = last_name;
  }

  public int getSalary()
  {
    return salary;
  }

  public void setSalary( int salary )
  {
    this.salary = salary;
  }

  public Set< Role1 > getRoles()
  {
    return roles;
  }
  public void setRoles( Set< Role1 > roles )
  {
    this.roles = roles;
  }
  
  
}