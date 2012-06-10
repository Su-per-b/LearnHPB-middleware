package swingdemo.framework;

import swingdemo.model.Node;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;

/**
 * A tree model for Swing that works easily with a tree of <tt>Node</tt> implementors.
 *
 * @see Node
 * @author Christian Bauer
 */
public class EntityTreeModel implements TreeModel {

    private Node rootNode;

    public EntityTreeModel(Node rootNode) {
        this.rootNode = rootNode;
    }

    public Object getRoot() {
        return rootNode;
    }

    public boolean isLeaf(Object object) {
        Node node = (Node)object;
        boolean isLeaf = node.getChildren().size() == 0;
        return isLeaf;
    }

    public int getChildCount(Object parent) {
        Node node = (Node)parent;
        return node.getChildren().size();
    }

    public Object getChild(Object parent, int i) {
        Node node = (Node)parent;
        Object child = node.getChildren().get(i);
        return child;
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) return -1;
        Node node = (Node)parent;
        int index = node.getChildren().indexOf(child);
        return index;
    }

    // This method is invoked by the JTree only for editable trees.
    // This TreeModel does not allow editing, so we do not implement
    // this method.  The JTree editable property is false by default.
    public void valueForPathChanged(TreePath path, Object newvalue) {}

    // Since this is not an editable tree model, we never fire any events,
    // so we don't actually have to keep track of interested listeners
    public void addTreeModelListener(TreeModelListener l) {}
    public void removeTreeModelListener(TreeModelListener l) {}

}
