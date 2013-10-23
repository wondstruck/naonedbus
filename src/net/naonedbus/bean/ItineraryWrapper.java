package net.naonedbus.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import fr.ybo.opentripplanner.client.modele.Itinerary;

public class ItineraryWrapper implements Serializable, Parcelable {

	private static final long serialVersionUID = 5232629057687343251L;

	private final Itinerary mItinerary;
	private String mTime;
	private String mDate;
	private String mWalkTime;
	private boolean mIsUnicorn;
	private boolean mIsError;
	private transient List<Route> mRoutes;

	public static ItineraryWrapper getEmptyItinerary() {
		return new ItineraryWrapper((Itinerary) null);
	}

	public static ItineraryWrapper getErrorItinerary() {
		final ItineraryWrapper wrapper = new ItineraryWrapper((Itinerary) null);
		wrapper.mIsError = true;

		return wrapper;
	}

	public static ItineraryWrapper getUnicornItinerary() {
		final ItineraryWrapper wrapper = new ItineraryWrapper((Itinerary) null);
		wrapper.mIsUnicorn = true;

		return wrapper;
	}

	public ItineraryWrapper(final Itinerary itinerary) {
		mItinerary = itinerary;
	}

	private ItineraryWrapper(final Parcel in) {
		mItinerary = (Itinerary) in.readSerializable();
		mTime = in.readString();
		mDate = in.readString();
		mWalkTime = in.readString();
		mIsUnicorn = in.readInt() == 1;
		mIsError = in.readInt() == 1;
		mRoutes = new ArrayList<Route>();
		in.readTypedList(mRoutes, Route.CREATOR);
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(final String time) {
		mTime = time;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(final String date) {
		mDate = date;
	}

	public String getWalkTime() {
		return mWalkTime;
	}

	public void setWalkTime(final String walkTime) {
		mWalkTime = walkTime;
	}

	public List<Route> getLignes() {
		return mRoutes;
	}

	public void setLignes(final List<Route> lignes) {
		mRoutes = lignes;
	}

	public Itinerary getItinerary() {
		return mItinerary;
	}

	public boolean isUnicorn() {
		return mIsUnicorn;
	}

	public boolean isError() {
		return mIsError;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeSerializable(mItinerary);
		dest.writeString(mTime);
		dest.writeString(mDate);
		dest.writeString(mWalkTime);
		dest.writeInt(mIsUnicorn ? 1 : 0);
		dest.writeInt(mIsError ? 1 : 0);
		dest.writeTypedList(mRoutes);
	}

	public static final Parcelable.Creator<ItineraryWrapper> CREATOR = new Parcelable.Creator<ItineraryWrapper>() {
		@Override
		public ItineraryWrapper createFromParcel(final Parcel in) {
			return new ItineraryWrapper(in);
		}

		@Override
		public ItineraryWrapper[] newArray(final int size) {
			return new ItineraryWrapper[size];
		}
	};
}
