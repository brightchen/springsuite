package my.bc.model.data;

/**
 * GenericData is a PropertiesCopyData with property id and name
 * @author Bright Chen
 *
 */
public class GenericData<T> extends PropertiesCopyData<T>
{
  private static final long serialVersionUID = -1553362202276521038L;

  private Long id;
  private String name;
  
  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
}
