package org.dis.sheet02.ui.base;

import java.awt.BorderLayout;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.dis.sheet02.ui.ObjectToStringRenderer;

/**
 * A list element that accepts lambda expressions to display elements and to 
 * react to selection changes.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 * @author Silvestrim, Filipe
 *
 * @param <TElement>
 * 		The type of the elements in the list.
 */
public class JLambdaList<TElement> extends JPanel {

	/** Generated serial version UID */
	private static final long serialVersionUID = 4641512314766501390L;
	
	private final JList<TElement> list;
	
	public JLambdaList(	String caption, 
            			Function<TElement, String> labelSelector, 
            			Consumer<TElement> activatedHandler) {
		JLabel captionLabel = new JLabel(caption);
	
		list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setCellRenderer(
				new ObjectToStringRenderer<TElement>(labelSelector));
		list.addListSelectionListener(
				new LambdaListSelectionListener<TElement>(activatedHandler));
		
		setLayout(new BorderLayout(5, 5));
		add(captionLabel, BorderLayout.NORTH);
		add(list, BorderLayout.CENTER);
	}
	
	@SuppressWarnings("unchecked")
	public void setData(List<TElement> data) {
		list.setListData((TElement[]) data.toArray());
	}

	public void selectElement(TElement element) {
		if (element == null)
			return;
		for (int i = 0; i < list.getModel().getSize(); i++) {
			TElement e = list.getModel().getElementAt(i);
			if (element.equals(e)) {
				list.setSelectedIndex(i);
				break;
			}
		}
//		list.setSelectedValue(element, true);
	}
}
