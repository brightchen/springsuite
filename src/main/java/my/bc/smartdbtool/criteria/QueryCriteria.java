package my.bc.smartdbtool.criteria;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import my.bc.common.classproperty.ClassProperty;
import my.bc.common.util.EntityUtil;
import my.bc.common.util.StringUtil;


public class QueryCriteria implements IQueryCriteria
{
  private String entityAlias;
  private ClassProperty property;
  private Operator operator;
  private List<Object> parameters;
  
  public QueryCriteria(){}
  
  public QueryCriteria( ClassProperty property, Operator operator, Object ... parameters )
  {
    this( EntityUtil.getPropertyStyleName( property.getDeclaringClass() ), property, operator, parameters );
  }
  
  public QueryCriteria( String entityAlias, ClassProperty property, Operator operator, Object ... parameters )
  {
    setEntityAlias( entityAlias );
    setProperty( property );
    setOperator( operator );
    setParameters( parameters );
  }
  
  /**
   * get the hsql for the criteria
   * for example sum( user1.age between ( 10, 20 )
   */
  @Override
  public String getHsql()
  {
    String hsql = String.format( "( %s %s %s )", getFunctionForProperty( entityAlias, property.getName() ), 
                                 operator.getKeyword(), getSqlParameterList() );
    return hsql;
  }

  @Override
  @SuppressWarnings( "rawtypes" )
  public void addAliases( Map< String, Class > aliases ) 
  {
    aliases.put( entityAlias, property.getDeclaringClass() );
  }

  /*
   * this method provides an opportunities to use the sql functions, for example upper();
   * the sub-class can override this method to use different sql functions
   * this implementation return no function
   */
  public String getFunctionForProperty( String beanAlias, String propertyName )
  {
    return String.format( "%s.%s", beanAlias, propertyName );
  }
  
  /*
   * need to get the type of property in order to determine if should add quote
   */
  public String getSqlParameterList()
  {
    if( NumberOfParameter.One.equals( operator.getNumberOfParameter() ) )
    {
      return getSqlParameter( parameters.get( 0 ) );
    }
    
    
    {
      StringBuilder sqlParameter = new StringBuilder( "( " );
      for( Object parameter : parameters )
      {
        sqlParameter.append( getSqlParameter( parameter ) + ", " );
      }
      sqlParameter.setCharAt( sqlParameter.length() - 2, ')' ); //replace the last ',' to ')'
      
      return sqlParameter.toString();
    }
  }

  protected String getSqlParameter( Object parameter )
  {
    String parameterString = StringUtil.toString( parameter );
    return String.class.equals( property.getPropertyRawType() ) ? "'" + parameterString + "'" : parameterString;
  }

  public String getEntityAlias()
  {
    return entityAlias;
  }

  public void setEntityAlias( String entityAlias )
  {
    this.entityAlias = entityAlias;
  }

  public ClassProperty getProperty()
  {
    return property;
  }

  public void setProperty( ClassProperty property )
  {
    this.property = property;
  }

  public Operator getOperator()
  {
    return operator;
  }

  public void setOperator( Operator operator )
  {
    this.operator = operator;
  }

  public List< Object > getParameters()
  {
    return parameters;
  }
  public void setParameters( List< Object > parameters )
  {
    this.parameters = parameters;
  }
  public void setParameters( Object ... parameters )
  {
    setParameters( Arrays.asList( parameters ) );
  }
  
  
}
