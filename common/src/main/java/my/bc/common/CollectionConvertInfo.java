package my.bc.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * this annotation apply to class properties which type is Collection
 * in order to keep the information to convert to
 * 
 * @author Bright Chen
 *
 */
@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface CollectionConvertInfo
{
  Class< ? extends Collection > targetCollectionType() default Collection.class;
  Class<?> targetItemType();
}
