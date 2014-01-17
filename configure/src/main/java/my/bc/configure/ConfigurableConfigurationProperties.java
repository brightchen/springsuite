package my.bc.configure;

import java.io.File;

import org.springframework.stereotype.Component;

/**
 * load configuration properties from manually configuration
 * 
 * @author Bright Chen
 *
 */
@Component( "configurationProperties" )
public class ConfigurableConfigurationProperties implements ConfigurationProperties
{
  public static final ConfigurationProperties DEF_INST = new ConfigurableConfigurationProperties();
  
  /**
   * get all configure property files.
   * it should not include the i18n property files
   * 
   * @return
   */
  public File[] getPropertyFiles(){ return null; }
  
//  public File[] getPropertyFiles()
//  {
//    //get from system properties first
//    String propertyFileList = PropertiesUtil.getSystemProperty( PropertiesKey.PROPERTY_FILE_LIST );
//    if( propertyFileList != null && !propertyFileList.isEmpty() )
//    {
//      //seperate the file path list
//      String[] filePaths = propertyFileList.split( ":" );
//      if( filePaths == null )
//        filePaths = propertyFileList.split( ";" );
//      
//      List< File > files = new ArrayList< File >();
//      for( String filePath : filePaths )
//      {
//        if( filePath == null || filePath.isEmpty() )
//          continue;
//        if( isClassPath( filePath ) )
//        {
//          files.addAll( getPropertiesFileFromClassPath( filePath ) );
//        }
//        if( isAbsoluteFilePath( filePath ) )
//          files.add( new File( filePath) );
//        else
//          files.add( new File( getConfDir(), filePath ) );
//      }
//      return files.toArray( new File[0] );
//      
//    }
//    
//    return null;
//  }
//
//  
  
  protected boolean isAbsoluteFilePath( String filePath )
  {
    return filePath.startsWith( "/" );
  }
  
  protected boolean isClassPath( String filePath )
  {
    return ( filePath.startsWith( "classpath:" ) || filePath.startsWith( "classpath*:" ) );
  }
  
  public File getHomeDir()
  {
    String homeDirPath = PropertiesUtil.getSystemProperty( PropertiesKey.HOME_DIR );
    if( homeDirPath != null )
    {
      File homeDir = new File( homeDirPath );
      if( homeDir.isDirectory() )
        return homeDir;
    }
    
    return null;
  }
  
  public File getConfDir()
  {
    String confDirPath = PropertiesUtil.getSystemProperty( PropertiesKey.CONF_DIR );
    if( confDirPath != null )
    {
      File confDir = new File( confDirPath );
      if( confDir.isDirectory() )
        return confDir;
    }
    
    return null;
  }
  
}
