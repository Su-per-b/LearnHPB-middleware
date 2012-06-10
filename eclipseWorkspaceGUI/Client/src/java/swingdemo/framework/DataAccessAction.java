package swingdemo.framework;

import swingdemo.util.HibernateUtil;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.ActionEvent;

/**
 * Overrides executeInController() and adds transaction demarcation.
 *
 * @author Christian Bauer
 */
public abstract class DataAccessAction extends DefaultAction {

    private static Log log = LogFactory.getLog(DataAccessAction.class);

    public DataAccessAction() {}

    public void actionPerformed(ActionEvent actionEvent) {
        actionPerformed(actionEvent, HibernateUtil.getSessionFactory().getCurrentSession());
    }

    public void executeInController(AbstractController controller, ActionEvent event) {
        PersistenceController persistenceController;
        if (controller instanceof PersistenceController)
            persistenceController = (PersistenceController) controller;
        else
            throw new IllegalArgumentException(
                "Controller: " + controller.getClass() + " is not a PersistenceController"
            );

        if (preTransaction()) { return; }
        try {
            log.debug("Beginning database transaction");
            persistenceController.getPersistenceContext().beginTransaction();

            log.debug("Executing action event");
            actionPerformed(event);

            log.debug("Committing database transaction");
            persistenceController.getPersistenceContext().getTransaction().commit();
        } catch (RuntimeException ex) {
            log.debug("Rolling back database transaction");
            persistenceController.getPersistenceContext().getTransaction().rollback();
            failedTransaction();
            throw ex;
        }
        postTransaction();
    }

    /**
     * Executes after transaction completion, Session is in auto-commit mode.
     *
     * @return Return <tt>true</tt> to abort execution.
     */
    protected boolean preTransaction() { return false; }

    /**
     * Executes before transaction begins, Session is in auto-commit mode.
     */
    protected void postTransaction() {}

    /**
     * Executes when transaction failed, after rollback. Session can't be used anymore.
     */
    protected void failedTransaction() {}

    /**
     * The main execute routine of this action.
     * 
     * @param actionEvent
     * @param currentSession Use this Session to access the database.
     */
    public abstract void actionPerformed(ActionEvent actionEvent, Session currentSession);

}
