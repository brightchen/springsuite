package my.bc.persistent;

import org.hibernate.FlushMode;
import org.hibernate.Session;

/**
 * This class is intended to allow FortiDB to void write-behind problem where an unintended pojo
 * object dirty, but a query request would force hibernate to write the dirty pojo to database
 * before query happens.
 * 
 * The persistent configure would configure flush model to COMMIT, rather then AUTO However, the
 * default AUTO mode must be set for all Spring JPA DAO test case.
 * 
 * 
 * @author dtran
 * 
 */
@SuppressWarnings("serial")
public class PersistentJpaDialet
    extends org.springframework.orm.jpa.vendor.HibernateJpaDialect
{
    private FlushMode flushMode;

    public String getFlushMode()
    {
        return flushMode != null ? flushMode.toString() : null;
    }

    public void setFlushMode( String aFlushMode )
    {
        flushMode = FlushMode.parse( aFlushMode );
        if ( aFlushMode != null && flushMode == null )
        {
            throw new IllegalArgumentException( aFlushMode
                + " value invalid. See class org.hibernate.FlushMode for valid values" );
        }
    }

    protected Session getSession( javax.persistence.EntityManager em )
    {
        Session session = super.getSession( em );
        session.setFlushMode( flushMode );
        return session;
    }
}