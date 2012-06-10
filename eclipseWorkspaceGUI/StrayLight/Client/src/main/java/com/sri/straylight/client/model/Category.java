package com.sri.straylight.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Category implements Serializable, Node<Category> {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "CAT_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_CATEGORY_ID", nullable = true)
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @org.hibernate.annotations.BatchSize(size = 5) // Optimizes tree navigation (expanding nodes)
    private List<Category> children= new ArrayList<Category>();

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<Item>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_ON", nullable = false, updatable = false)
    private Date created = new Date();

    /**
     * No-arg constructor for JavaBean tools.
     */
    Category() {}

    /**
     * Full constructor.
     */
    public Category(String name, Category parent, Set<Item> items, List<Category> children) {
        this.name = name;
        this.parent= parent;
        this.items = items;
        this.children = children;
    }

    /**
     * Simple constructor.
     */
    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
        if (parent!= null)
            parent.getChildren().add(this);
    }

    // ********************** Accessor Methods ********************** //

    public Long getId() { return id; }

    // Immutable properties
    public Date getCreated() { return created; }

    // Regular properties

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Association management methods

    public Category getParent() { return parent; }
    public List<Category> getChildren() { return children; }

    // This relationship is immutable from this side, see Item
    public Set<Item> getItems() { return Collections.unmodifiableSet(items); }
    // Package visible only, so that Item can call these but nobody else
    void addItem(Item item) {
        // Add to collection
        this.items.add(item);
    }

    void removeItem(Item item) {
        // Remove from collection
        items.remove(item);
    }

    // ********************** Common Methods ********************** //

    // TODO: This is not completely safe: when the category is renamed, the hashcode changes
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Category category = (Category) o;

        if (!created.equals(category.created)) return false;
        return name.equals(category.name);
    }

    public int hashCode() {
        int result;
        result = name.hashCode();
        result = 29 * result + created.hashCode();
        return result;
    }

    public String toString() {
        return  getName();
    }


    // ********************** Business Methods ********************** //

    // None
}

