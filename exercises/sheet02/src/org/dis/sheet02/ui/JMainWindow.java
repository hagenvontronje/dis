package org.dis.sheet02.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.dis.sheet02.ui.base.DataBound;

public class JMainWindow extends JFrame implements DataBound {

	/** Generated version UID */
	private static final long serialVersionUID = -2681420727879883139L;
	private JUserManagementPanel userManagementPanel;
	

	public JMainWindow() {
		setTitle("Estate Management");
		initializeComponents();
		UIUtil.centerOnScreen(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void initializeComponents() {
		setLayout(new BorderLayout());
		JTabbedPane tabPane = new JTabbedPane();

		tabPane.addTab("Estates", new JPanel());
		tabPane.addTab("Contracts", new JPanel());
		userManagementPanel = new JUserManagementPanel();
		tabPane.addTab("Users", userManagementPanel);
		
		add(tabPane);
		pack();
		setMinimumSize(new Dimension(800, 600));
		setPreferredSize(new Dimension(800, 600));
	}

	@Override
	public void loadData() {
		userManagementPanel.loadData();
	}
}
