package org.molgenis.biobank;

import org.molgenis.auth.MolgenisUser;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.security.SimpleLogin;

import plugins.emptydb.emptyDatabase;
import app.DatabaseFactory;
import app.FillMetadata;

public class BbmriUpdateDatabase
{
	public static void main(String[] args) throws Exception
	{

		// new
		// Molgenis("apps/bbmri/org/molgenis/biobank/bbmri.molgenis.properties").updateDb(true);

		Database db = DatabaseFactory.create("apps/bbmri/org/molgenis/biobank/bbmri.molgenis.properties");

		// Login login = new DatabaseLogin(db, null, new TokenFactory());
		// login.login(db, "admin", "admin");
		// db.setLogin(login);

		new emptyDatabase(db, false);
		FillMetadata.fillMetadata(db, false, "SimpleUserLoginPlugin");

		try
		{
			// Only add "Margreet Brandsma" user if type of Login allows for
			// this
			if (!(db.getLogin() instanceof SimpleLogin))
			{
				MolgenisUser u = new MolgenisUser();
				u.setName("bbmri");
				u.setPassword("bbmri");
				u.setSuperuser(true);
				u.setFirstName("Margreet");
				u.setLastName("Brandsma");
				u.setEmail("m.brandsma@bbmri.nl");
				db.add(u);
			}
		}
		finally
		{
			db.close();
		}

		// BBmriFillPermission.fillPermission(db);

	}
}