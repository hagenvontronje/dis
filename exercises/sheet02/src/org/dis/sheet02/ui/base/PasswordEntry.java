package org.dis.sheet02.ui.base;

import org.gnome.gtk.Entry;

public class PasswordEntry extends Entry {
	public PasswordEntry() {
		super();
		setVisibility(false);
		setInvisibleChar('*');
	}
}
