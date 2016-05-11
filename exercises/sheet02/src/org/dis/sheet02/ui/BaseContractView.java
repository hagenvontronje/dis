package org.dis.sheet02.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.ContextBuilder;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.entities.Contract;
import org.dis.sheet02.entities.Person;
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

public abstract class BaseContractView<TEntity extends Contract> extends HBox {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private SpinButton entryContractNumber;
	private Entry entryDate;
	private Entry entryPlace;
	private Entry entryPersonFirstName;
	private Entry entryPersonLastName;
	private Entry entryPersonAddress;

	/** The currently displayed entity */
	protected TEntity entity;
	
	private Person person;

	private RealEstateContext context;
	private final Runnable saveDeleteCallback;
	private final Supplier<TEntity> newEntitySupplier;
	
	public BaseContractView(Runnable saveDeleteCallback,
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
			context = ContextBuilder.build();
		}
	}

	private void initializeComponents() {
		ensureContextIsCreated();
		VBox detailsbox = new VBox(false, 5);
		detailsbox.setAlignVertical(Align.START);
		detailsbox.add(new AlignedLabel("Contract Number", Align.START));
		detailsbox.add(entryContractNumber = new SpinButton(1, Integer.MAX_VALUE, 1));
		detailsbox.add(new AlignedLabel("Contract Date", Align.START));
		detailsbox.add(entryDate = new Entry());
		detailsbox.add(new AlignedLabel("Place", Align.START));
		detailsbox.add(entryPlace = new Entry());
		detailsbox.add(new AlignedLabel("Person Firstname", Align.START));
		detailsbox.add(entryPersonFirstName = new Entry());
		detailsbox.add(new AlignedLabel("Person Lastname", Align.START));
		detailsbox.add(entryPersonLastName = new Entry());
		detailsbox.add(new AlignedLabel("Person Address", Align.START));
		detailsbox.add(entryPersonAddress = new Entry());
		
		addDetailsControls(detailsbox, context);
		
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
	
	protected abstract void addDetailsControls(VBox detailsbox, RealEstateContext ctx);

	private void deleteCurrent() {
		try {
			if (entity != null && entity.getId() >= 0) {
				deleteEntity(entity, context);
				context.getPersons().delete(person);
			}
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
				person = context.getPersons().save(person);
				entity.setPersonId(person.getId());
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
	
	protected abstract TEntity saveEntity(TEntity entity, RealEstateContext ctx) 
			throws SQLException;
	
	private void saveInputToCurrent() {
		// TODO: Set apartment/house id!
		entity.setContractNumber((int)entryContractNumber.getValue());
		try {
			entity.setDate(DATE_FORMAT.parse(entryDate.getText()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		entity.setPlace(entryPlace.getText());
		person.setFirstName(entryPersonFirstName.getText());
		person.setLastName(entryPersonLastName.getText());
		person.setAddress(entryPersonAddress.getText());
		saveInputToCurrentCustom(entity);
	}
	
	protected abstract void saveInputToCurrentCustom(TEntity entity);

	private boolean checkInput() {
		final List<String> errors = new ArrayList<>();
		try {
			DATE_FORMAT.parse(entryDate.getText());
		} catch (Exception e) {
			errors.add("The date was not recognized (Format: YYYY-MM-DD)");
		}
		errors.addAll(checkInputCustom());
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
	
	protected abstract List<String> checkInputCustom();
	
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
		entryContractNumber.setValue(Util.NullToValue(entity.getContractNumber()));
		entryDate.setText(DATE_FORMAT.format(Util.NullToValue(entity.getDate())));
		entryPlace.setText(Util.NullToValue(entity.getPlace()));
		loadPerson(entity);
		setEntityCustom(entity);
	}

	private void loadPerson(TEntity entity) {
		try {
			ensureContextIsCreated();
			if (entity.getPersonId() == 0)
				person = new Person();
			else
				person = context.getPersons().get(entity.getPersonId());
			entryPersonFirstName.setText(Util.NullToValue(person.getFirstName()));
			entryPersonLastName.setText(Util.NullToValue(person.getLastName()));
			entryPersonAddress.setText(Util.NullToValue(person.getAddress()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void setEntityCustom(TEntity house);
	
}