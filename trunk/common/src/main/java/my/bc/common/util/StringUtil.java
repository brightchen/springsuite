package my.bc.common.util;

public class StringUtil
{
  public static String nullToEmpty( String str )
  {
    return ( str == null ) ? "" : str;
  }
  
  public static boolean isNullOrEmpty( String str )
  {
    return ( str == null || str.length() == 0 );
  }
  
  public static String toString( Object obj )
  {
    if( obj == null )
      return "null";
    if( obj instanceof Number )
    {
      return String.valueOf( (Number)obj );
    }
    if( obj instanceof String )
    {
      return (String)obj;
    }
    return obj.toString();
  }
}
