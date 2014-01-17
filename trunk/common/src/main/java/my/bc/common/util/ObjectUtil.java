package my.bc.common.util;

import java.lang.reflect.Array;

public class ObjectUtil
{
  public static boolean equals( Object obj1, Object obj2 )
  {
    if( obj1 == obj2 )
      return true;
    if( obj1 == null || obj2 == null )   
      return false;   //one is null another is not null
    
    // in case it is an array
    boolean isObj1Array = isArray( obj1 );
    boolean isObj12rray = isArray( obj2 );
    if( isObj1Array != isObj12rray )
      return false;     //one is array and another one is not
    if( !isObj1Array )    //not array
      return obj1.equals( obj2 );
    return arrayEquals( obj1, obj2 );
  }
  
  
  public static boolean isArray( Object obj )
  {
    if( obj == null )
      return false;
    return obj.getClass().isArray();
//    return obj.getClass().getName().startsWith( "[" );
  }
  
  public static boolean arrayEquals( Object obj1, Object obj2 )
  {
    if( obj1 == obj2 )
      return true;
    if( obj1 == null || obj2 == null )   
      return false;   //one is null another is not null

    int arraySize = getArraySize( obj1 );
    if( arraySize != getArraySize( obj2 ) )
      return false;
    
    for( int index = 0; index < arraySize; ++index )
    {
      Object item1 = Array.get( obj1, index );
      Object item2 = Array.get( obj2, index );
      if( !equals( item1, item2 ) )
        return false;
    }
    return true;
  }
  
  /**
   * precondition: object is not null
   * @param object
   * @return: the size of the array
   */
  public static int getArraySize( Object object )
  {
    int index = 0;
    try
    {
      for( ;; )
      {
        Array.get( object, index++ );
      }
    }
    catch( ArrayIndexOutOfBoundsException iobe )
    {
      return index;
    }
  }
}
