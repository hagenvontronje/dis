package org.dis.sheet02.ui;

import java.sql.SQLException;

import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.gnome.gtk.Align;
import org.gnome.gtk.CheckButton;
import org.gnome.gtk.SpinButton;
import org.gnome.gtk.VBox;

public class HouseView extends BaseEstateDetailsView<House> {

	private SpinButton entryFloors;
	private SpinButton entryPrice;
	private CheckButton entryHasGarden;
	
	public HouseView(Runnable saveDeleteCallback) {
		super(saveDeleteCallback, () -> new House());
	}

	@Override
	protected void saveInputToCurrentCustom(House entity) {
		entity.setFloors((int)entryFloors.getValue());
		entity.hasGarden(entryHasGarden.getActive());
		entity.setPrice(entryPrice.getValue());
	}

	@Override
	protected void addDetailsControls(VBox detailsbox) {
		detailsbox.add(new AlignedLabel("Floors", Align.START));
		detailsbox.add(entryFloors = new SpinButton(0, Double.MAX_VALUE, 1));
//		detailsbox.add(new AlignedLabel("Has Garden", Align.START));
		detailsbox.add(entryHasGarden = new CheckButton("Garden"));
		detailsbox.add(new AlignedLabel("Price", Align.START));
		detailsbox.add(entryPrice = new SpinButton(0, Double.MAX_VALUE, 0.01));
	}
	
	@Override
	protected void setEntityCustom(House house) {
		entryFloors.setValue(house.getFloors());
		entryHasGarden.setActive(house.hasGarden());
		entryPrice.setValue(house.getPrice());
	}

	@Override
	protected House saveEntity(House entity, RealEstateContext ctx) throws SQLException {
		return ctx.getHouses().save(entity);
	}

	@Override
	protected void deleteEntity(House entity, RealEstateContext ctx) throws SQLException {
		ctx.getHouses().delete(entity);
	}
}
