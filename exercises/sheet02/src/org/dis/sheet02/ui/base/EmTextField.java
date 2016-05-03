package org.dis.sheet02.ui.base;

import java.awt.Dimension;

import javax.swing.JTextField;

import org.dis.sheet02.Constants;

/**
 * Preconfigured {@link JTextField}.
 *  
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 * @author Silvestrim, Filipe
 *
 */
public class EmTextField extends JTextField {

	public EmTextField() {
		setPreferredSize(new Dimension(	Constants.DEFAULT_FIELD_WIDTH, 
										Constants.DEFAULT_FIELD_HEIGHT));
	}
}
