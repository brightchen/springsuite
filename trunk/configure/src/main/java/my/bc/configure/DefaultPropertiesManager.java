package my.bc.configure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultPropertiesManager implements PropertiesManager
{
  private static final Log log = LogFactory.getLog( DefaultPropertiesManager.class );
  
  public static final DefaultPropertiesManager instance = new DefaultPropertiesManager();
  protected DefaultPropertiesManager(){}
  
  // this class maybe run at different environment, say, web application or stand-alone application
  // so, it can't tell it's context path, let the caller set this parameter;
  private String contextPath;
  private ConfigurationProperties configurationProperties = ChainedConfigurationProperties.instance;
  
  
  public String getContextPath()
  {
    return contextPath;
  }
  public void setContextPath(String contextPath)
  {
    this.contextPath = contextPath;
  }

  @Override
  public void populateProperties()
  {
    File[] propertyFiles = configurationProperties.getPropertyFiles();
    if( propertyFiles == null )
      return;

    Properties properties = new Properties();
    for( File propertyFile : propertyFiles )
    {
      loadProperties( properties, propertyFile );
    }
    
    Enumeration<Object> keys = properties.keys();
    while( keys.hasMoreElements() )
    {
      String key = (String)keys.nextElement();
      
      //we should NOT override the system property
      if( System.getProperty( key ) != null )
        continue;
      System.setProperty( key, properties.getProperty(key) );
    }
  }

  protected void loadProperties( Properties properties, File propertyFile )
  {
    try
    {
      FileReader fr = new FileReader( propertyFile );
      properties.load(fr);
    }
    catch( FileNotFoundException fnfe )
    {
      log.warn( "FileNotFoundException: " + fnfe.getMessage() );
      log.warn( "Can NOT find file: " + propertyFile.getAbsolutePath() );
    }
    catch( IOException ioe )
    {
      log.warn( "IOException: " + ioe.getMessage() );
    }

  }
  
}
