package my.bc.studio.dbmetadata;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MetaDataApp
{
  private final String connection_url = "jdbc:derby:${basedir}/target/general-derby;create=false";
  private final String connection_username = "sa";
  private final String connection_password = "";
  private final String connection_driver_class = "org.apache.derby.jdbc.EmbeddedDriver";
  //private final String dialect = "org.hibernate.dialect.DerbyDialect";

  protected JdbcTemplate jdbcTemplate;

  public static void main( String[] argvs )
  {
    MetaDataApp app = new MetaDataApp();
    app.createJdbcTemplate();
  }
  
  protected void createJdbcTemplate()
  {
    DriverManagerDataSource dataSource = buildDataSource();
    jdbcTemplate = new JdbcTemplate(dataSource);
  }
  
  protected DriverManagerDataSource buildDataSource()
  {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(connection_driver_class);
    dataSource.setUrl(connection_url);
    dataSource.setUsername(connection_username);
    dataSource.setPassword(connection_password);
    
    return dataSource;
  }
}
