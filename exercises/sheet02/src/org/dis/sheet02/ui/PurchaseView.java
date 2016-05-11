package org.dis.sheet02.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.dis.sheet02.ui.base.EntityComboBox;
import org.gnome.gtk.Align;
import org.gnome.gtk.SpinButton;
import org.gnome.gtk.VBox;

public class PurchaseView extends BaseContractView<PurchaseContract> {
	
	public PurchaseView(Runnable saveDeleteCallback) {
		super(saveDeleteCallback, () -> new PurchaseContract());
	}
	private SpinButton entryInstallments;
	private SpinButton entryRate;
	private EntityComboBox<House> entryHouse;
	
	@Override
	protected void addDetailsControls(VBox detailsbox, RealEstateContext ctx) {
		entryHouse = new EntityComboBox<>(
				(a) -> a.getDisplayName(), 
				(a) -> entity.setHouse(a));
		try {
			entryHouse.setData(ctx.getHouses().getAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		detailsbox.add(new AlignedLabel("House", Align.START));
		detailsbox.add(entryHouse);
		detailsbox.add(new AlignedLabel("Number of Installments", Align.START));
		detailsbox.add(entryInstallments = new SpinButton(0, Integer.MAX_VALUE, 1));
		detailsbox.add(new AlignedLabel("Interest Rate", Align.START));
		detailsbox.add(entryRate = new SpinButton(0, Double.MAX_VALUE, 0.01));
	}

	@Override
	protected void deleteEntity(PurchaseContract entity, RealEstateContext ctx)
			throws SQLException {
		ctx.getPurchaseContracts().delete(entity);
	}

	@Override
	protected PurchaseContract saveEntity(PurchaseContract entity, RealEstateContext ctx)
			throws SQLException {
		return ctx.getPurchaseContracts().save(entity);
		
	}

	@Override
	protected void saveInputToCurrentCustom(PurchaseContract entity) {
		entity.setNumberOfInstallments((int)entryInstallments.getValue());
		entity.setInterestRate(entryRate.getValue());
	}

	@Override
	protected void setEntityCustom(PurchaseContract entity) {
		entryInstallments.setValue(Util.NullToValue(entity.getNumberOfInstallments()));
		entryRate.setValue(Util.NullToValue(entity.getInterestRate()));
		entryHouse.selectValue(entity.getHouse());
	}

	@Override
	protected List<String> checkInputCustom() {
		return new ArrayList<>();
	}

}
