package org.dis.sheet02.ui;

import java.util.List;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.EstateAgent;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.dis.sheet02.ui.base.LambdaButton;
import org.dis.sheet02.ui.base.PasswordEntry;
import org.dis.sheet02.ui.base.SingleColumnList;
import org.gnome.gtk.Align;
import org.gnome.gtk.Button;
import org.gnome.gtk.ButtonsType;
import org.gnome.gtk.Entry;
import org.gnome.gtk.HBox;
import org.gnome.gtk.HButtonBox;
import org.gnome.gtk.MessageDialog;
import org.gnome.gtk.MessageType;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Window;

public class UserManagementView extends HBox {
	private Entry entryName;
	private Entry entryAddress;
	private Entry entryLogin;
	private Entry entryPassword;
	private Entry entryPasswordRepeat;
	private SingleColumnList<EstateAgent> listAgents;

	/** The currently displayed agent */
	private EstateAgent currentAgent;

	private RealEstateContext context;
	private EntitySet<EstateAgent> agents;
	
	public UserManagementView() {
		super(false, 10);
		setBorderWidth(10);
		initializeComponents();
		currentAgent = new EstateAgent();
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = new RealEstateContext();
			agents = context.getAgents();
		}
	}
	
	public void loadAgents() {
		try {
			ensureContextIsCreated();
			List<EstateAgent> allAgents = agents.getAll();
			listAgents.setData(allAgents);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeComponents() {
		
		listAgents = new SingleColumnList<>(
				"Users", 
				(e) -> e.getName(),
				(e) -> displayAgent(e));
		add(listAgents);
		VBox agentdetails = new VBox(false, 5);
		agentdetails.setAlignVertical(Align.START);
		agentdetails.add(new AlignedLabel("Name", Align.START));
		agentdetails.add(entryName = new Entry());
		agentdetails.add(new AlignedLabel("Address", Align.START));
		agentdetails.add(entryAddress = new Entry());
		agentdetails.add(new AlignedLabel("Login", Align.START));
		agentdetails.add(entryLogin = new Entry());
		agentdetails.add(new AlignedLabel("Password", Align.START));
		agentdetails.add(entryPassword = new PasswordEntry());
		agentdetails.add(new AlignedLabel("Password (repeat)", Align.START));
		agentdetails.add(entryPasswordRepeat = new PasswordEntry());
		
		HButtonBox agentActions = new HButtonBox();
		agentActions.setBorderWidth(20);
		agentActions.setAlignVertical(Align.END);
		
		Button newAgent = new LambdaButton("New", 
				() -> displayAgent(new EstateAgent()));
		newAgent.setSizeRequest(120, 0);
		agentActions.add(newAgent);
		
		Button saveAgent = new LambdaButton("Save", 
				() -> saveCurrentAgent());
		saveAgent.setSizeRequest(120, 0);
		agentActions.add(saveAgent);
		
		Button deleteAgent = new LambdaButton("Delete",
				() -> deleteCurrentAgent());
		deleteAgent.setSizeRequest(120, 0);
		agentActions.add(deleteAgent);
		
		VBox details = new VBox(false, 0);
		details.add(agentdetails);
		details.add(agentActions);
		
		add(details);
	}
	
	private void deleteCurrentAgent() {
		try {
			if (currentAgent != null && currentAgent.getId() != 0) {
				agents.delete(currentAgent);
				loadAgents();
			}
			displayAgent(new EstateAgent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveCurrentAgent() {
		try {
			saveInputToAgent();
			if (checkInput()) {
				ensureContextIsCreated();
				agents.save(currentAgent);
				loadAgents();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveInputToAgent() {
		currentAgent.setName(entryName.getText());
		currentAgent.setAddress(entryAddress.getText());
		currentAgent.setLogin(entryLogin.getText());
		currentAgent.setPassword(entryPassword.getText());
	}

	private boolean checkInput() {
		if (entryPassword.getText().equals(entryPasswordRepeat.getText()))
			return true;
		MessageDialog dialog = new MessageDialog(
				(Window)getToplevel(), 
				true,
				MessageType.INFO, 
				ButtonsType.OK, 
				"Passwords don't match!");
		dialog.run();
		dialog.hide();
		dialog.destroy();
		return false;
	}

	private void displayAgent(EstateAgent agent) {
		currentAgent = agent;
		entryName.setText(Util.NullToValue(agent.getName()));
		entryAddress.setText(Util.NullToValue(agent.getAddress()));
		entryLogin.setText(Util.NullToValue(agent.getLogin()));
		entryPassword.setText(Util.NullToValue(agent.getPassword()));
		entryPasswordRepeat.setText(Util.NullToValue(agent.getPassword()));
	}
}
