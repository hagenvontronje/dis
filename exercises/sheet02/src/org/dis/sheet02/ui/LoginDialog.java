package org.dis.sheet02.ui;

import java.sql.SQLException;

import org.dis.sheet02.services.LoginService;
import org.gnome.gdk.Event;
import org.gnome.gdk.EventKey;
import org.gnome.gdk.Keyval;
import org.gnome.gtk.Align;
import org.gnome.gtk.Button;
import org.gnome.gtk.Button.Clicked;
import org.gnome.gtk.ButtonsType;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Gtk;
import org.gnome.gtk.Label;
import org.gnome.gtk.MessageDialog;
import org.gnome.gtk.MessageType;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Widget;
import org.gnome.gtk.Window;

public class LoginDialog extends Window {

	private Entry passwordEdit;
	private Entry usernameEdit;
	
	public LoginDialog() {
		InitializeComponents();

		connect(new Window.DeleteEvent() {
            public boolean onDeleteEvent(Widget source, Event event) {
                Gtk.mainQuit();
                return false;
            }
        });
	}

	private void InitializeComponents() {
		setTitle("Login");
		setSizeRequest(300, 0);
		setResizable(false);
		
		// Login handler:
		LoginEventHanlder loginHandler = new LoginEventHanlder(this);
		
		VBox vbox = new VBox(false, 10);
		vbox.setBorderWidth(10);

		// username entry:
		vbox.add(new Label("Username:"));
		usernameEdit = new Entry();
		vbox.add(usernameEdit);

		// password entry:
		vbox.add(new Label("Password:"));
		passwordEdit = new Entry();
		passwordEdit.setInvisibleChar('*');
		passwordEdit.setVisibility(false);
		passwordEdit.connect(loginHandler);
		vbox.add(passwordEdit);
		
		// login button:
		Button loginButton = new Button("Login");
		loginButton.setAlignHorizontal(Align.CENTER);
		loginButton.setBorderWidth(10);
		loginButton.connect((Clicked)loginHandler);
		vbox.add(loginButton);

		for (Widget widget : vbox.getChildren()) {
			if (widget.getClass() == Label.class)
				widget.setAlignHorizontal(Align.START);
		}
		this.add(vbox);
	}
	
	private class LoginEventHanlder implements Clicked, KeyReleaseEvent {
		private final LoginDialog parent;
		private final LoginService loginService;
		
		public LoginEventHanlder(LoginDialog parent) {
			this.parent = parent;
			this.loginService = new LoginService();
		}

		@Override
		public void onClicked(Button source) {
			source.setSensitive(false);
			login();
			source.setSensitive(true);
		}
		
		private void login() {
			String username = parent.usernameEdit.getText();
			String password = parent.passwordEdit.getText();
			String message = "";
			boolean success = false;
			try {
				success = loginService.login(username, password);
				if (!success)
					message = "Login failed!";
			} catch (SQLException e) {
				message = "Error trying to login:\n\n" + e.getMessage();
			}
			if (success) {
				parent.hide();
				parent.destroy();
				Window main = new MainWindow();
				main.showAll();
			}
			else {
				MessageDialog confirmation = new MessageDialog(
						parent, 
						true,
						MessageType.INFO, 
						ButtonsType.OK, 
						message);
				confirmation.run();
				confirmation.hide();
				confirmation.destroy();
			}
		}

		@Override
		public boolean onKeyReleaseEvent(Widget source, EventKey event) {
			if (event.getKeyval() == Keyval.Return){
				source.setSensitive(false);
				login();
				source.setSensitive(true);
				return true;
			}
			return false;
		}
	}
}
