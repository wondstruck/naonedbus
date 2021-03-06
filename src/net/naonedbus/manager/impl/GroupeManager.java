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

import net.naonedbus.bean.Groupe;
import net.naonedbus.manager.SQLiteManager;
import net.naonedbus.provider.impl.FavoriGroupeProvider;
import net.naonedbus.provider.impl.GroupeProvider;
import net.naonedbus.provider.table.FavorisGroupesTable;
import net.naonedbus.provider.table.GroupeTable;
import net.naonedbus.utils.QueryUtils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class GroupeManager extends SQLiteManager<Groupe> {

	private static GroupeManager instance;

	public static synchronized GroupeManager getInstance() {
		if (instance == null) {
			instance = new GroupeManager();
		}
		return instance;
	}

	private GroupeManager() {
		super(GroupeProvider.CONTENT_URI);
	}

	/**
	 * Récupérer un groupe selon son nom.
	 * 
	 * @param contentResolver
	 * @param nom
	 * @return le groupe
	 */

	@Override
	public Groupe getSingle(final ContentResolver contentResolver, final String nom) {
		final Cursor c = getCursor(contentResolver, GroupeTable.NOM + "=?", new String[] { nom });
		return getFirstFromCursor(c);
	}

	@Override
	public Groupe getSingleFromCursor(final Cursor c) {
		final Groupe groupe = new Groupe();
		groupe.setId(c.getInt(c.getColumnIndex(GroupeTable._ID)));
		groupe.setNom(c.getString(c.getColumnIndex(GroupeTable.NOM)));
		groupe.setOrdre(c.getInt(c.getColumnIndex(GroupeTable.ORDRE)));
		groupe.setVisibility(c.getInt(c.getColumnIndex(GroupeTable.VISIBILITE)));
		return groupe;
	}

	public Cursor getCursor(final ContentResolver contentResolver, final List<Integer> idFavoris) {
		final Uri.Builder builder = FavoriGroupeProvider.CONTENT_URI.buildUpon();
		builder.path(FavoriGroupeProvider.LINK_BASE_PATH);
		builder.appendQueryParameter(FavoriGroupeProvider.QUERY_PARAMETER_IDS, QueryUtils.listToInStatement(idFavoris));

		return contentResolver.query(builder.build(), null, null, null, null);
	}

	public void delete(final ContentResolver contentResolver, final int idGroupe) {
		final Uri.Builder builder = GroupeProvider.CONTENT_URI.buildUpon();
		builder.appendPath(String.valueOf(idGroupe));

		contentResolver.delete(builder.build(), null, null);
	}

	public void update(final ContentResolver contentResolver, final Groupe groupe) {
		final ContentValues contentValues = getContentValues(groupe);

		contentResolver.update(GroupeProvider.CONTENT_URI, contentValues, GroupeTable._ID + "=?",
				new String[] { String.valueOf(groupe.getId()) });
	}

	public boolean isFavoriAssociated(final ContentResolver contentResolver, final int idGroupe, final int idFavori) {
		boolean result;

		final Cursor c = contentResolver.query(FavoriGroupeProvider.CONTENT_URI, null, FavorisGroupesTable.ID_GROUPE
				+ "=? AND " + FavorisGroupesTable.ID_FAVORI + "=?",
				new String[] { String.valueOf(idGroupe), String.valueOf(idFavori) }, null);
		result = c.getCount() > 0;
		c.close();

		return result;
	}

	public List<Groupe> getAll(final ContentResolver contentResolver, final int idFavori) {
		final Uri.Builder builder = FavoriGroupeProvider.CONTENT_URI.buildUpon();
		builder.path(FavoriGroupeProvider.FAVORI_ID_BASE_PATH);
		builder.appendQueryParameter(FavoriGroupeProvider.QUERY_PARAMETER_IDS, String.valueOf(idFavori));

		return getFromCursor(contentResolver.query(builder.build(), null, null, null, null));
	}

	public void addFavoriToGroup(final ContentResolver contentResolver, final int idGroupe, final int idFavori) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(FavorisGroupesTable.ID_GROUPE, String.valueOf(idGroupe));
		contentValues.put(FavorisGroupesTable.ID_FAVORI, String.valueOf(idFavori));

		contentResolver.insert(FavoriGroupeProvider.CONTENT_URI, contentValues);
	}

	public void removeFavoriFromGroup(final ContentResolver contentResolver, final int idGroupe, final int idFavori) {
		final Uri.Builder builder = FavoriGroupeProvider.CONTENT_URI.buildUpon();
		builder.appendPath(String.valueOf(idGroupe));
		builder.appendPath(String.valueOf(idFavori));
		contentResolver.delete(builder.build(), null, null);
	}

	public void move(final ContentResolver contentResolver, final Cursor cursor, final int from, final int to) {
		Groupe groupe;
		final int start = Math.min(from, to);
		final int stop = Math.max(from, to);
		int position = start;

		cursor.moveToPosition(start);
		while (cursor.getPosition() <= stop && !cursor.isAfterLast()) {
			groupe = getSingleFromCursor(cursor);
			groupe.setOrdre(position);

			update(contentResolver, groupe);

			cursor.moveToNext();
			position++;
		}

	}

	@Override
	protected ContentValues getContentValues(final Groupe item) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(GroupeTable.NOM, item.getNom());
		contentValues.put(GroupeTable.ORDRE, item.getOrdre());
		contentValues.put(GroupeTable.VISIBILITE, String.valueOf(item.getVisibility()));
		return contentValues;
	}

}
