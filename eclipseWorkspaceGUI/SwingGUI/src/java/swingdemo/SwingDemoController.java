package swingdemo;

import swingdemo.framework.AbstractController;
import swingdemo.ui.catalog.CatalogController;
import swingdemo.ui.catalog.ItemListController;

import javax.swing.*;

public class SwingDemoController extends AbstractController {

    // View
    private JFrame mainWindow;

    public SwingDemoController() {
        super(new JFrame("Item List"), null);
        mainWindow = (JFrame)getView();

        // Decoration etc.
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Category list in own window, own persistence context,
        new CatalogController(this);

        // Item list in this window
        mainWindow.add(new ItemListController(this).getView());

        // Display the window
        mainWindow.pack();
        mainWindow.setLocation(300,0);
        mainWindow.setVisible(true);
    }
}
