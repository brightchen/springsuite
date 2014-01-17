package my.bc.configure;

public class PropertiesUtil
{
  public static String getSystemProperty( PropertiesKey key )
  {
    return System.getProperty( key.getKey() );
  }
}
