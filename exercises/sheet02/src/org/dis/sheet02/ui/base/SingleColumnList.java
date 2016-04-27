package org.dis.sheet02.ui.base;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.DataColumn;
import org.gnome.gtk.DataColumnReference;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.HBox;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.TreePath;
import org.gnome.gtk.TreeView;
import org.gnome.gtk.TreeViewColumn;

public class SingleColumnList<TElement> extends HBox {

	private final DataColumnString contentColumn;
	private final DataColumnReference<TElement> objectColumn;
	private final TreeView view;
	private final ListStore model;
	private final Function<TElement, String> labelSelector;
    private final Consumer<TElement> activatedHandler;
	
	/**
	 * Creates a new list.
	 * @param caption	
	 * 			The caption of the singular column.
	 * @param labelSelector 
	 * 			A function to select the display value of an object.
	 * @param rowActivatedHandler
	 * 			An action to execute when a row is activated.
	 */
	public SingleColumnList(String caption, 
			Function<TElement, String> labelSelector,
			Consumer<TElement> rowActivatedHandler) {
		super(true, 0);
		
		this.labelSelector = labelSelector;
        this.activatedHandler = rowActivatedHandler;
        
		contentColumn = new DataColumnString();
		objectColumn =  new DataColumnReference<>();
		model = new ListStore(
				new DataColumn[] { objectColumn, contentColumn });
		view = new TreeView(model);
		
		TreeViewColumn column = view.appendColumn();
		column.setTitle(caption);
		CellRendererText renderer = new CellRendererText(column);
		renderer.setText(contentColumn);
		this.add(view);
		
		view.connect(new TreeView.RowActivated() {
			public void onRowActivated(	TreeView source, 
                    					TreePath path, 
                    					TreeViewColumn vertical) 
			{
                final TreeIter row = model.getIter(path);
                final TElement backingValue = model.getValue(row, objectColumn);
                try {
					activatedHandler.accept(backingValue);
                }
                catch (Exception e) {
        			e.printStackTrace();
                }
            }
        });
	}
	
	public void setData(List<TElement> data) {
		model.clear();
		for (TElement value : data) {
			TreeIter row = model.appendRow();
			model.setValue(row, objectColumn, value);
			model.setValue(row, contentColumn, labelSelector.apply(value));
		}
	}
	
}
