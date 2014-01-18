package my.bc.web.listener;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import my.bc.configure.DefaultPropertiesManager;

/**
 * this class populates properties in order to fulfill the spring placeholder
 * configuration So, this listener should be called before
 * ContextLoaderListener, which means this listener should NOT take advantage of
 * spring bean DI.
 * 
 * @author Bright Chen
 * 
 */
public class PopulatePropertiesListener implements ServletContextListener
{
  @Override
  public void contextDestroyed(ServletContextEvent contextEvent)
  {
  }

  /**
   * in fact, we only need to populate the properties which stored in the files out of the application.
   * the Placeholder can be configured use internal property files as default 
   */
  @Override
  public void contextInitialized(ServletContextEvent contextEvent)
  {
//    try
//    {
//      ServletContext context = contextEvent.getServletContext();
//      URL url = context.getResource("my/bc/persistent/persistent.properties");
//      if( url != null )
//        System.out.println( url.toString() );
//
//      String realPath = context.getRealPath("/");
//      realPath += "WEB-INF\\lib\\my\\bc\\persistent\\persistent.properties";
//      File file = new File(realPath);
//      System.out.println(file.exists());
//      DefaultPropertiesManager.instance.populateProperties();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
  }

}
