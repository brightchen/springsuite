package my.bc.configure;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import my.bc.common.util.CollectionUtil;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.springframework.stereotype.Component;

/**
 * load configuration properties by default.
 * the properties will take effect if the property didn't configured manually.
 * 
 * @author Bright Chen
 *
 */
@Component( "configurationProperties" )
public class DefaultConfigurationProperties implements ConfigurationProperties
{
  @Override
  public File[] getPropertyFiles()
  {
    return getPropertiesFileFromClassPath( "my.bc" );
  }

  /**
   * get the properties files from class path by default.
   * 
   * @param superPackageName the super package name under which
   * @return
   */
  protected File[] getPropertiesFileFromClassPath( String superPackageName )
  {
    Reflections reflections = new Reflections( "my.bc", new ResourcesScanner() );
    Set<String> fileNames = reflections.getResources( Pattern.compile( "\\w+\\.properties" ) );
    if( fileNames == null || fileNames.isEmpty() )
      return new File[0];
    
    Set<File> files = new HashSet<File>();
    for( String fileName : fileNames )
    {
      files.add( new File( fileName ) );
    }
    
    return CollectionUtil.copyToArray( new File[ files.size() ], files );
  }
  
  @Override
  public File getHomeDir()
  {
    return new File( "." );
  }
  

  @Override
  public File getConfDir()
  {
    return new File( getHomeDir(), "conf" );
  }
}
