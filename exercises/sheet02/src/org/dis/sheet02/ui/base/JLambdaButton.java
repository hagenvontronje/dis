package org.dis.sheet02.ui.base;

import java.awt.Dimension;

import javax.swing.JButton;

public class JLambdaButton extends JButton {

	/** Generated serial version UID */
	private static final long serialVersionUID = -8226985910875141494L;

	public JLambdaButton(String caption, Runnable action) {
		super(caption);
		addActionListener(new LambdaActionListener(action));
		setPreferredSize(new Dimension(120, 36));
	}
}
