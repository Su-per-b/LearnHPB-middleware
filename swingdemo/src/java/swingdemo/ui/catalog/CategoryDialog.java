package swingdemo.ui.catalog;

import swingdemo.model.Category;

import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;

public class CategoryDialog extends JDialog {

    public CategoryDialog(Frame owner, Category category) {
        super(owner);

        // Assemble dialog
        setResizable(false);
        getContentPane().add(buildForm(category), BorderLayout.PAGE_START);
    }

    public JPanel buildForm(Category category) {
        FormLayout layout = new FormLayout(
            "l:p, 4dlu, p:g"
        );
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

        // Binding
        BeanAdapter beanAdapter = new BeanAdapter(category);
        ValueModel nameModel = beanAdapter.getValueModel("name");

        builder.append("Category name:", BasicComponentFactory.createTextField(nameModel));
        builder.append("Parent category:",
                category.getParent() ==  null
                    ? new JLabel("(Root)")
                    : new JLabel(category.getParent().getName()) );

        return builder.getPanel();
    }
}
