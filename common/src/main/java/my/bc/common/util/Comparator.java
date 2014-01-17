package my.bc.common.util;

public class Comparator
{
  //returns 
  // 0: if a1 equals a2
  // 1: a1 > a2
  // -1: a1 < a2;
  public static < T extends Comparable< S >, S >  int CompareArray( T[] a1, S[] a2 )
  {
    if( a1 == null && a2 == null )
      return 0;
    if( a1 == null )
      return (-1);
    if( a2 == null )
      return 1;
    
    int length = Math.min( a1.length, a2.length );
    int result = CompareArray( a1, a2, 0, length );
    if( result != 0 || a1.length == a2.length )
      return result;
    
    //they are equal, the result depends on which one is longer
    return a1.length > a2.length ? 1 : (-1);
  }
  
  public static < T extends Comparable< S >, S >  int CompareArray( T[] a1, S[] a2, int beginIndex, int length )
  {
    if( a1 == null && a2 == null )
      return 0;
    if( a1 == null )
      return (-1);
    if( a2 == null )
      return 1;
    
    for( int i = beginIndex; i < beginIndex + length; ++i )
    {
      int result = compare( a1[ i ], a2[i] );
      if( result != 0 )
        return result;
    }
    
    return 0;
  }

  public static < T extends Comparable< S >, S > int compare( T o1, S o2 )
  {
    if( o1 == null && o2 == null )
      return 0;
    if( o1 == null )
      return (-1);
    if( o2 == null )
      return 1;
    
    if( o1 instanceof String && o2 instanceof String )
    {
      if( ( (String)o1 ).equalsIgnoreCase( (String)o2 ) )
        return 0;
    }
    
    return o1.compareTo( o2 );
  }
}
