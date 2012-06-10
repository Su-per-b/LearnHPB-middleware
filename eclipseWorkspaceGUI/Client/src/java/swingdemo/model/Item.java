package swingdemo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "ITEM")
public class Item implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ITEM_ID")
    private Long id = null;

    @Column(name = "ITEM_NAME", length = 255, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", length = 4000, nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CATEGORY_ITEM",
               joinColumns = {@JoinColumn(name = "ITEM_ID")},
               inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID")}
    )
    private Set<Category> categories = new HashSet<Category>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_ON", nullable = false, updatable = false)
    private Date created = new Date();

    /**
     * No-arg constructor for JavaBean tools.
     */
    Item() {}

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Simple properties only constructor.
     */

    // ********************** Accessor Methods ********************** //

    public Long getId() { return id; }

    // Immutable properties
    public Date getCreated() { return created; }

    // Regular properties
    public String getName() { return name; }
    public void setName(String name) { this.name = name;}

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Category> getCategories() { return Collections.unmodifiableSet(categories); }
    // Primary public methods for handling this relationship, it's not mutable from the Item side
    public void addToCategory(Category cat) {
        // Add to collection
        this.categories.add(cat);
        // Set backpointer
        cat.removeItem(this);
    }
    public void removeFromCategory(Category cat) {
        // Remove from collection
        this.categories.remove(cat);
        // Remove backpointer
        cat.removeItem(this);
    }

    // ********************** Common Methods ********************** //

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Item item = (Item) o;

        if (!created.equals(item.getCreated())) return false;
        return name.equals(item.getName());

    }

    public int hashCode() {
        int result;
        result = name.hashCode();
        result = 29 * result + created.hashCode();
        return result;
    }

    public String toString() {
        return  "Item ('" + getId() + "'), " +
                "Name: '" + getName() + "'";
    }


    // ********************** Business Methods ********************** //


}

