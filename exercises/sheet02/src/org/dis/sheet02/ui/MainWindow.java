package org.dis.sheet02.ui;

import org.dis.sheet02.services.LoginService;
import org.gnome.gdk.Event;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.Label;
import org.gnome.gtk.Notebook;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;

public class MainWindow extends Window {

	private UserManagementView userManagementView;
	private EstatesView estatesView;
	private ContractsView contractsView;

	public MainWindow() {
		InitializeComponents();
		connect(new Window.DeleteEvent() {
			public boolean onDeleteEvent(Widget source, Event event) {
				Gtk.mainQuit();
				return false;
			}
		});

		userManagementView.loadAgents();
	}

	private void InitializeComponents() {
		setTitle(String.format(	"Estate Management (User: %s)", 
								LoginService.User.getName()));
		setSizeRequest(800, 600);

		userManagementView = new UserManagementView();
		estatesView = new EstatesView();
		contractsView = new ContractsView();

		Notebook top = new Notebook();
		top.appendPage(estatesView, new Label("Estates"));
		// top.appendPage(new Label("Persons content"), new Label("Persons"));
		top.appendPage(contractsView, new Label("Contracts"));
		top.appendPage(userManagementView, new Label("Users"));

		add(top);

		boolean isRoot = LoginService.User.getId() == 0;
		estatesView.setSensitive(!isRoot);
		contractsView.setSensitive(!isRoot);
		userManagementView.setSensitive(isRoot);
	}
}
