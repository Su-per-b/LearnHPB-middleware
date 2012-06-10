package swingdemo.ui.catalog;

import swingdemo.framework.DefaultEvent;
import swingdemo.model.Category;

public class CategorySelectedEvent extends DefaultEvent<Category> {

    public CategorySelectedEvent(Category payload) {
        super(payload);
    }
}
