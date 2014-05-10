package my.bc.studio.smartdbtool.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanConfigurator
{
  public static final SpringBeanConfigurator DEFAULT = new SpringBeanConfigurator();
  
  private static final String CONFIG_PATH = "classpath*:my/bc/studio-*.xml";
  private ApplicationContext applicationContext;
  
  private SpringBeanConfigurator(){}
  
  public ApplicationContext getApplicationContext()
  {
    if( applicationContext == null )
    {
      synchronized( this )
      {
        if( applicationContext == null )
          applicationContext = new ClassPathXmlApplicationContext(CONFIG_PATH);
      }
    }
    
    return applicationContext; 
  }
  
  public void closeApplicationContext()
  {
    ((ConfigurableApplicationContext)applicationContext).close();
    applicationContext = null;
  }
}