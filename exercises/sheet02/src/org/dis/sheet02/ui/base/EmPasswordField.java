package org.dis.sheet02.ui.base;

import java.awt.Dimension;

import javax.swing.JPasswordField;

import org.dis.sheet02.Constants;

public class EmPasswordField extends JPasswordField {

	public EmPasswordField() {
		setPreferredSize(new Dimension(	Constants.DEFAULT_FIELD_WIDTH, 
										Constants.DEFAULT_FIELD_HEIGHT));
	}
}
