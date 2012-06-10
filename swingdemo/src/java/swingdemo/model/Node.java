package swingdemo.model;

import java.util.List;

/**
 * A contract that can be used to represent a hierarchical data structure.
 *
 * @author Christian Bauer
 */
public interface Node<T> {

    public Long getId();
    public T getParent();
    public List<T> getChildren();
    
}
