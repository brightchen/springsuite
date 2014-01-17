package my.bc.configure;

import java.io.File;

/**
 * This interface provides methods for configuration properties instead of functional properties.
 * namely the properties for locate/manage property files
 * 
 * @author Bright Chen
 *
 */
public interface ConfigurationProperties
{
  /**
   * get all property files, all the property file returned should be exists and 
   * @return
   */
  public File[] getPropertyFiles();
  public File getHomeDir();
  public File getConfDir();
}
