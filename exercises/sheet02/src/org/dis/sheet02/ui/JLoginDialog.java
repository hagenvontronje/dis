package org.dis.sheet02.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.dis.sheet02.services.LoginService;
import org.dis.sheet02.ui.base.EmTextField;

public class JLoginDialog extends JFrame {
	
	/** Generated version UID */
	private static final long serialVersionUID = -5658706345171351318L;
	
	private JTextField editUsername;
	private JTextField editPassword;
	
	private JButton buttonLogin;
	
	private boolean wasSuccessful = false;
	
	public JLoginDialog() {
		initializeComponents();
		connectEvents();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void connectEvents() {
		setTitle("Login");
		LoginEventHanlder loginHander = new LoginEventHanlder(this);
		buttonLogin.addActionListener(loginHander);
		editPassword.addKeyListener(loginHander);
	}

	private void initializeComponents() {
		Insets border = new Insets(5, 5, 5, 5);
		setLayout(new GridBagLayout());
		int padx = 2;
		int pady = 3;
		add(new JLabel("Username"), 
				new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editUsername = new EmTextField(), 
				new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));
		add(new JLabel("Password"), 
				new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editPassword = new EmTextField(), 
				new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));
		add(buttonLogin = new JButton("Login"), 
				new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, border, padx, pady));

//		setMinimumSize(new Dimension(200, 0));
//		setPreferredSize(new Dimension(200, 100));
		setResizable(false);
		pack();
		
		UIUtil.centerOnScreen(this);
	}
	
	public boolean wasSuccessful() {
		return wasSuccessful;
	}

	private class LoginEventHanlder implements ActionListener, KeyListener {
		private final JLoginDialog parent;
		private final LoginService loginService;
		
		public LoginEventHanlder(JLoginDialog parent) {
			this.parent = parent;
			this.loginService = new LoginService();
		}
		
		private void login() {
			String username = parent.editUsername.getText();
			String password = parent.editPassword.getText();
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
				parent.wasSuccessful = true;
				parent.setVisible(false);
				parent.dispose();
				JMainWindow mainWindow = new JMainWindow();
				mainWindow.loadData();
				EventQueue.invokeLater(() -> mainWindow.setVisible(true));
			}
			else {
				JOptionPane.showMessageDialog(parent, message, "Login", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			login();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				login();
		}
	}
}
