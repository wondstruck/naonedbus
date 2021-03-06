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
package net.naonedbus.activity.impl;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.naonedbus.NBApplication;
import net.naonedbus.R;
import net.naonedbus.activity.MenuDrawerActivity;
import net.naonedbus.manager.impl.HoraireManager;
import net.naonedbus.utils.CalendarUtils;
import net.naonedbus.widget.item.impl.DrawerMenuItem;
import net.naonedbus.widget.item.impl.MainMenuItem;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;

@SuppressWarnings("deprecation")
public class OldSettingsActivity extends SherlockPreferenceActivity {

	private ListPreference mNavigationHome;
	private ListPreference mCalendrierDefaut;
	private Preference mClearCachePlan;
	private Preference mClearCacheHoraires;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		mNavigationHome = (ListPreference) getPreferenceScreen().findPreference(NBApplication.PREF_NAVIGATION_HOME);
		mCalendrierDefaut = (ListPreference) getPreferenceScreen().findPreference(NBApplication.PREF_CALENDRIER_DEFAUT);
		mClearCachePlan = getPreferenceScreen().findPreference("plan.cache.clear");
		mClearCacheHoraires = getPreferenceScreen().findPreference("horaires.cache.clear");

		initNavigationHome(preferences);
		initCalendar(preferences);
		initClearCache(preferences);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isValidFragment(final String fragmentName) {
		return true;
	}

	private void initNavigationHome(final SharedPreferences preferences) {
		final List<DrawerMenuItem> items = MenuDrawerActivity.getMainMenuItems();

		final List<String> entriesName = new ArrayList<String>();
		final List<String> entriesId = new ArrayList<String>();

		for (int i = 0; i < items.size(); i++) {
			final DrawerMenuItem item = items.get(i);
			if (item instanceof MainMenuItem) {
				entriesName.add(getString(item.getTitle()));
				entriesId.add(String.valueOf(i));
			}
		}

		final int section = Integer.parseInt(preferences.getString(NBApplication.PREF_NAVIGATION_HOME, "0"));
		final DrawerMenuItem item = items.get(section);
		mNavigationHome.setSummary(getString(item.getTitle()));

		mNavigationHome.setEntries(entriesName.toArray(new String[entriesName.size()]));
		mNavigationHome.setEntryValues(entriesId.toArray(new String[entriesId.size()]));
		mNavigationHome.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue) {
				final DrawerMenuItem item = items.get(Integer.parseInt((String) newValue));
				mNavigationHome.setSummary(getString(item.getTitle()));
				return true;
			}
		});
	}

	/**
	 * Initier la liste des calendriers
	 * 
	 * @param preferences
	 */
	private void initCalendar(final SharedPreferences preferences) {
		mCalendrierDefaut.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue) {
				setCalendarSummary((String) newValue);
				return true;
			}
		});
		setCalendarSummary(preferences);
		fillCalendars(mCalendrierDefaut);
	}

	/**
	 * Initier le vidage du cache
	 * 
	 * @param preferences
	 */
	private void initClearCache(final SharedPreferences preferences) {
		mClearCachePlan.setSummary(getString(R.string.pref_cache_size, readableFileSize(getCacheSize())));

		mClearCachePlan.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(final Preference preference) {
				try {
					clearCache();
					mClearCachePlan.setSummary(getString(R.string.pref_cache_size, readableFileSize(getCacheSize())));
				} catch (final IOException e) {
					BugSenseHandler.sendExceptionMessage("Erreur lors de la suppression du cache des plans", null, e);
				}

				return false;
			}
		});

		mClearCacheHoraires.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(final Preference preference) {
				clearCacheHoraires();
				Toast.makeText(OldSettingsActivity.this, R.string.msg_cache_horaire_clear, Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}

	/**
	 * Vider le cache des plans
	 * 
	 * @throws IOException
	 */
	private void clearCache() throws IOException {
		FileUtils.deleteQuietly(getCacheDir());
		clearWebviewCache();
	}

	/**
	 * Vider le cache horaires
	 */
	private void clearCacheHoraires() {
		final HoraireManager horaireManager = HoraireManager.getInstance();
		horaireManager.clearSchedules(getContentResolver());
		clearWebviewCache();
	}

	/**
	 * Supprimer le cache webView
	 */
	private void clearWebviewCache() {
		final File directory = getFilesDir();

		final Collection<File> webviewFiles = FileUtils.listFiles(directory, webViewFilter, webViewFilter);
		for (final File file : webviewFiles) {
			file.delete();
		}
	}

	private static IOFileFilter webViewFilter = new IOFileFilter() {

		@Override
		public boolean accept(final File file) {
			return file.getName().startsWith("webview");
		}

		@Override
		public boolean accept(final File file, final String name) {
			return name.startsWith("webview");
		}

	};

	/**
	 * Lister les calendrier dans la ListPreference passée en paramètre
	 * 
	 * @param list
	 */
	private void fillCalendars(final ListPreference list) {
		CharSequence[] entriesName;
		CharSequence[] entriesId;
		final Map<Integer, String> calendars = CalendarUtils.getCalendars(getContentResolver());

		entriesName = new String[calendars.size()];
		entriesId = new String[calendars.size()];

		int i = 0;
		for (final Entry<Integer, String> cal : calendars.entrySet()) {
			entriesName[i] = cal.getValue();
			entriesId[i++] = String.valueOf(cal.getKey());
		}
		list.setEntries(entriesName);
		list.setEntryValues(entriesId);
	}

	/**
	 * Afficher le sous-titre du calendrier
	 * 
	 * @param preferences
	 */
	private void setCalendarSummary(final SharedPreferences preferences) {
		final String calendarId = preferences.getString(NBApplication.PREF_CALENDRIER_DEFAUT, null);
		setCalendarSummary(calendarId);
	}

	/**
	 * Afficher le sous-titre du calendrier
	 * 
	 * @param id
	 */
	private void setCalendarSummary(final String id) {
		if (id != null) {
			mCalendrierDefaut.setSummary(CalendarUtils.getCalendarName(getContentResolver(), id));
		} else {
			mCalendrierDefaut.setSummary(R.string.pref_calendar_summary);
		}
	}

	/**
	 * Calculer la taille du cache
	 * 
	 * @return La taille du cache en octets
	 */
	private long getCacheSize() {
		final File cache = getCacheDir();
		return FileUtils.sizeOfDirectory(cache);
	}

	/**
	 * Formatter la taille
	 * 
	 * @param size
	 * @return Taille compréhensible par les humains ordinaires
	 */
	private String readableFileSize(final long size) {
		if (size <= 0)
			return getString(R.string.msg_vide);
		final String[] units = new String[] { "o", "Ko", "Mo", "Go", "To" };
		final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

}
