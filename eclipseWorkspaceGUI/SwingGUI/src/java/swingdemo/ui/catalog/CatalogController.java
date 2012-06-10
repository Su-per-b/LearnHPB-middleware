package swingdemo.ui.catalog;

import swingdemo.framework.*;
import swingdemo.model.Category;
import swingdemo.model.Item;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class CatalogController extends PersistenceController {

    // View
    private JFrame categoryListWindow;
    private	JTree categoryTree;
    private JScrollPane categoryTreePane;

    JButton newButton;
    JButton editButton;
    JButton deleteButton;

    // Model
    private EntityTreeModel categoryTreeModel;
    private Category selectedCategory;

    // Actions
    public static String[] ACTION_NEW = {"New", "newCategory"};
    public static String[] ACTION_EDIT = {"Edit", "editCategory"};
    public static String[] ACTION_DELETE = {"Delete", "deleteCategory"};
    public static String[] ACTION_REFRESH = {"Refresh", "refreshTree"};

    public CatalogController(AbstractController parentController) {
        super(new JFrame("Categories"), parentController);
        categoryListWindow = (JFrame)getView();

        // Create view components
        refreshCategoryTree( getPersistenceContext() );
        categoryTreePane = new JScrollPane(categoryTree);
        categoryTreePane.setMinimumSize(new Dimension(150,150));
        categoryTreePane.setPreferredSize(new Dimension(150, 250));

        newButton = new JButton(ACTION_NEW[0]);
        editButton = new JButton(ACTION_EDIT[0]);
        editButton.setEnabled(false);
        deleteButton = new JButton(ACTION_DELETE[0]);
        deleteButton.setEnabled(false);

        // Bind and register actions
        registerAction(newButton, ACTION_NEW[1],
            new DefaultAction() {
                public void actionPerformed(ActionEvent actionEvent) {
                    // Use the selected category as the parent for the new one
                    Category newCategory = new Category("New category", selectedCategory);
                    JDialog dialog = new CategoryDialog(categoryListWindow, newCategory);
                    new EditNewDialogController<Category>(dialog,
                                                          CatalogController.this,
                                                          getPersistenceContext(),
                                                          newCategory) {
                        public void fireSaveEvents() {
                            CatalogController.this.fireEvent(new CategoryModifiedEvent(selectedCategory));
                        }
                    };
                }
            }
        );
        registerAction(editButton, ACTION_EDIT[1],
            new DefaultAction() {
                public void actionPerformed(ActionEvent actionEvent) {

                    JDialog dialog = new CategoryDialog(categoryListWindow, selectedCategory);
                    new EditNewDialogController<Category>(dialog,
                                                          CatalogController.this,
                                                          getPersistenceContext(),
                                                          selectedCategory) {
                        public void fireSaveEvents() {
                            CatalogController.this.fireEvent(new CategoryModifiedEvent(selectedCategory));
                        }
                    };
                }
            }
        );

        registerAction(deleteButton,ACTION_DELETE[1],
            new DataAccessAction() {
                protected boolean preTransaction() {
                    // Alternatively, just don't enable the Delete button if the selectedCatgory has children!
                    if (selectedCategory.getChildren().size() > 0) {
                        JOptionPane.showMessageDialog(
                            categoryListWindow,
                            "Sorry, deletion of categories with subcategories is not implemented!"
                        );
                        return true; // Veto and abort execution
                    }
                    return false;
                }

                public void actionPerformed(ActionEvent actionEvent, Session currentSession) {
                    // Remove from parent category
                    if (selectedCategory.getParent() != null)
                        selectedCategory.getParent().getChildren().remove(selectedCategory);
                    // Remove all links to all items (well _from_ them, need to use the non-inverse side)
                    for (Item item : selectedCategory.getItems()) {
                        item.removeFromCategory(selectedCategory);
                    }
                    currentSession.delete(selectedCategory);
                    currentSession.flush();
                }
                protected void postTransaction() {
                    CatalogController.this.fireEventGlobal(new CategoryDeletedEvent(selectedCategory));
                    selectedCategory = null;
                }
            }
        );

        // Register events
        registerEventListener(
            CategoryModifiedEvent.class,
            new DefaultEventListener() {
                public void handleEvent(DefaultEvent event) {
                    refreshCategoryTree(getPersistenceContext());
                }
            }
        );
        registerEventListener(
            CategoryDeletedEvent.class,
            new DefaultEventListener() {
                public void handleEvent(DefaultEvent event) {
                    refreshCategoryTree(getPersistenceContext());
                }
            }
        );

        // Assemble the view
        categoryListWindow.add(categoryTreePane, BorderLayout.PAGE_START);
        categoryListWindow.add(ButtonBarFactory.buildGrowingBar(deleteButton, editButton, newButton), BorderLayout.PAGE_END);

        // Window close event
        categoryListWindow.addWindowListener(this);
        categoryListWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Display the window
        categoryListWindow.pack();
        categoryListWindow.setResizable(false);
        categoryListWindow.setVisible(true);
    }


    private void refreshCategoryTree(Session currentSession) {
        Category rootNode = new Category("Root");
        java.util.List categories = currentSession.createCriteria(Category.class)
                                .add(Restrictions.isNull("parent")).list();
        rootNode.getChildren().addAll(categories);
        categoryTreeModel = new EntityTreeModel(rootNode);

        if (categoryTree == null) {
            categoryTree = new JTree(categoryTreeModel);
            categoryTree.setRootVisible(false);
            categoryTree.setShowsRootHandles(true);
            categoryTree.setEditable(false);

            // Tree selection model and local listener action
            categoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            categoryTree.addTreeSelectionListener(
                new TreeSelectionListener() {
                    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                        selectedCategory = (Category) treeSelectionEvent.getPath().getLastPathComponent();
                        editButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        // This would be better, but we want to show the preTransaction() earlier in the code
                        //if (selectedCategory.getChildren().size() > 0) deleteButton.setEnabled(false);
                        fireEventGlobal(new CategorySelectedEvent(selectedCategory));
                    }
                }
            );
        } else {
            categoryTree.setModel(categoryTreeModel);
        }
    }

}
