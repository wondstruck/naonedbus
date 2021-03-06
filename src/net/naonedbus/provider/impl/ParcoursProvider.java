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
package net.naonedbus.provider.impl;

import net.naonedbus.provider.ReadOnlyContentProvider;
import net.naonedbus.provider.table.ArretTable;
import net.naonedbus.provider.table.LigneTable;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author romain.guefveneu
 * 
 */
public class ParcoursProvider extends ReadOnlyContentProvider {

	public static final int PARCOURS = 100;
	public static final int PARCOURS_ID = 110;

	private static final String AUTHORITY = "net.naonedbus.provider.ParcoursProvider";
	private static final String BASE_PATH = "parcours";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(AUTHORITY, BASE_PATH, PARCOURS);
		URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", PARCOURS_ID);
	}

	private static final String SQL_SELECT = "SELECT a."
			+ ArretTable._ID
			+ ", a."
			+ ArretTable.CODE
			+ " AS codeArret, a."
			+ ArretTable.CODE_SENS
			+ " AS codeSens, l."
			+ LigneTable.CODE
			+ " AS codeLigne, l."
			+ LigneTable._ID
			+ " AS idLigne, l."
			+ LigneTable.LETTRE
			+ " as lettre"
			+ ", l."
			+ LigneTable.COULEUR_BACK
			+ " as "
			+ ParcoursTable.COULEUR_BACK
			+ ", l."
			+ LigneTable.COULEUR_FRONT
			+ " as "
			+ ParcoursTable.COULEUR_FRONT
			+ ", s.nomSens AS nomSens, s._id AS idSens FROM equipements st LEFT JOIN arrets a ON a.idStation = st._id LEFT JOIN sens s ON s.code = a.codeSens AND s.codeLigne = a.codeLigne LEFT JOIN lignes l ON l.code = a.codeLigne WHERE l.code IS NOT NULL AND st.idType = 0 AND st.normalizedNom = ? GROUP BY l._id, s._id ORDER BY l.type, CAST(a.codeLigne AS numeric)";

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		String normalizedNom = uri.getQueryParameter("normalizedNom");
		return getReadableDatabase().rawQuery(SQL_SELECT, new String[] { normalizedNom });
	}

	public class ParcoursTable implements BaseColumns {
		public static final String CODE_LIGNE = "codeLigne";
		public static final String CODE_SENS = "codeSens";
		public static final String CODE_ARRET = "codeArret";
		public static final String LETTRE = "lettre";
		public static final String COULEUR_BACK = "couleurBack";
		public static final String COULEUR_FRONT = "couleurFront";
		public static final String NOM_SENS = "nomSens";
		public static final String ID_LIGNE = "idLigne";
		public static final String ID_SENS = "idSens";

	}

}
