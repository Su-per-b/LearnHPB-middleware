package swingdemo.ui.catalog;

import swingdemo.model.Category;
import swingdemo.framework.DefaultEvent;

public class CategoryDeletedEvent extends DefaultEvent<Category> {

    public CategoryDeletedEvent(Category payload) {
        super(payload);
    }
}
