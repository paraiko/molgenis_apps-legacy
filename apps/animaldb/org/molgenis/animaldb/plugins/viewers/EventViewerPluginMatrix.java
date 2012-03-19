/* Date:        February 10, 2010
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package org.molgenis.animaldb.plugins.viewers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.molgenis.animaldb.commonservice.CommonService;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.GenericPlugin;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.html.ActionInput;
import org.molgenis.framework.ui.html.Container;
import org.molgenis.framework.ui.html.DivPanel;
import org.molgenis.framework.ui.html.Paragraph;
import org.molgenis.framework.ui.html.Table;
import org.molgenis.matrix.component.MatrixViewer;
import org.molgenis.matrix.component.SliceablePhenoMatrix;
import org.molgenis.matrix.component.general.MatrixQueryRule;
import org.molgenis.pheno.Individual;
import org.molgenis.pheno.Measurement;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.ProtocolApplication;
import org.molgenis.util.Tuple;


public class EventViewerPluginMatrix extends GenericPlugin
{
	private static final long serialVersionUID = 8804579908239186037L;
	MatrixViewer targetMatrixViewer = null;
	static String TARGETMATRIX = "targetmatrix";
	private Container container = null;
	private DivPanel div = null;
	private CommonService cs = CommonService.getInstance();
	Table animalInfo = null;
	Paragraph animalInfoHeader = null;
	
	public EventViewerPluginMatrix(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	@Override
	public void handleRequest(Database db, Tuple request)
	{
		cs.setDatabase(db);
		if (targetMatrixViewer != null) {
			targetMatrixViewer.setDatabase(db);
		}
		
		String action = request.getAction();
		
		try {
			if (action.startsWith(targetMatrixViewer.getName())) {
	    		targetMatrixViewer.handleRequest(db, request);
			}
			
			if (action.equals("Select")) {
				List<?> rows = targetMatrixViewer.getSelection(db);
				int row = request.getInt(TARGETMATRIX + "_selected");
				int animalId = ((ObservationElement) rows.get(row)).getId();
				createInfoTable(db, animalId); // table gets added to screen on reload()
			}
		} catch(Exception e) {
			e.printStackTrace();
			this.getMessages().add(new ScreenMessage("Something went wrong while handling request: " + e.getMessage(), false));
		}
	}

	private void createInfoTable(Database db, int animalId) throws DatabaseException, ParseException {
		
		// Remove old items from screen div first:
		if (animalInfoHeader != null) div.remove(animalInfoHeader);
		if (animalInfo != null) div.remove(animalInfo);
		
		animalInfoHeader = new Paragraph("<h3>Timeline for animal " + cs.getObservationTargetLabel(animalId) + "</h3>");
		animalInfoHeader.setId("infoheader");
		
		List<ObservedValue> valList = db.query(ObservedValue.class).eq(ObservedValue.TARGET, 
				animalId).find();
		if (!valList.isEmpty()) {
			animalInfo = new Table("AnimalInfoTable");
			animalInfo.addColumn("Valid from...");
			animalInfo.addColumn("through...");
			animalInfo.addColumn("Protocol");
			animalInfo.addColumn("Measurement");
			animalInfo.addColumn("Value");
			int rowCount = 0;
			for (ObservedValue currentValue : valList) {
				animalInfo.addRow("");
				if (currentValue.getTime() != null) {
					animalInfo.setCell(0, rowCount, currentValue.getTime().toString());
				}
				if (currentValue.getEndtime() != null) {
					animalInfo.setCell(1, rowCount, currentValue.getEndtime().toString());
				}
				if (currentValue.getProtocolApplication_Id() != null) {
					int eventId = currentValue.getProtocolApplication_Id();
					ProtocolApplication currentEvent = db.find(ProtocolApplication.class, 
							new QueryRule(ProtocolApplication.ID, Operator.EQUALS, eventId)).get(0);
					animalInfo.setCell(2, rowCount, currentEvent.getProtocol_Name());
				}
				String featureName = currentValue.getFeature_Name();
				if (featureName == null) {
					animalInfo.setCell(3, rowCount, "");
					animalInfo.setCell(4, rowCount, "");
				} else {
					animalInfo.setCell(3, rowCount, featureName);
					// The actual value
					String currentValueContents = "<strong>";
					int featureId = currentValue.getFeature_Id();
					Measurement currentFeature = db.find(Measurement.class, 
							new QueryRule(Measurement.ID, Operator.EQUALS, featureId)).get(0);
					if (currentFeature.getDataType().equals("xref")) {
						currentValueContents += currentValue.getRelation_Name();
					} else {
						currentValueContents += currentValue.getValue();
					}
					currentValueContents += "</strong>";
					animalInfo.setCell(4, rowCount, currentValueContents);
				}
				rowCount++;
			}
		}
	}

	@Override
	public void reload(Database db)
	{
		cs.setDatabase(db);
		
		if (container == null) {
			container = new Container();
			div = new DivPanel();
			try {
				List<String> investigationNames = cs.getAllUserInvestigationNames(this.getLogin().getUserName());
				List<MatrixQueryRule> filterRules = new ArrayList<MatrixQueryRule>();
				filterRules.add(new MatrixQueryRule(MatrixQueryRule.Type.rowHeader, Individual.INVESTIGATION_NAME, 
						Operator.IN, investigationNames));
				targetMatrixViewer = new MatrixViewer(this, TARGETMATRIX, 
						new SliceablePhenoMatrix<Individual, Measurement>(Individual.class, Measurement.class), 
						true, 1, false, false, filterRules);
				targetMatrixViewer.setDatabase(db);
				targetMatrixViewer.setLabel("Choose animal:");
				div.add(targetMatrixViewer);
				
				ActionInput selectButton = new ActionInput("Select", "", "Select");
				selectButton.setId("select");
				div.add(selectButton);
				
				container.add(div);
			} catch(Exception e) {
				e.printStackTrace();
				this.getMessages().add(new ScreenMessage("Something went wrong while loading matrix: " + e.getMessage(), false));
			}
		} else {
			targetMatrixViewer.setDatabase(db);
			if (animalInfo != null) {
				div.add(animalInfoHeader);
				div.add(animalInfo);
			}
		}
    }
	
	public String render()
    {
    	return container.toHtml();
    }
	
}