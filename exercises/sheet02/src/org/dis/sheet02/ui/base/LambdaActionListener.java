package org.dis.sheet02.ui.base;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action listener that executes a {@link Runnable} as action.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 * @author Silvestrim, Filipe
 *
 */
public class LambdaActionListener implements ActionListener {

	private final Runnable action;
	private final boolean useEventQueue;
	
	
	public LambdaActionListener(Runnable action, boolean useEveneQueue) {
		this.action = action;
		this.useEventQueue = useEveneQueue;
	}
	
	public LambdaActionListener(Runnable action) {
		this(action, false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (useEventQueue)
			EventQueue.invokeLater(() -> tryExecute());
		else
			tryExecute();
	}
	
	private void tryExecute() {
		try {
			action.run();
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}

}
