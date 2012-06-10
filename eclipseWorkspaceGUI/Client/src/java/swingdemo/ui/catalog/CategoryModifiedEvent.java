package swingdemo.ui.catalog;

import swingdemo.framework.DefaultEvent;
import swingdemo.model.Category;

public class CategoryModifiedEvent extends DefaultEvent<Category> {

    public CategoryModifiedEvent(Category payload) {
        super(payload);
    }
}
