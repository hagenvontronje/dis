package org.dis.sheet02.ui.base;

import java.util.function.Consumer;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LambdaListSelectionListener<TElement> 
		implements ListSelectionListener 
{
	private Consumer<TElement> selectionAction;

	public LambdaListSelectionListener(Consumer<TElement> action) {
		this.selectionAction = action;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object source = e.getSource();
		if (e.getValueIsAdjusting() || !(source instanceof JList))
			return;
		@SuppressWarnings("unchecked")
		JList<TElement> list = (JList<TElement>) source;

		TElement element = list.getSelectedValue();
		if (element == null)
			return;
		
		try {
			selectionAction.accept(element);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
