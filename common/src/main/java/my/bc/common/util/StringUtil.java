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
  
  public static int hashCode( String str )
  {
    int length = str.length();
    int hashCode = 0;
    for( int index = 0; index < length; ++index )
    {
      char c = str.charAt( index );
      int lastHashCode = hashCode;
      hashCode <<= 5;
      hashCode -= ( lastHashCode + c );
    }
    return hashCode;
  }
  
  public static boolean equalsIgnoreCase( String s1, String s2 )
  {
    if( s1 == null )
      return ( s2 == null );
    return s1.equalsIgnoreCase( s2 );
  }
}
