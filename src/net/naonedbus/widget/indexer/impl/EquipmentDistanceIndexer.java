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
package net.naonedbus.widget.indexer.impl;

import net.naonedbus.R;
import net.naonedbus.bean.Equipment;
import net.naonedbus.widget.indexer.ArraySectionIndexer;
import android.content.Context;

public class EquipmentDistanceIndexer extends ArraySectionIndexer<Equipment> {

	@Override
	protected String getSectionLabel(Context context, Equipment item) {
		return context.getString((Integer) item.getSection());
	}

	@Override
	protected void prepareSection(Equipment equipment) {
		if (equipment.getDistance() == null) {
			equipment.setSection(R.string.section_distance_none);
		} else if (equipment.getDistance() > 100000) {
			equipment.setSection(R.string.section_distance_100000);
		} else if (equipment.getDistance() > 50000) {
			equipment.setSection(R.string.section_distance_50000);
		} else if (equipment.getDistance() > 40000) {
			equipment.setSection(R.string.section_distance_40000);
		} else if (equipment.getDistance() > 30000) {
			equipment.setSection(R.string.section_distance_30000);
		} else if (equipment.getDistance() > 20000) {
			equipment.setSection(R.string.section_distance_20000);
		} else if (equipment.getDistance() > 10000) {
			equipment.setSection(R.string.section_distance_10000);
		} else if (equipment.getDistance() > 5000) {
			equipment.setSection(R.string.section_distance_5000);
		} else if (equipment.getDistance() > 1000) {
			equipment.setSection(R.string.section_distance_1000);
		} else if (equipment.getDistance() > 500) {
			equipment.setSection(R.string.section_distance_500);
		} else {
			equipment.setSection(R.string.section_distance_0);
		}
	}

}
