package org.dis.sheet02.ui;

import java.sql.SQLException;

import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.entities.Apartment;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.gnome.gtk.Align;
import org.gnome.gtk.CheckButton;
import org.gnome.gtk.SpinButton;
import org.gnome.gtk.VBox;

public class ApartmentView extends BaseEstateDetailsView<Apartment> {

	private SpinButton entryFloor;
	private SpinButton entryRooms;
	private SpinButton entryRent;
	private CheckButton entryHasBalcony;
	private CheckButton entryHasKitchen;
	
	public ApartmentView(Runnable saveDeleteCallback) {
		super(saveDeleteCallback, () -> new Apartment());
		
	}

	@Override
	protected void addDetailsControls(VBox detailsbox) {

		detailsbox.add(new AlignedLabel("Floor", Align.START));
		detailsbox.add(entryFloor = new SpinButton(0, Double.MAX_VALUE, 1));
		detailsbox.add(new AlignedLabel("Rooms", Align.START));
		detailsbox.add(entryRooms = new SpinButton(0, Double.MAX_VALUE, 1));
//		detailsbox.add(new AlignedLabel("Has Garden", Align.START));
		detailsbox.add(entryHasBalcony = new CheckButton("Balcony"));
		detailsbox.add(entryHasKitchen = new CheckButton("Kitchen"));
		detailsbox.add(new AlignedLabel("Rent", Align.START));
		detailsbox.add(entryRent = new SpinButton(0, Double.MAX_VALUE, 0.01));
	}

	@Override
	protected void deleteEntity(Apartment entity, RealEstateContext ctx)
			throws SQLException {
		ctx.getAppartments().delete(entity);
	}

	@Override
	protected Apartment saveEntity(Apartment entity, RealEstateContext ctx)
			throws SQLException {
		return ctx.getAppartments().save(entity);
	}

	@Override
	protected void saveInputToCurrentCustom(Apartment entity) {
		entity.setFloor((int)entryFloor.getValue());
		entity.setRooms((int)entryRooms.getValue());
		entity.hasBalcony(entryHasBalcony.getActive());
		entity.hasBuiltInKitchen(entryHasKitchen.getActive());
		entity.setRent(entryRent.getValue());
	}

	@Override
	protected void setEntityCustom(Apartment house) {
		entryFloor.setValue(house.getFloor());
		entryRooms.setValue(house.getRooms());
		entryHasBalcony.setActive(house.hasBalcony());
		entryHasKitchen.setActive(house.hasBuiltInKitchen());
		entryRent.setValue(house.getRent());
	}
}
