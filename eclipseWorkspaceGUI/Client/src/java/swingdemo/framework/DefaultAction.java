package swingdemo.framework;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * An action without database transaction demarcation.
 *
 * @author Christian Bauer
 */
public abstract class DefaultAction extends AbstractAction {
    
    public void executeInController(AbstractController controller, ActionEvent event) {
        actionPerformed(event);
    }

}
