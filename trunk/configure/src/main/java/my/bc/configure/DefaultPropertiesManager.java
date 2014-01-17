package my.bc.configure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class DefaultPropertiesManager implements PropertiesManager
{
  @Resource( name="chainedConfigurationProperties" )
  private ConfigurationProperties configurationProperties;
  
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
      
    }
    catch( IOException ioe )
    {
      
    }

  }
  
}
