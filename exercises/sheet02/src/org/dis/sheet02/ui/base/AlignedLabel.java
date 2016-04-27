package org.dis.sheet02.ui.base;

import org.gnome.gtk.Align;
import org.gnome.gtk.Label;

public class AlignedLabel extends Label {
	public AlignedLabel(String label, Align alignment) {
		super(label);
		setAlignHorizontal(alignment);
		setAlignVertical(Align.CENTER);;
		setExpandHorizontal(true);
		setExpandVertical(false);
	}
}