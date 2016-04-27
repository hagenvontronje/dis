package org.dis.sheet02.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.dis.sheet02.Util;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.TenancyContract;
import org.dis.sheet02.ui.base.AlignedLabel;
import org.dis.sheet02.ui.base.EntityComboBox;
import org.gnome.gtk.Align;
import org.gnome.gtk.Entry;
import org.gnome.gtk.SpinButton;
import org.gnome.gtk.VBox;

public class TenancyView extends BaseContractView<TenancyContract> {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public TenancyView(Runnable saveDeleteCallback) {
		super(saveDeleteCallback, () -> new TenancyContract());
	}

	private Entry entryStartDate;
	private SpinButton entryDuration;
	private SpinButton entryAddCosts;
	private EntityComboBox<Appartment> entryApartment;
	
	@Override
	protected void addDetailsControls(VBox detailsbox, RealEstateContext ctx) {
		entryApartment = new EntityComboBox<>(
				(a) -> a.getDisplayName(), 
				(a) -> entity.setAppartmentId(a.getId()));
		try {
			entryApartment.setData(ctx.getAppartments().getAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		detailsbox.add(new AlignedLabel("Apartment", Align.START));
		detailsbox.add(entryApartment);
		detailsbox.add(new AlignedLabel("Start Date", Align.START));
		detailsbox.add(entryStartDate = new Entry());
		detailsbox.add(new AlignedLabel("Duration", Align.START));
		detailsbox.add(entryDuration = new SpinButton(0, Integer.MAX_VALUE, 1));
		detailsbox.add(new AlignedLabel("Additional Costs", Align.START));
		detailsbox.add(entryAddCosts = new SpinButton(0, Double.MAX_VALUE, 0.01));
		
		
	}

	@Override
	protected void deleteEntity(TenancyContract entity, RealEstateContext ctx)
			throws SQLException {
		ctx.getTenancyContracts().delete(entity);
	}

	@Override
	protected void saveEntity(TenancyContract entity, RealEstateContext ctx)
			throws SQLException {
		ctx.getTenancyContracts().save(entity);
		
	}

	@Override
	protected void saveInputToCurrentCustom(TenancyContract entity) {
		try {
			entity.setStartDate(DATE_FORMAT.parse(entryStartDate.getText()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		entity.setDuration((int)entryDuration.getValue());
		entity.setAdditionalCosts(entryAddCosts.getValue());
	}

	@Override
	protected void setEntityCustom(TenancyContract entity) {
		entryStartDate.setText(DATE_FORMAT.format(Util.NullToValue(entity.getStartDate())));
		entryDuration.setValue(Util.NullToValue(entity.getDuration()));
		entryAddCosts.setValue(Util.NullToValue(entity.getAdditionalCosts()));
		entryApartment.selectValue(Util.NullToValue(entity.getAppartmentId()));
	}

	@Override
	protected List<String> checkInputCustom() {
		final List<String> errors = new ArrayList<>();
		if (entity.getAppartmentId() == 0)
			errors.add("No apartment selected");
		try {
			DATE_FORMAT.parse(entryStartDate.getText());
		} catch (Exception e) {
			errors.add("The start date was not recognized (Format: YYYY-MM-DD)");
		}
		return errors;
	}

}
