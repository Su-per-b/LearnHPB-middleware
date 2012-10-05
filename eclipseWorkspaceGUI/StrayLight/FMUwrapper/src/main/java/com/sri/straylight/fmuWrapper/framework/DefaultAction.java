package com.sri.straylight.fmuWrapper.framework;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

// TODO: Auto-generated Javadoc
/**
 * An action without database transaction demarcation.
 *
 * @author Christian Bauer
 */
public abstract class DefaultAction extends AbstractAction {
    
    /**
     * Execute in controller.
     *
     * @param controller the controller
     * @param event the event
     */
    public void executeInController(AbstractController controller, ActionEvent event) {
        actionPerformed(event);
    }

}
