package my.bc.configure;

/**
 * This Enum defines key of properties.
 * try to use enum instead of string to avoid duplicate
 * 
 * @author Bright Chen
 *
 */
public enum PropertiesKey
{
  MODULE_NAME,
  PROPERTY_FILE_NAMES,
  HOME_DIR,
  CONF_DIR,
  PROPERTY_FILE_LIST;
  
  //keep the key as lower case only
  private String key;

  private PropertiesKey()
  {
    setKey( name().toLowerCase() );
  }
  private PropertiesKey( String key )
  {
    setKey( key.toLowerCase() );
  }
  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }
  
}
