package my.bc.configure;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * this class services as a chain of configuration properties
 * 
 * @author Bright Chen
 *
 */


@Component
public class ChainedConfigurationProperties implements ConfigurationProperties
{
  private ChainedConfigurationProperties(){}
  
  /**
   * how to bind configurations with ConfigurableConfigurationProperties and DefaultConfigurationProperties via annotation
   * I can only name these two component with same name
   */
  @Autowired
  @Qualifier( "configurationProperties" )
  private ConfigurationProperties configurations[]; 
//  new ConfigurationProperties[]{ @Resource configurableConfigurationProperties, new ConfigurableConfigurationProperties(), 
//                                                                                    new DefaultConfigurationProperties() };
  @Override
  public File[] getPropertyFiles()
  {
    for( ConfigurationProperties configuration : configurations )
    {
      File[] propertyFiles = configuration.getPropertyFiles();
      if( propertyFiles != null && propertyFiles.length > 0 )
        return propertyFiles;
    }
    return null;
  }

  @Override
  public File getHomeDir()
  {
    for( ConfigurationProperties configuration : configurations )
    {
      File homeDir = configuration.getHomeDir();
      if( homeDir != null  )
        return homeDir;
    }
    return null;
  }

  @Override
  public File getConfDir()
  {
    for( ConfigurationProperties configuration : configurations )
    {
      File confDir = configuration.getConfDir();
      if( confDir != null  )
        return confDir;
    }
    return null;
  }

}
