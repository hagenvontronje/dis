package org.dis.sheet02.ui.base;

import org.gnome.gtk.Button;

public class LambdaButton extends Button {

	public LambdaButton(String label, Runnable action) {
		super(label);
		connect(new Clicked() {
			
			@Override
			public void onClicked(Button source) {
				try {
					action.run();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
