/**
 *  Copyright (C) 2011 Romain Guefveneu
 *  
 *  This file is part of naonedbus.
 *  
 *  Naonedbus is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Naonedbus is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.naonedbus.manager.impl;

import net.naonedbus.provider.impl.UpdaterProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

/**
 * @author romain.guefveneu
 * 
 */
public class UpdaterManager {

	public static final String ACTION_ON_POST_EXECUTE = "UpdaterManager:OnPostExecute";

	private final Context mContext;

	public UpdaterManager(final Context context) {
		mContext = context;
	}

	/**
	 * Déclencher la mise à jour de la base de données si nécessaire.
	 * 
	 * @param contentResolver
	 */
	public void triggerUpdate(final ContentResolver contentResolver) {
		final Cursor c = contentResolver.query(UpdaterProvider.CONTENT_URI, null, null, null, null);
		if (c != null) {
			c.close();
		}
		mContext.sendBroadcast(new Intent(ACTION_ON_POST_EXECUTE));
	}

}
