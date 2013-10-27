package net.naonedbus.fragment.header;

import net.naonedbus.R;
import net.naonedbus.fragment.impl.BiclooBookmarksFragment;
import net.naonedbus.fragment.impl.BicloosFragment;
import net.naonedbus.manager.impl.BiclooBookmarkManager;
import android.content.Context;

public class BicloosFragmentHeader implements FragmentHeader {

	private final Class<?>[] mFragments = new Class<?>[] { BicloosFragment.class, BiclooBookmarksFragment.class };

	private final int[] mTitles = new int[] { R.string.stations, R.string.bookmarks };

	@Override
	public int[] getFragmentsTitles() {
		return mTitles;
	}

	@Override
	public Class<?>[] getFragmentsClasses() {
		return mFragments;
	}

	@Override
	public int getSelectedPosition(final Context context) {
		final BiclooBookmarkManager favoriManager = BiclooBookmarkManager.getInstance();
		final int count = favoriManager.getAll(context.getContentResolver()).size();
		return count > 0 ? 1 : 0;
	}

}