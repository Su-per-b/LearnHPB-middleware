package swingdemo.framework;

import org.hibernate.Session;
import org.hibernate.FlushMode;
import org.hibernate.context.ManagedSessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;

import swingdemo.util.HibernateUtil;

/**
 * A controller that includes persistence context management.
 * <p>
 * Subclass and implement this controller if your actions (or your UI components)
 * need data access through a Hibernate <tt>Session</tt> An instance of this class
 * either joins an existing persistence context (for example, several HMVC triads
 * share the same persistence context), or manages its own persistence context.
 * This decision has to be made when you decide which constructor to use. Note that
 * the persistence context is never flushed automatically! You always have to call
 * <tt>flush()</tt> to synchronize in-memory state with the database, and to execute
 * <i>any</i> SQL DML operations.
 * <p>
 * A subclass can call <tt>getPersistenceContext()</tt> at all times and access the
 * database. Even Swing UI components can pull data out of the persistent objects,
 * the persistence context the view of this controller accesses the model. You need
 * to enable <tt>hibernate.connection.autocommit</tt> in the Hibernate configuration
 * for this to work predictably. Read (and write!) access is then executed with
 * auto-committed transactions, whenever the persistence context is used.
 * Every single SQL statement (including DML!) is then wrapped automatically in a very
 * short transaction.
 * <p>
 * Every action that is registered on this controller can use the "current" persistence
 * context, in three ways:
 * <ul>
 * <li>Calling <tt>getPersistenceContext()</tt>; returns a <tt>Session</tt> API and
 * all SQL is executed in auto-commit transaction mode.
 * <li>Calling <tt>HibernateUtil.getSessionFactory().getCurrentSession()</tt> returns
 * the current <tt>Session</tt> and all SQL is executed in auto-commit transaction
 * mode. This controller binds the "current" persistence context into Hibernate before
 * action exeuction.
 * <li>Registering a <tt>DataAccessAction</tt> and using the <tt>Session</tt> that is
 * passed into the actions <tt>execute()</tt> operation. This is the preferred strategy,
 * because <tt>execute()</tt> is also wrapped in a single database transaction, no
 * auto-commit mode is enabled. Note that auto-commit is also disabled on any other
 * "current" persistence context and <tt>Session</tt>, inside the <tt>execute()</tt>
 * method of a <tt>DataAccessAction</tt>.
 * </ul>
 *
 * @see swingdemo.framework.DataAccessAction
 * @author Christian Bauer
 */
public abstract class PersistenceController extends AbstractController {

    private static Log log = LogFactory.getLog(PersistenceController.class);

    private Session persistenceContext;
    private boolean ownsPersistenceContext;

    /**
     * Subclass wants to control own view and is root controller. Gets a fresh persistence context.
     */
    public PersistenceController() {}

    /**
     * Subclass wants to control own view and is subcontroller. Gets a fresh persistence context.
     *
     * @param parentController
     */
    public PersistenceController(AbstractController parentController) {
        this(null, parentController, null);
    }

    /**
     * Subclass is completely dependend on the given view and is a subcontroller. Gets a fresh persistence context.
     *
     * @param view
     * @param parentController
     */
    public PersistenceController(Container view, AbstractController parentController) {
        this(view, parentController, null);
    }

    /**
     * Subclass wants to control own view and is subcontroller. Also joins given persistence context.
     *
     * @param parentController
     * @param persistenceContext
     */
    public PersistenceController(AbstractController parentController, Session persistenceContext) {
        this(null, parentController, persistenceContext);
    }

    /**
     * Subclass is completely dependend on the given view and is a subcontroller. Also joins given persistence context.
     *
     * @param view
     * @param parentController
     * @param persistenceContext
     */
    public PersistenceController(Container view, AbstractController parentController, Session persistenceContext) {
        super(view, parentController);

        // Open a new persistence context for this controller, if none should be joined
        if (persistenceContext == null) {
            log.debug("Creating new persistence context for controller");
            this.persistenceContext = HibernateUtil.getSessionFactory().openSession();
            this.persistenceContext.setFlushMode(FlushMode.MANUAL);
            this.ownsPersistenceContext = true;
        } else {
            log.debug("Joining existing persistence context");
            this.persistenceContext = persistenceContext;
        }
    }

    public Session getPersistenceContext() {
        return persistenceContext;
    }

    /**
     * Close the persistence context if this controller owns it.
     * <p>
     * Cascades down in the controller hierarchy and closes all
     * persistence contexts that might be owned by subcontrollers.
     *
     */
    public void dispose() {
        if (ownsPersistenceContext && getPersistenceContext().isOpen()) {
            log.debug("Closing persistence context");
            getPersistenceContext().close();
        }
        super.dispose();
    }

    protected void preActionExecute() {
        log.debug("Binding current persistence context to Hibernate");
        org.hibernate.classic.Session currentSession =
                (org.hibernate.classic.Session)this.getPersistenceContext();
        ManagedSessionContext.bind(currentSession);

    }

    protected void finalActionExecute() {
        log.debug("Unbinding current persistence context from Hibernate");
        ManagedSessionContext.unbind(HibernateUtil.getSessionFactory());
    }

}
