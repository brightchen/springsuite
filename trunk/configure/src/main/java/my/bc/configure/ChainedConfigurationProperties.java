package my.bc.configure;

import java.io.File;

/**
 * this class services as a chain of configuration properties
 * 
 * @author Bright Chen
 *
 */

public class ChainedConfigurationProperties extends ContextDepended implements ConfigurationProperties
{
  public static final ChainedConfigurationProperties instance = new ChainedConfigurationProperties();
  
  protected ChainedConfigurationProperties(){}
  
  /**
   * how to bind configurations with ConfigurableConfigurationProperties and DefaultConfigurationProperties via annotation
   * I can only name these two component with same name, but in fact it throws exception 
   * Annotation-specified bean name 'configurationProperties' for bean class [my.bc.configure.DefaultConfigurationProperties] conflicts with existing,
   */
  protected ConfigurationProperties configurations[] = new ConfigurationProperties[]{ new ConfigurableConfigurationProperties(), 
                                                                                      new DefaultConfigurationProperties() };
  
  @Override
  public File[] getPropertyFiles()
  {
    for( ConfigurationProperties configuration : getDelegatedToConfigurationProperties() )
    {
      File[] propertyFiles = configuration.getPropertyFiles();
      if( propertyFiles != null && propertyFiles.length > 0 )
        return propertyFiles;
    }
    return null;
  }
  
  protected ConfigurationProperties[] getDelegatedToConfigurationProperties()
  {
    return configurations;
  }

  @Override
  public File getHomeDir()
  {
    for( ConfigurationProperties configuration : getDelegatedToConfigurationProperties() )
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
    for( ConfigurationProperties configuration : getDelegatedToConfigurationProperties() )
    {
      File confDir = configuration.getConfDir();
      if( confDir != null  )
        return confDir;
    }
    return null;
  }

}
