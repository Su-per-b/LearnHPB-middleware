package com.sri.straylight.client.framework;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

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
