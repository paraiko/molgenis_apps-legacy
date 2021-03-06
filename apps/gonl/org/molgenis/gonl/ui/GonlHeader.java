/* Date:        March 23, 2011
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.3
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package org.molgenis.gonl.ui;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.util.Entity;

public class GonlHeader extends PluginModel<Entity>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -498412981769405874L;

	public GonlHeader(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.molgenis.framework.ui.SimpleScreenController#getCustomHtmlHeaders()
	 */
	@Override
	public String getCustomHtmlHeaders()
	{
		return "<link rel=\"stylesheet\" style=\"text/css\" href=\"bbmri/css/bbmri_colors.css\">" + "\n";
	}

	@Override
	public String getViewName()
	{
		return "plugins_header_GonlHeader";
	}

	@Override
	public String getViewTemplate()
	{
		return "org/molgenis/gonl/ui/GonlHeader.ftl";
	}

	@Override
	public void handleRequest(Database db, MolgenisRequest request)
	{
		// replace example below with yours
		// try
		// {
		// //start database transaction
		// db.beginTx();
		//
		// //get the "__action" parameter from the UI
		// String action = request.getAction();
		//
		// if( action.equals("do_add") )
		// {
		// Experiment e = new Experiment();
		// e.set(request);
		// db.add(e);
		// }
		//
		// //commit all database actions above
		// db.commitTx();
		//
		// } catch(Exception e)
		// {
		// db.rollbackTx();
		// //e.g. show a message in your form
		// }
	}

	@Override
	public void reload(Database db)
	{
		// try
		// {
		// Database db = this.getDatabase();
		// Query q = db.query(Experiment.class);
		// q.like("name", "test");
		// List<Experiment> recentExperiments = q.find();
		//
		// //do something
		// }
		// catch(Exception e)
		// {
		// //...
		// }
	}

	@Override
	public boolean isVisible()
	{
		// you can use this to hide this plugin, e.g. based on user rights.
		// e.g.
		// if(!this.getLogin().hasEditPermission(myEntity)) return false;
		return true;
	}
}
