package org.dis.sheet02.ui;

import java.awt.Component;
import java.util.function.Function;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class ObjectToStringRenderer<E> 
        extends DefaultListCellRenderer 
{
	/** Generated version UID */
	private static final long serialVersionUID = -5727316750566502561L;
	
	private final Function<E, String> converter;

	public ObjectToStringRenderer(Function<E, String> converter) {
		this.converter = converter;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
		@SuppressWarnings("unchecked")
		E e = (E) value;
		label.setText(converter.apply(e));
		return label;
	}

	
}
