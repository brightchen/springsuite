package my.bc.web.util;

import java.util.ArrayList;
import java.util.Collection;

import my.bc.model.data.RequestData;
import my.bc.model.data.ResponseData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientDataUtil
{
  private static final Log log = LogFactory.getLog( ClientDataUtil.class );
  
  public static <B, T extends ResponseData<B> > T toClientData( Class<T> dataClass, B bean )
  {
    try
    {
      T data = dataClass.newInstance();
      data.toData( bean );
      return data;
    }
    catch( Exception e )
    {
      log.warn( "exception", e );
      throw new RuntimeException( e );
    }
  }
  
  /**
   * create datas via a collection of bean
   * 
   * @param dataClass
   * @param datas
   * @param beanCollection
   * @return
   */
  public static <B, T extends ResponseData<B> > Collection<T> 
      toClientDatas( Class<T> dataClass, Collection<T> datas, Collection<? extends B> beanCollection )
  {
    if( beanCollection == null || beanCollection.isEmpty() )
      return datas;
    
    if( datas == null )
      datas = new ArrayList<T>();
    
    for( B bean : beanCollection )
    {
      datas.add( toClientData( dataClass, bean ) );
    }
    
    return datas;
  }

  public static <T> T toBean( RequestData<T> clientData, Class<? extends T> beanClass )
  {
    try
    {
      T bean = beanClass.newInstance();
     
      clientData.toBean( bean );
      return bean;
    }
    catch( Exception e )
    {
      log.warn( "exception", e );
      throw new RuntimeException( e );
    }
  }

}
