package my.bc.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/*
 * the methods of Collection which need comparing are compared by instance instead of value
 * this util implements some methods by compare by value 
 */
@SuppressWarnings( "rawtypes" )
public class CollectionUtil
{
  
  public static <T> boolean retainAllByValue( Collection<T> c1, Collection<? extends T> c2 )
  {
    if( c1 == null || c2 == null || c1.isEmpty() || c2.isEmpty() )
      return false;
    List< T > itemsToRemove = new ArrayList< T >();
    for( T item : c1 )
    {
      if( !containsByValue( c2, item ) )
        itemsToRemove.add( item );
    }
    c1.removeAll( itemsToRemove );
    return ( !itemsToRemove.isEmpty() );
  }
  
  public static <T> boolean addAllByValue( Collection<T> c1, Collection<? extends T> c2 )
  {
    if( c1 == null || c2 == null || c1.isEmpty() || c2.isEmpty() )
      return false;
    if( !( c1 instanceof Set ) )
      return c1.addAll( c2 );

    List< T > itemsToAdd = new ArrayList< T >();
    for( T item : c2 )
    {
      if( !containsByValue( c1, item ) )
        itemsToAdd.add( item );
    }
    return c1.addAll( itemsToAdd );

  }
  
  public static <T> boolean containsByValue( Collection< ? extends T > c, T item )
  {
    if( c == null || c.isEmpty() )
      return false;
    for( T i : c )
    {
      if( i.equals( item ) )
        return true;
    }
    return false;
  }
  
  /*
   * compare by reference;
   */
  public static boolean hasIntersection( Collection c1, Collection c2 )
  {
    return hasIntersection( c1, c2, false );
  }
  
  public static boolean hasIntersection( Collection c1, Collection c2, boolean compareByValue )
  {
    if( c1 == null || c2 == null || c1.isEmpty() || c2.isEmpty() )
      return false;
    for( Object o1 : c1 )
    {
      for( Object o2 : c2 )
      {
        boolean isEqual = compareByValue ? ObjectUtil.equals( o1, o2 ) : o1==o2;
        if( isEqual )
          return true;
      }
    }
    return false;
  }
  
  public static < I, T extends Collection<I> > T shallowCloneTo( Collection<I> srcCollection, T destCollection )
  {
    if( srcCollection == null || srcCollection.isEmpty() )
      return destCollection;
    destCollection.addAll( srcCollection );
    return destCollection;
  }

  /**
   * copy items from collection to array. 
   * this method will take advantage the parameter <array> instead of create a new array.
   * If the size of array less than size of collection, only copy first items
   * 
   * @param array
   * @param collection
   * @return
   */
  public static <T> T[] copyToArray( T[] array, Collection< ? extends T > collection )
  {
    if( collection == null )
      return null;
    if( collection.isEmpty() )
      return array;
    int copiedSize = 0;
    for( T item : collection )
    {
      if( copiedSize >= array.length )
        break;
      array[ copiedSize ] = item;
      ++copiedSize;
    }
    return array;
  }
}
