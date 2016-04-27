package org.dis.sheet02.ui;

import java.util.ArrayList;
import java.util.List;

import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Appartment;
import org.dis.sheet02.entities.Estate;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.ui.base.LambdaButton;
import org.dis.sheet02.ui.base.SingleColumnList;
import org.gnome.gtk.Align;
import org.gnome.gtk.Button;
import org.gnome.gtk.Frame;
import org.gnome.gtk.HBox;
import org.gnome.gtk.HButtonBox;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Widget;

public class EstatesView extends HBox {
	private SingleColumnList<Estate> estatesList;

	private RealEstateContext context;
	private EntitySet<House> houses;
	private Frame detailsFrame;
	private EntitySet<Appartment> apartments;
	private HouseView houseView;
	private ApartmentView appartmentView;
	
	public EstatesView() {
		super(false, 10);
		setBorderWidth(10);
		initializeComponents();
		displayEstate(new House());
		loadEstates();
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = new RealEstateContext();
			houses = context.getHouses();
			apartments = context.getAppartments();
		}
	}
	
	public void loadEstates() {
		try {
			ensureContextIsCreated();
			List<Estate> allEstates = new ArrayList<>();
			allEstates.addAll(houses.getAll());
			allEstates.addAll(apartments.getAll());
			estatesList.setData(allEstates);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeComponents() {
		houseView = new HouseView(() -> loadEstates());
		appartmentView = new ApartmentView(() -> loadEstates());
		
		estatesList = new SingleColumnList<Estate>(
				"Estates", 
				(e) -> e.getDisplayName(),
				(e) -> displayEstate(e));
		
		
		Button newHouse = new LambdaButton("New House", 
				() -> displayEstate(new House()));
		newHouse.setSizeRequest(120, 0);
		
		Button newAppartment = new LambdaButton("New Apartment", 
				() -> displayEstate(new Appartment()));
		newAppartment.setSizeRequest(120, 0);
		
		HButtonBox newEstateButtons = new HButtonBox();
		newEstateButtons.setAlignVertical(Align.END);
		newEstateButtons.add(newHouse);
		newEstateButtons.add(newAppartment);
		
		VBox estatesListVbox = new VBox(false, 10);
		estatesListVbox.add(estatesList);
		estatesListVbox.add(newEstateButtons);
		
		detailsFrame = new Frame("Estate");
		detailsFrame.setLabelAlign(0.5f, 0.5f);
		detailsFrame.add(houseView);
		

		add(estatesListVbox);
		add(detailsFrame);

	}

	private void displayEstate(Estate estate) {
		if (estate instanceof House) {
			houseView.setEntity((House)estate);
			setView(houseView);
			detailsFrame.setLabel("House");
		}
		else if (estate instanceof Appartment) {
			appartmentView.setEntity((Appartment)estate);
			setView(appartmentView);
			detailsFrame.setLabel("Apartment");
		}
	}

	private void setView(Widget view) {
		Widget[] previousChildren = detailsFrame.getChildren();
		for (Widget child : previousChildren)
			detailsFrame.remove(child);
		
		detailsFrame.add(view);
		detailsFrame.showAll();
	}
}
