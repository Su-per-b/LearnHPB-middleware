package swingdemo.ui.catalog;

import swingdemo.framework.*;
import swingdemo.model.Category;
import swingdemo.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ItemListController extends PersistenceController {

    // View
    JPanel panel;
    JLabel label;
    JTable itemTable;

    // Model
    EntityTableModel itemTableModel;

    public ItemListController(AbstractController parentController) {
        super(new JPanel(), parentController);
        panel = (JPanel)getView();

        // Decoration etc.
        panel.setMinimumSize(new Dimension(500,300));
        panel.setPreferredSize(new Dimension(500, 300));

        // View components
        label = new JLabel("Please select a category.");

        itemTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(itemTable);
        itemTable.setPreferredScrollableViewportSize(new Dimension(500, 280));

        // Assemble the view
        panel.add(label, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Event listeners
        registerEventListener(
            CategorySelectedEvent.class,
            new DefaultEventListener<Category>() {
                public void handleEvent(DefaultEvent<Category> event) {
                    refreshItemList(event.getPayload());
                }
            }
        );
        // This is actually not necessary in the demo, because editing a category also
        // results in a refresh of the category tree, which fires a CategorySelectedEvent.
        // So editing a Category results in two events being fired. This is OK, because
        // the persistence contexts caches and the second call of refreshItemList() doesn't
        // hit the database.
        registerEventListener(
            CategoryModifiedEvent.class,
            new DefaultEventListener<Category>() {
                public void handleEvent(DefaultEvent<Category> event) {
                    refreshItemList(event.getPayload());
                }
            }
        );
        registerEventListener(
            CategoryDeletedEvent.class,
            new DefaultEventListener<Category>() {
                public void handleEvent(DefaultEvent<Category> event) {
                    label.setText("Category '" + event.getPayload().getName() + "' has been deleted.");
                    itemTable.setModel(new DefaultTableModel());
                }
            }
        );
    }

    private void refreshItemList(Category category) {
        label.setText("Selected category: " + category.getName());
        itemTableModel = new EntityTableModel<Item>(Item.class, category.getItems());
        itemTableModel.addColumn("Item Name", "name");
        itemTableModel.addColumn("Description", "description");
        itemTableModel.addColumn("Created On", "created");
        itemTable.setModel(itemTableModel);
    }

}
