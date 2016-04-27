package org.dis.sheet02.ui;

import java.util.ArrayList;
import java.util.List;

import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Estate;
import org.dis.sheet02.entities.House;
import org.dis.sheet02.ui.base.SingleColumnList;
import org.gnome.gtk.HBox;
import org.gnome.gtk.Widget;

public class EstatesView extends HBox {
	private SingleColumnList<Estate> estatesList;

	private RealEstateContext context;
	private EntitySet<House> houses;
	private HBox detailsBox;
//	private EntitySet<Appartment> appartments;
	private HouseView housesView;
	
	public EstatesView() {
		super(false, 10);
		setBorderWidth(10);
		initializeComponents();
		displayEstate(new House());
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = new RealEstateContext();
			houses = context.getHouses();
//			appartments = context.getAppartments();
		}
	}
	
	public void loadEstates() {
		try {
			ensureContextIsCreated();
			List<Estate> allEstates = new ArrayList<>();
			allEstates.addAll(houses.getAll());
//			allEstates.addAll(appartments.getAll());
			estatesList.setData(allEstates);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeComponents() {
		
		estatesList = new SingleColumnList<Estate>(
				"Name", 
				(e) -> e.getDisplayName(),
				(e) -> displayEstate(e));
		add(estatesList);
		
		detailsBox = new HBox(true, 0);
		housesView = new HouseView(() -> loadEstates());
		detailsBox.add(housesView);
		add(detailsBox);

//		Button newAgent = new LambdaButton("New", 
//				() -> displayEstate(new EstateAgent()));
//		newAgent.setSizeRequest(120, 0);
//		agentActions.add(newAgent);
	}

	private void displayEstate(Estate estate) {
		if (estate instanceof House) {
			Widget[] previousChildren = detailsBox.getChildren();
			for (Widget child : previousChildren)
				detailsBox.remove(child);
			
			housesView.setEntity((House)estate);
			detailsBox.add(housesView);
		}
	}
}
