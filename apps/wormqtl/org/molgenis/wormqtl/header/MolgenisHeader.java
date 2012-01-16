/* Date:        November 11, 2009
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package org.molgenis.wormqtl.header;

import org.molgenis.auth.DatabaseLogin;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.util.Entity;
import org.molgenis.util.Tuple;


/**
 * A simple plugin to create the header of the MOLGENIS application. This
 * includes the header logo as well as the top level menu items for
 * documentation, services etc (replaces the hardcoded header).
 * 
 * @author Morris Swertz
 */
public class MolgenisHeader extends PluginModel<Entity>
{
	private static final long serialVersionUID = 7628452789847569319L;

	public MolgenisHeader(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	@Override
	public String getViewName()
	{
		return "org_molgenis_xgap_other_panacea_header_MolgenisHeader";
	}

	@Override
	public String getViewTemplate()
	{
		return "org/molgenis/xgap/other/panacea/header/MolgenisHeader.ftl";
	}

	@Override
	public void handleRequest(Database db, Tuple request) throws Exception
	{
		if ("doLogout".equals(request.getAction())) {

			getLogin().logout(db);
		}
	}

	@Override
	public void reload(Database db)
	{
		setUserLogin();
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}
	
	private String userLogin;
	
	public String getUserLogin() {
		
		return userLogin;
	}
	
	public void setUserLogin() {
		if (this.getLogin().isAuthenticated()) {
			this.userLogin = "<a href='molgenis.do?__target=main&select=UserLogin'>" + "Logged in as: " + ((DatabaseLogin)this.getLogin()).getUserName() + "</a>";
			this.userLogin += " | ";
			this.userLogin += "<a href='molgenis.do?__target=MolgenisHeader&select=UserLogin&__action=doLogout'>" + "Logout " + "</a>";
		} else {
			this.userLogin = "<a href='molgenis.do?__target=main&select=UserLogin'>" + "Login" + "</a>";
		}	
	}
	
	@Override
	public String getCustomHtmlHeaders()
	{
		return "<link rel=\"stylesheet\" style=\"text/css\" href=\"clusterdemo/xqtlpanaceacolors.css\">" + "\n" +
			   "<link rel=\"stylesheet\" style=\"text/css\" href=\"clusterdemo/main_override.css\">" ;
	}
}
