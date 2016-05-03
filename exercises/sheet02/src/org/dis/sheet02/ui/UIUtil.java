package org.dis.sheet02.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class UIUtil {

	public static void centerOnScreen(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(	(screenSize.width - frame.getSize().width) / 2,
							(screenSize.height - frame.getSize().height) / 2);
	}
}
