package org.dis.sheet02.ui;

import java.util.ArrayList;
import java.util.List;

import org.dis.sheet02.dal.ContextBuilder;
import org.dis.sheet02.dal.RealEstateContext;
import org.dis.sheet02.dal.dbcontext.EntitySet;
import org.dis.sheet02.entities.Contract;
import org.dis.sheet02.entities.PurchaseContract;
import org.dis.sheet02.entities.TenancyContract;
import org.dis.sheet02.ui.base.LambdaButton;
import org.dis.sheet02.ui.base.SingleColumnList;
import org.gnome.gtk.Align;
import org.gnome.gtk.Button;
import org.gnome.gtk.Frame;
import org.gnome.gtk.HBox;
import org.gnome.gtk.HButtonBox;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Widget;

public class ContractsView extends HBox {
	private SingleColumnList<Contract> contractsList;

	private RealEstateContext context;
	private EntitySet<TenancyContract> tenancies;
	private EntitySet<PurchaseContract> purchases;
	
	
	private Frame detailsFrame;
	private TenancyView tenancyView;
	private PurchaseView purchaseView;
	
	public ContractsView() {
		super(false, 10);
		setBorderWidth(10);
		initializeComponents();
		displayContract(new TenancyContract());
		loadContract();
	}
	
	private void ensureContextIsCreated() {
		if (context == null) {
			context = ContextBuilder.build();
			tenancies = context.getTenancyContracts();
			purchases = context.getPurchaseContracts();
		}
	}
	
	public void loadContract() {
		try {
			ensureContextIsCreated();
			List<Contract> allContracts = new ArrayList<>();
			allContracts.addAll(tenancies.getAll());
			allContracts.addAll(purchases.getAll());
			contractsList.setData(allContracts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeComponents() {
		tenancyView = new TenancyView(() -> loadContract());
		purchaseView = new PurchaseView(() -> loadContract());
		
		contractsList = new SingleColumnList<>(
				"Contracts", 
				(e) -> e.getDisplayName(),
				(e) -> displayContract(e));
		
		
		Button newTenancy = new LambdaButton("New Tenancy", 
				() -> displayContract(new TenancyContract()));
		newTenancy.setSizeRequest(120, 0);
		
		Button newPurchase = new LambdaButton("New Purchase", 
				() -> displayContract(new PurchaseContract()));
		newPurchase.setSizeRequest(120, 0);
		
		HButtonBox newEstateButtons = new HButtonBox();
		newEstateButtons.setAlignVertical(Align.END);
		newEstateButtons.add(newTenancy);
		newEstateButtons.add(newPurchase);
		
		VBox estatesListVbox = new VBox(false, 10);
		estatesListVbox.add(contractsList);
		estatesListVbox.add(newEstateButtons);
		
		detailsFrame = new Frame("Estate");
		detailsFrame.setLabelAlign(0.5f, 0.5f);
		detailsFrame.add(tenancyView);
		

		add(estatesListVbox);
		add(detailsFrame);

	}

	private void displayContract(Contract estate) {
		if (estate instanceof TenancyContract) {
			tenancyView.setEntity((TenancyContract)estate);
			setView(tenancyView);
			detailsFrame.setLabel("Tenancy Contract");
		}
		else if (estate instanceof PurchaseContract) {
			purchaseView.setEntity((PurchaseContract)estate);
			setView(purchaseView);
			detailsFrame.setLabel("Purchase Contract");
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
