package my.bc.persistent;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class BcBasicDataSource extends org.apache.commons.dbcp.BasicDataSource
{

  public final static String FROMFILE_PREFIX = "FROMFILE:";
  public final static String ENCRYPT_PREFIX  = "ENCODED:";
  public final static String ENCRYPT_KEY     = "my.bc";

  private static Logger      logger          = Logger.getLogger( BcBasicDataSource.class );

  public BcBasicDataSource()
  {
    super();
  }

  protected DataSource createDataSource() throws SQLException
  {
    try
    {
      DataSource ds = super.createDataSource();
      return ds;
    }
    catch( SQLException sqle )
    {
      sqle.printStackTrace();
      throw new SQLException( sqle );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      throw new SQLException( e );
    }
  }

  protected void createDataSourceInstance() throws SQLException
  {
    try
    {
      super.createDataSourceInstance();
    }
    catch( SQLException sqle )
    {
      sqle.printStackTrace();
      throw new SQLException( sqle );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
  
  public Connection getConnection() throws SQLException
  {
    try
    {
      Connection conn = super.getConnection();
      return conn;
    }
    catch( SQLException sqle )
    {
      sqle.printStackTrace();
      throw new SQLException( sqle );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      throw new SQLException( e );
    }
  }
  
  public Connection getConnection(String user, String pass) throws SQLException
  {
    try
    {
      // BasicDataSource does NOT support getConnection( String, String ), 
      // and UnsupportedOperationException exception will be thrown.
      // Why and how come this method be called? and in fact the input parameters are not expected.  ??
      Connection conn = super.getConnection( user, pass );
      return conn;
    }
    catch( SQLException sqle )
    {
      sqle.printStackTrace();
      throw new SQLException( sqle );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      throw new SQLException( e );
    }
  
  }
      
  private String decode( String value )
  {
    String strResult = value;

    if ( strResult.startsWith( FROMFILE_PREFIX ) )
    { // load from file
      String[] sa = strResult.substring( FROMFILE_PREFIX.length() ).split( "\\|" ); // FROMFILE:file:key
      if ( sa.length != 2 )
      {
        logger.error( "Invalid setting: " + value );
      }
      else
      {
        File f = new File( sa[0] );
        if ( f.exists() )
        {
          logger.debug( "load setting from file, file = " + sa[0] + ", key = " + sa[1] );
          try
          {
            Properties props = new Properties();
            props.load( new FileInputStream( sa[0] ) );

            strResult = props.getProperty( sa[1] );

          }
          catch ( Exception e )
          {
            logger.error( "exception when loading settings, Message=\n" + e.getMessage() );
          }
        }
        else
        {
          logger.error( "settings file does not exist, file = " + sa[0] );
        }
      }

    }

    if ( strResult.startsWith( ENCRYPT_PREFIX ) )
    { // encrypted
      String encrypted = strResult;
      // strResult = DESEncrypt.decrypt(ENCRYPT_KEY, strResult.substring(ENCRYPT_PREFIX.length()));

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Decode username/password for datasource: " + encrypted + " -> " + strResult );
      }
    }

    return strResult;

  }

  public synchronized void setUsername( String username )
  {
    this.username = decode( username );
  }

  public synchronized void setPassword( String password )
  {
    this.password = decode( password );
  }
}
