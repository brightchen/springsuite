package my.bc.common.util;

public class ConvertUtil
{
  //we put different classes into this array, so, it can be type safe for compiler
  public static Class[] NumberConvertRoute = { (Class)Byte.class, (Class)Short.class, (Class)Integer.class, (Class)Long.class, (Class)Float.class, (Class)Double.class  }; 
  
  /**
   * check if the source class can convert to dest class
   * @param source: the class want to be converted
   * @param dest: the class convert to
   * @return True if can be converted
   */
  public static boolean isConvertable( Class sourceClass, Class destClass )
  {
    if( destClass.equals( sourceClass ) )
      return true;
    
    boolean isSourceNumber = Number.class.isAssignableFrom( sourceClass );
    boolean isDestNumber = Number.class.isAssignableFrom( destClass );
    if( isSourceNumber != isDestNumber )
      return false;
    if( !isSourceNumber )
      return destClass.isAssignableFrom( sourceClass );
    
    //handle number convertable
    return isNumberConvertable( sourceClass, destClass );
  }
  
  /**
   * @precondition: both sourceClass and destClass are Number
   * @param sourceClass
   * @param destClass
   * @return
   */
  @SuppressWarnings( "rawtypes") 
  public static boolean isNumberConvertable( Class sourceClass, Class destClass )
  {
    if( sourceClass.equals( destClass ) )
      return true;
    
    int sourceClassIndex = -1;
    int destClassIndex = -1;
    for( int index = 0; index < NumberConvertRoute.length && ( sourceClassIndex < 0 || destClassIndex < 0 ); ++index )
    {
      if( NumberConvertRoute[index].equals( sourceClass ) )
        sourceClassIndex = index;
      if( NumberConvertRoute[index].equals( destClass ) )
        destClassIndex = index;
    }
    
    return ( sourceClassIndex >= 0 && destClassIndex >= 0 && destClassIndex > sourceClassIndex );
  }

}
