package my.bc.web.listener;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import my.bc.configure.PropertiesManager;

/**
 * this class populates properties in order to fulfill the spring placeholder configuration
 * 
 * @author Bright Chen
 *
 */
public class PopulatePropertiesListener implements ServletContextListener
{
  @Resource( name="defaultPropertiesManager" )
  private PropertiesManager propertiesManager;
  
  @Override
  public void contextDestroyed(ServletContextEvent arg0)
  {
  }

  @Override
  public void contextInitialized(ServletContextEvent arg0)
  {
    propertiesManager.populateProperties();
  }


}
