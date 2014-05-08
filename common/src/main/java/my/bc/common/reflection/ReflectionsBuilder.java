package my.bc.common.reflection;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ReflectionsBuilder
{
  private static ReflectionsBuilder defaultInstance;
  
  private String includeRegex;
  private String excludeRegex;

  public static ReflectionsBuilder defaultInstance()
  {
    if( defaultInstance == null )
    {
      synchronized( ReflectionsBuilder.class )
      {
        if( defaultInstance == null )
        {
          defaultInstance = new ReflectionsBuilder();
        }
      }
    }
    return defaultInstance;
  }
  
  public ConfigurationBuilder getTypicalConfigurationBuilder()
  {
    return ( new ConfigurationBuilder() ).filterInputsBy( getFilterBuilder() ).setUrls( ClasspathHelper.forManifest() );
  }
  
  public Reflections buildSubTypeReflections()
  {
    //the urls must be set or use the empty urls and not package searched
    return new Reflections( getTypicalConfigurationBuilder().setScanners( new SubTypesScanner() ) );
  }
  
  public Reflections buildResourcesReflections()
  {
    return new Reflections( getTypicalConfigurationBuilder().setScanners( new ResourcesScanner() ) );
  }

  public Reflections buildAnnotationReflections()
  {
    return new Reflections( getTypicalConfigurationBuilder().setScanners( new TypeAnnotationsScanner() ) );
  }
  
  protected FilterBuilder getFilterBuilder()
  {
    FilterBuilder fb = new FilterBuilder();
    fb.include( getIncludeRegex() );
    String er = getExcludeRegex();
    if( er != null && !er.isEmpty() )
      fb.exclude( er );
    return fb;
  }

  protected String getDefaultIncludeRegex()
  {
    return "my.bc.*";
  }
  public String getIncludeRegex()
  {
    return ( includeRegex == null ) ? getDefaultIncludeRegex() : includeRegex;
  }

  public void setIncludeRegex( String includeRegex )
  {
    this.includeRegex = includeRegex;
  }

  public String getExcludeRegex()
  {
    return excludeRegex;
  }

  public void setExcludeRegex( String excludeRegex )
  {
    this.excludeRegex = excludeRegex;
  }
}
