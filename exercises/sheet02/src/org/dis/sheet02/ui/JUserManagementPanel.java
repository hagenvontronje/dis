package org.dis.sheet02.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.dal.dbcontext.DependentRecordsException;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.ui.base.DataBound;
import org.dis.sheet02.ui.base.EmPasswordField;
import org.dis.sheet02.ui.base.EmTextField;
import org.dis.sheet02.ui.base.JLambdaButton;
import org.dis.sheet02.ui.base.JLambdaList;

/**
 * A {@link JPanel} for managing users.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 * @author Silvestrim, Filipe
 *
 */
public class JUserManagementPanel extends JPanel implements DataBound {

	/** Generated version UID */
	private static final long serialVersionUID = 7289855793877348805L;

	private JLambdaList<EstateAgent> list;
	private JTextField editName;
	private JTextField editAddress;
	private JTextField editLogin;
	private JTextField editPassword;
	private JTextField editPasswordRepeat;
	
	private RealEstateContext context;
	private EntitySet<EstateAgent> agents;

	private JButton buttonDelete;
	private JButton buttonSave;
	private JButton buttonNew;

	private EstateAgent currentUser;
	
	public JUserManagementPanel() {
		initializeComponents();
		setUser(new EstateAgent());
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = new RealEstateContext();
			agents = context.getAgents();
		}
	}

	private void initializeComponents() {
		Insets border = new Insets(5, 5, 5, 5);
		setLayout(new GridBagLayout());
		
		list = new JLambdaList<>(	"Users", 
                    				e -> e.getName(),
                    				e -> setUser(e));

		buttonSave = new JLambdaButton("Save", () -> saveCurrentUser());
		buttonNew = new JLambdaButton("New", () -> newUser());
		buttonDelete = new JLambdaButton("Delete", () -> deleteCurrentUser());
		
		int padx = 0;
		int pady = 0;
		
		add(new JScrollPane(list),
				new GridBagConstraints(0, 0, 1, 8, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, border, 0, 0));

		add(new JLabel("Username"), 
				new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editName = new EmTextField(), 
				new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));

		add(new JLabel("Address"), 
				new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editAddress = new EmTextField(), 
				new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));

		add(new JLabel("Login"), 
				new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editLogin = new EmTextField(), 
				new GridBagConstraints(2, 3, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));

		add(new JLabel("Password"), 
				new GridBagConstraints(1, 4, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editPassword = new EmPasswordField(), 
				new GridBagConstraints(2, 4, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));

		add(new JLabel("Password (repeat)"), 
				new GridBagConstraints(1, 5, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, border, padx, pady));
		add(editPasswordRepeat = new EmPasswordField(), 
				new GridBagConstraints(2, 5, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, border, pady, padx));

		add(new JPanel(), 
				new GridBagConstraints(1, 6, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, border, padx, pady));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(buttonNew);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(buttonSave);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(buttonDelete);
		buttonPanel.add(Box.createHorizontalGlue());
		
		add(buttonPanel, 
				new GridBagConstraints(1, 7, 2, 1, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, border, padx, pady));
	}
	
	private void newUser() {
		setUser(new EstateAgent());
	}

	private void setUser(EstateAgent user) {
		this.currentUser = user;
		editName.setText(Util.NullToValue(user.getName()));
		editAddress.setText(Util.NullToValue(user.getAddress()));
		editLogin.setText(Util.NullToValue(user.getLogin()));
		editPassword.setText(Util.NullToValue(user.getPassword()));
		editPasswordRepeat.setText(Util.NullToValue(user.getPassword()));
	}

	private void deleteCurrentUser() {
		try {
			ensureContextIsCreated();
			if (currentUser.getId() > 0)
				agents.delete(currentUser);
			loadData();
			newUser();
		}
		catch (DependentRecordsException e) {
			JOptionPane.showMessageDialog(
					this, 
					"User cannot be deleted, because there are still estates "
					+ "associated with it!", 
					"Delete", 
					JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkInput() {
		boolean isValid = true;
		if (!editPassword.getText().equals(editPasswordRepeat.getText())) {
			isValid = false;
			JOptionPane.showMessageDialog(	this, 
                        					"Passwords don't match!", 
                        					"Save", 
                        					JOptionPane.ERROR_MESSAGE);
		}
		return isValid;
	}
	
	private void copyInputToObject() {
		currentUser.setName(editName.getText());
		currentUser.setAddress(editAddress.getText());
		currentUser.setLogin(editLogin.getText());
		currentUser.setPassword(editPassword.getText());
	}

	private void saveCurrentUser() {
		try {
			copyInputToObject();
			if (checkInput()) {
    			ensureContextIsCreated();
    			agents.save(currentUser);
    			loadData();
			}
		} catch (Exception e) {
			
		}
	}

	@Override
	public void loadData() {
		ensureContextIsCreated();
		try {
			ensureContextIsCreated();
			List<EstateAgent> allAgents = agents.getAll();
			list.setData(allAgents);
			list.selectElement(currentUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
