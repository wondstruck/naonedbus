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
package net.naonedbus.bean.horaire;

public class ScheduleToken {

	private final Long mDate;
	private final Integer mId;

	public ScheduleToken(final Long date, final Integer id) {
		mDate = date;
		mId = id;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof ScheduleToken) {
			final ScheduleToken element = (ScheduleToken) o;
			return (element.mDate.equals(mDate) && element.mId.equals(mId));
		} else {
			return super.equals(o);
		}
	}

	public Long getDate() {
		return mDate;
	}

	public Integer getArretId() {
		return mId;
	}

	@Override
	public int hashCode() {
		return mDate.hashCode() * 31 + mId.hashCode() * 31;
	}

	@Override
	public String toString() {
		return mId + " : " + mDate;
	}

}
