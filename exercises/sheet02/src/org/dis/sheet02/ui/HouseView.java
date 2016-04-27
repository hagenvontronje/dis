package org.dis.sheet02.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Estate;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.services.LoginService;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.dis.sheet02.ui.base.LambdaButton;
import org.gnome.gtk.Align;
import org.gnome.gtk.Button;
import org.gnome.gtk.ButtonsType;
import org.gnome.gtk.CheckButton;
import org.gnome.gtk.Entry;
import org.gnome.gtk.HBox;
import org.gnome.gtk.HButtonBox;
import org.gnome.gtk.MessageDialog;
import org.gnome.gtk.MessageType;
import org.gnome.gtk.SpinButton;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Window;

public class HouseView extends HBox {
	private Entry entryCity;
	private Entry entryPostalCode;
	private Entry entryStreet;
	private Entry entryStreetNumber;
	private SpinButton entryArea;
	private SpinButton entryFloors;
	private SpinButton entryPrice;
	private CheckButton entryHasGarden;

	/** The currently displayed entity */
	private House currentEntity;

	private RealEstateContext context;
	private EntitySet<House> houses;
	final Runnable saveDeleteCallback;
	
	public HouseView(Runnable saveDeleteCallback) {
		super(false, 10);
		this.saveDeleteCallback = saveDeleteCallback;
		setBorderWidth(10);
		initializeComponents();
		setEntity(new House());
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = new RealEstateContext();
			houses = context.getHouses();
		}
	}

	private void initializeComponents() {
		VBox detailsbox = new VBox(false, 5);
		detailsbox.setAlignVertical(Align.START);
		detailsbox.add(new AlignedLabel("City", Align.START));
		detailsbox.add(entryCity = new Entry());
		detailsbox.add(new AlignedLabel("Postal code", Align.START));
		detailsbox.add(entryPostalCode = new Entry());
		detailsbox.add(new AlignedLabel("Street", Align.START));
		detailsbox.add(entryStreet = new Entry());
		detailsbox.add(new AlignedLabel("Street number", Align.START));
		detailsbox.add(entryStreetNumber = new Entry());
		detailsbox.add(new AlignedLabel("Square area", Align.START));
		detailsbox.add(entryArea = new SpinButton(0, Double.MAX_VALUE, 0.01));
		
		addDetailsControls(detailsbox);
		
		HButtonBox actionsbox = new HButtonBox();
		actionsbox.setBorderWidth(20);
		actionsbox.setAlignVertical(Align.END);
		
//		Button newButton = new LambdaButton("New", 
//				() -> displayHouse(new House()));
//		newButton.setSizeRequest(120, 0);
//		agentActions.add(newButton);
		
		Button saveButton = new LambdaButton("Save", 
				() -> saveCurrent());
		saveButton.setSizeRequest(120, 0);
		actionsbox.add(saveButton);
		
		Button deleteButton = new LambdaButton("Delete",
				() -> deleteCurrent());
		deleteButton.setSizeRequest(120, 0);
		actionsbox.add(deleteButton);
		
		VBox details = new VBox(false, 0);
		details.add(detailsbox);
		details.add(actionsbox);
		
		add(details);
	}
	
	private void addDetailsControls(VBox detailsbox) {
		detailsbox.add(new AlignedLabel("Floors", Align.START));
		detailsbox.add(entryFloors = new SpinButton(0, Double.MAX_VALUE, 1));
//		detailsbox.add(new AlignedLabel("Has Garden", Align.START));
		detailsbox.add(entryHasGarden = new CheckButton("Garden"));
		detailsbox.add(new AlignedLabel("Price", Align.START));
		detailsbox.add(entryPrice = new SpinButton(0, Double.MAX_VALUE, 0.01));
	}

	private void deleteCurrent() {
		try {
			if (currentEntity != null && currentEntity.getId() != 0)
				houses.delete((House)currentEntity);
			setEntity(new House());
			onRecordModififed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveCurrent() {
		try {
			saveInputToCurrent();
			if (checkInput()) {
				ensureContextIsCreated();
				saveEntity(currentEntity);
				onRecordModififed();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onRecordModififed() {
		if (saveDeleteCallback != null) {
			try {
				saveDeleteCallback.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void saveEntity(Estate e) throws SQLException {
		houses.save((House) e);
	}
	
	private void saveInputToCurrent() {
		currentEntity.setManagerId(LoginService.User.getId());
		currentEntity.setCity(entryCity.getText());
		currentEntity.setPostalCode(entryPostalCode.getText());
		currentEntity.setStreet(entryStreet.getText());
		currentEntity.setStreetNumber(entryStreetNumber.getText());
		currentEntity.setManagerId(LoginService.User.getId());
		currentEntity.setSquareArea(entryArea.getValue());
		saveInputToCurrentCustom();
	}

	private void saveInputToCurrentCustom() {
		currentEntity.setFloors((int)entryFloors.getValue());
		currentEntity.hasGarden(entryHasGarden.getActive());
		currentEntity.setPrice(entryPrice.getValue());
	}

	private boolean checkInput() {
		final List<String> errors = new ArrayList<>();
//		try {
//			Double.parseDouble(entryArea.getText());
//		} catch (NumberFormatException e) {
//			errors.add("Square area is not a number.");
//		}
//		try {
//			Double.parseDouble(entryArea.getText());
//		} catch (NumberFormatException e) {
//			errors.add("Square area is not a number.");
//		}
		if (errors.isEmpty())
			return true;
		
		showMessage(MessageType.ERROR, ButtonsType.OK, String.format(
					"The input cannot be saved yet:\n\n-%s",
					String.join("\n-", errors.toArray(new String[0]))));
		return false;
	}
	
	private void showMessage(MessageType type, ButtonsType buttons, String msg) 
	{
		MessageDialog dialog = new MessageDialog(
                                		(Window)getToplevel(), 
                                		true,
                                		type, 
                                		buttons, 
                                		msg);
        dialog.run();
        dialog.hide();
        dialog.destroy();
		
	}

	public void setEntity(House entity) {
		currentEntity = entity;
		entryCity.setText(Util.NullToValue(entity.getCity()));
		entryPostalCode.setText(Util.NullToValue(entity.getPostalCode()));
		entryStreet.setText(Util.NullToValue(entity.getStreet()));
		entryStreetNumber.setText(Util.NullToValue(entity.getStreetNumber()));
		entryArea.setText(String.format(
				"%.2f",
				Util.NullToValue(entity.getSquareArea())));
		setEntityCustom(entity);
	}

	private void setEntityCustom(House house) {
		entryFloors.setValue(house.getFloors());
		entryHasGarden.setActive(house.hasGarden());
		entryPrice.setValue(house.getPrice());
	}
}