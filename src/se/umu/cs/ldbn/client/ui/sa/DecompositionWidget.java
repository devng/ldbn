package se.umu.cs.ldbn.client.ui.sa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import se.umu.cs.ldbn.client.Common;
import se.umu.cs.ldbn.client.core.FD;
import se.umu.cs.ldbn.client.core.Relation;
import se.umu.cs.ldbn.client.ui.FDHolderPanel;
import se.umu.cs.ldbn.client.ui.FDWidget;
import se.umu.cs.ldbn.client.ui.HasAdditionalControlls;
import se.umu.cs.ldbn.client.ui.InfoButton;
import se.umu.cs.ldbn.client.ui.MouseAdapter;
import se.umu.cs.ldbn.client.ui.dialog.FDEditorDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class DecompositionWidget extends Composite 
	implements ClickListener, HasAdditionalControlls{

	private List<RelationWidget> relations;
	private Button addRelation;
	private Panel relationsPanel;
	private Image[] checkControlls;
	private int rCounter;
	
	public DecompositionWidget() {
		rCounter = 1;
		checkControlls = new Image[3];
		checkControlls = new Image[3];
		checkControlls[0] = new Image("img/check-box.png", 0, 0, 15, 15);
		Common.setCursorPointer(checkControlls[0]);
		checkControlls[0].setTitle("Select all");
		checkControlls[0].addClickListener(this);
		checkControlls[1] = new Image("img/check-box.png", 0, 15, 15, 15);
		Common.setCursorPointer(checkControlls[1]);
		checkControlls[1].setTitle("Select none");
		checkControlls[1].addClickListener(this);
		checkControlls[2] = new Image("img/bin.png", 0, 0, 15, 15);
		checkControlls[2].setTitle("Delete selected");
		checkControlls[2].addMouseListener(new MouseAdapter() {
			public void onMouseEnter(Widget sender) {
				checkControlls[2].setVisibleRect(15, 0, 15, 15);
			}

			public void onMouseLeave(Widget sender) {
				checkControlls[2].setVisibleRect(0, 0, 15, 15);
			}
		});
		checkControlls[2].addClickListener(this);
		Common.setCursorPointer(checkControlls[2]);
		
		VerticalPanel vp = new VerticalPanel();
		relations = new ArrayList<RelationWidget>();
		addRelation = new Button("Add new Relation");
		addRelation.setStyleName("min-cov-but");
		addRelation.addClickListener(this);
		Common.setCursorPointer(addRelation);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.add(addRelation);
		hp.add(new InfoButton("decompose"));
		vp.add(hp);
		relationsPanel = new HorizontalPanel();
		vp.add(relationsPanel);
		initWidget(vp);
	}

	public void onClick(Widget sender) {
		if (sender == addRelation) {
			RelationWidget r = new RelationWidget("R"+rCounter);
			rCounter++;
			relations.add(r);
			relationsPanel.add(r);
			FDEditorDialog.get().center(); //always center first
			FDEditorDialog.get().setCurrentFDHolderPanel(r.getFDHolderPanel());
			FDEditorDialog.get().setCurrentDomain(SolveAssignmentWidget.get().getDomainTable());
		} else if (sender == checkControlls[0]) {
			//select all
			for (RelationWidget rw : relations) {
				rw.setChecked(true);
			}
		} else if (sender == checkControlls[1]) {
			//select none
			for (RelationWidget rw : relations) {
				rw.setChecked(false);
			}
		} else if (sender == checkControlls[2]) {
			//delete selected
			for (Iterator<RelationWidget> i = relations.iterator(); i.hasNext();) {
				RelationWidget rw = i.next();
				if(rw.isChecked()) {
					i.remove();
					rw.removeFromParent();
					rw = null;
				}
			}
		}
	}
	
	public void addRelationList(Collection<Relation> list) {
		for (Relation r : list) {
			addRelation(r);
		}
	}
	
	public void addRelation(Relation r) {
		List<FD> fds = r.getFds();
		if(fds == null) {
			Log.warn("DecompositonWidget.addRelation : FD List is null");
			fds = new ArrayList<FD>(); //Place holder
		}
		
		RelationWidget rw = new RelationWidget("R"+rCounter);
		RelationAttributesWidget raw = rw.getRelationAttributesWidget();
		rCounter++;
		relations.add(rw);
		relationsPanel.add(rw);
		FDHolderPanel fdhp = rw.getFDHolderPanel();
		
		if(r.getAttrbutes() != null) {
			raw.setAttributes(r.getAttrbutes());
		} else {
			Log.warn("DecompositonWidget.addRelation : attributes are null, skip it");
		}
		
		if(r.getSuperKey() != null) {
			raw.setKey(r.getSuperKey());
		} else {
			Log.warn("DecompositonWidget.addRelation : keys are null, skip it");
		}

		for (FD fd : fds) {
			fdhp.addFDWidget(new FDWidget(true, fd));
		}
		

	}
	
	public List<RelationWidget> getRelationWidgets() {
		return relations;
	}
	
	public List<Relation> getRelations() {
		ArrayList<Relation> result = new ArrayList<Relation>(relations.size());
		for (RelationWidget rw : relations) {
			result.add(rw.getRelation());
		}
		return result;
	}
	
	public void clearData() {
		relations.clear();
		relationsPanel.clear();
		rCounter = 1;
	}
	
	public Widget[] getAdditionalControlls() {
		return checkControlls;
	}
}
