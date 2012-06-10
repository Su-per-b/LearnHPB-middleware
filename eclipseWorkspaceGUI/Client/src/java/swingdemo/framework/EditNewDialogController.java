package swingdemo.framework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import org.hibernate.Session;
import com.jgoodies.forms.factories.ButtonBarFactory;

/**
 * A convenience subclass for basic forms that edit or create some model objects.
 * <p>
 * This controllers view is always a <tt>Dialog</tt>, usually modal. It adds a button bar
 * with <i>Save</i> and <i>Cancel</i> buttons to the bottom of the dialog. The actions for
 * these two buttons are simple. Saving calls <tt>merge()</tt> for all entity instances, using
 * the current persistence context. Cancel just disposes everything.
 * <p>
 * You can override the <tt>fireSaveEvent()</tt> and <tt>fireCancelEvents</tt> if you want
 * something to happen after the actions have been executed.
 *
 * @author Christian Bauer
 */
public abstract class EditNewDialogController<T> extends PersistenceController {

    // View
    JDialog formDialog;

    // Model
    T[] entityInstances;

    // Actions
    public static String[] ACTION_SAVE = {"Save", "save"};
    public static String[] ACTION_CANCEL = {"Cancel", "cancel"};

    public EditNewDialogController(JDialog dialog, AbstractController parentController, Session persistenceContext, T... entities) {
        super(dialog, parentController, persistenceContext);
        this.formDialog = (JDialog)getView();
        this.entityInstances = entities;

        // Buttons and actions
        JButton save= new JButton(ACTION_SAVE[0]);
        registerAction(
            save,
            ACTION_SAVE[1],
            new DataAccessAction() {
                public void actionPerformed(ActionEvent actionEvent, Session currentSession) {
                    for (T entity : entityInstances) entity = (T)currentSession.merge(entity);
                    currentSession.flush();
                }
                protected void postTransaction() {
                    formDialog.setVisible(false);
                    formDialog.dispose();
                    fireSaveEvents();
                    dispose();
                }
            }
        );
        JButton cancel = new JButton(ACTION_CANCEL[0]);
        registerAction(
            cancel,
            ACTION_CANCEL[1],
            new DefaultAction() {
                public void actionPerformed(ActionEvent actionEvent) {
                    formDialog.setVisible(false);
                    formDialog.dispose();
                    fireCancelEvents();
                    dispose();
                }
            }
        );
        formDialog.add(ButtonBarFactory.buildOKCancelBar(save, cancel), BorderLayout.PAGE_END);

        // Window Decoration
        formDialog.setModal(true);

        // Window close event
        formDialog.addWindowListener(this);
        formDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Display the window
        formDialog.pack();
        formDialog.setVisible(true);

    }

    /**
     * NOOP by default, override it to fire an event after the saving transaction is committed.
     */
    public void fireSaveEvents() {}

    /**
     * NOOP by default, override it to fire an event after the form dialog has been disposed.
     */
    public void fireCancelEvents() {}

}
