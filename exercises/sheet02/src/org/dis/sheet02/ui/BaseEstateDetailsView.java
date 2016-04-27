package org.dis.sheet02.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.entities.Estate;
import org.dis.sheet02.services.LoginService;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.dis.sheet02.ui.base.LambdaButton;
import org.gnome.gtk.Align;
import org.gnome.gtk.Button;
import org.gnome.gtk.ButtonsType;
import org.gnome.gtk.Entry;
import org.gnome.gtk.HBox;
import org.gnome.gtk.HButtonBox;
import org.gnome.gtk.MessageDialog;
import org.gnome.gtk.MessageType;
import org.gnome.gtk.SpinButton;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Window;

public abstract class BaseEstateDetailsView<TEntity extends Estate> extends HBox {
	private Entry entryCity;
	private Entry entryPostalCode;
	private Entry entryStreet;
	private Entry entryStreetNumber;
	private SpinButton entryArea;

	/** The currently displayed entity */
	private TEntity entity;

	private RealEstateContext context;
	private final Runnable saveDeleteCallback;
	private final Supplier<TEntity> newEntitySupplier;
	
	public BaseEstateDetailsView(Runnable saveDeleteCallback,
			Supplier<TEntity> newEntitySupplier) {
		super(false, 10);
		this.saveDeleteCallback = saveDeleteCallback;
		this.newEntitySupplier = newEntitySupplier;
		setBorderWidth(10);
		initializeComponents();
		setEntity(newEntitySupplier.get());
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = new RealEstateContext();
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
	
	protected abstract void addDetailsControls(VBox detailsbox);

	private void deleteCurrent() {
		try {
			if (entity != null && entity.getId() >= 0)
				deleteEntity(entity, context);
			setEntity(newEntitySupplier.get());
			onRecordModififed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void deleteEntity(TEntity entity, RealEstateContext ctx) 
			throws SQLException;

	private void saveCurrent() {
		try {
			saveInputToCurrent();
			if (checkInput()) {
				ensureContextIsCreated();
				saveEntity(entity, context);
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
	
	protected abstract void saveEntity(TEntity entity, RealEstateContext ctx) 
			throws SQLException;
	
	private void saveInputToCurrent() {
		entity.setManagerId(LoginService.User.getId());
		entity.setCity(entryCity.getText());
		entity.setPostalCode(entryPostalCode.getText());
		entity.setStreet(entryStreet.getText());
		entity.setStreetNumber(entryStreetNumber.getText());
		entity.setManagerId(LoginService.User.getId());
		entity.setSquareArea(entryArea.getValue());
		saveInputToCurrentCustom(entity);
	}
	
	protected abstract void saveInputToCurrentCustom(TEntity entity);

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

	public void setEntity(TEntity entity) {
		this.entity = entity;
		entryCity.setText(Util.NullToValue(entity.getCity()));
		entryPostalCode.setText(Util.NullToValue(entity.getPostalCode()));
		entryStreet.setText(Util.NullToValue(entity.getStreet()));
		entryStreetNumber.setText(Util.NullToValue(entity.getStreetNumber()));
		entryArea.setText(String.format(
				"%.2f",
				Util.NullToValue(entity.getSquareArea())));
		setEntityCustom(entity);
	}
	
	protected abstract void setEntityCustom(TEntity house);
	
}