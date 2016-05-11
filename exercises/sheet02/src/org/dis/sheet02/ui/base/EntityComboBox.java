package org.dis.sheet02.ui.base;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dis.sheet02.entities.Estate;
import org.gnome.gtk.CellRendererText;
import org.gnome.gtk.ComboBox;
import org.gnome.gtk.ComboBox.Changed;
import org.gnome.gtk.DataColumn;
import org.gnome.gtk.DataColumnReference;
import org.gnome.gtk.DataColumnString;
import org.gnome.gtk.ListStore;
import org.gnome.gtk.TreeIter;
import org.gnome.gtk.VBox;

public class EntityComboBox<TElement extends Estate> extends VBox {

	private final DataColumnString contentColumn;
	private final DataColumnReference<TElement> objectColumn;
	
	private final ComboBox view;
	private final ListStore model;
	private final Function<TElement, String> labelSelector;
    private final Consumer<TElement> activatedHandler;
    
	public EntityComboBox( 
			Function<TElement, String> labelSelector,
			Consumer<TElement> changedHandler) {
		super(false, 0);
		
		this.labelSelector = labelSelector;
        this.activatedHandler = changedHandler;
        
		contentColumn = new DataColumnString();
		objectColumn =  new DataColumnReference<>();
		model = new ListStore(
				new DataColumn[] { objectColumn, contentColumn });
		
		view = new ComboBox(model);
		
		CellRendererText renderer = new CellRendererText(view);
		renderer.setText(contentColumn);
		this.add(view);
		
		view.connect(new Changed() {
			@Override
			public void onChanged(ComboBox source) {
				final TreeIter row = source.getActiveIter();
				if (row == null)
					return;
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
	
	public void selectValue(TElement value) {
		if (value == null) {
			view.setActive(-1);
			return;
		}
		TreeIter row = model.getIterFirst();
		if (row != null) {
			do {
				TElement element = model.getValue(row, objectColumn);
				if (value.equals(element)) {
					view.setActiveIter(row);
					return;
				}
			} while (row.iterNext());
		}
		view.setActive(-1);
	}
}
