package my.bc.configure;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import my.bc.common.util.CollectionUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

/**
 * load configuration properties by default.
 * the properties will take effect if the property didn't configured manually.
 * 
 * @author Bright Chen
 *
 */
public class DefaultConfigurationProperties implements ConfigurationProperties
{
  private static final Log log = LogFactory.getLog( DefaultConfigurationProperties.class );
  
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
      File file = createFile( fileName );
      if( file != null )
        files.add( file );
    }
    
    return CollectionUtil.copyToArray( new File[ files.size() ], files );
  }
  
  protected File createFile( String fileName )
  {
    File file = new File( fileName );
    if( !file.exists() )
    {
      log.warn( "File does NOT exist: " + file.getAbsolutePath() );
      return null;
    }
    if( !file.isFile() )
    {
      log.warn( "File is NOT a regular file: " + file.getAbsolutePath() );
      return null;
    }
    return file;
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
