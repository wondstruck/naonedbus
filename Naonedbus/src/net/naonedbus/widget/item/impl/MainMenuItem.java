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
package net.naonedbus.widget.item.impl;

import net.naonedbus.widget.item.SectionItem;

public class MainMenuItem implements SectionItem {

	private int mTitle;
	private Class<?> mIntentClass;
	private int mResIcon;
	private Object mSection;

	public MainMenuItem(final int title, final Class<?> intentClass, final int resIcon, final Integer section) {
		mTitle = title;
		mIntentClass = intentClass;
		mResIcon = resIcon;
		mSection = section;
	}

	@Override
	public Object getSection() {
		return mSection;
	}

	public int getTitle() {
		return mTitle;
	}

	public void setTitle(final int title) {
		mTitle = title;
	}

	public Class<?> getIntentClass() {
		return mIntentClass;
	}

	public void setIntentClass(final Class<?> intentClass) {
		mIntentClass = intentClass;
	}

	public int getResIcon() {
		return mResIcon;
	}

	public void setResIcon(final int resIcon) {
		mResIcon = resIcon;
	}

	public void setSection(final Object section) {
		mSection = section;
	}

}
