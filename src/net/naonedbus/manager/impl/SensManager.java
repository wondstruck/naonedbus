/**
 * Copyright (C) 2013 Romain Guefveneu.
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

import java.util.List;

import net.naonedbus.bean.Sens;
import net.naonedbus.manager.SQLiteManager;
import net.naonedbus.provider.impl.SensProvider;
import net.naonedbus.provider.table.SensTable;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class SensManager extends SQLiteManager<Sens> {

	private static SensManager instance = new SensManager();

	public static synchronized SensManager getInstance() {
		if (instance == null) {
			instance = new SensManager();
		}
		return instance;
	}

	private SensManager() {
		super(SensProvider.CONTENT_URI);
	}

	/**
	 * Récupérer une liste contenant les sens de la ligne sépcifiée
	 * 
	 * @param contentResolver
	 * @param codeLigne
	 */
	public List<Sens> getAll(final ContentResolver contentResolver, final String codeLigne) {
		final Cursor c = getCursor(contentResolver, codeLigne);
		return getFromCursor(c);
	}

	/**
	 * Récupérer un cursor contenant les sens de la ligne sépcifiée
	 * 
	 * @param contentResolver
	 * @param codeLigne
	 */
	public Cursor getCursor(final ContentResolver contentResolver, final String codeLigne) {
		final Uri.Builder builder = SensProvider.CONTENT_URI.buildUpon();
		builder.path(SensProvider.SENS_LIGNE_CODE_URI_PATH_QUERY);
		builder.appendQueryParameter("codeLigne", codeLigne);
		return contentResolver.query(builder.build(), null, null, null, null);
	}

	public Sens getSingle(final ContentResolver contentResolver, final String codeLigne, final String codeSens) {
		final Uri.Builder builder = SensProvider.CONTENT_URI.buildUpon();
		builder.path(SensProvider.SENS_CODE_LIGNE_CODE_URI_PATH_QUERY);
		builder.appendQueryParameter("codeLigne", codeLigne);
		builder.appendQueryParameter("codeSens", codeSens);
		final Cursor c = contentResolver.query(builder.build(), null, null, null, null);
		return getFirstFromCursor(c);
	}

	@Override
	public Sens getSingleFromCursor(final Cursor c) {
		final Sens item = new Sens();
		item._id = c.getInt(c.getColumnIndex(SensTable._ID));
		item.code = c.getString(c.getColumnIndex(SensTable.CODE));
		item.codeLigne = c.getString(c.getColumnIndex(SensTable.CODE_LIGNE));
		item.text = c.getString(c.getColumnIndex(SensTable.NOM));
		return item;
	}

	@Override
	protected ContentValues getContentValues(final Sens item) {
		return null;
	}

}
