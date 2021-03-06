package net.naonedbus.fragment.header;

import net.naonedbus.R;
import net.naonedbus.fragment.impl.BicloosFavorisFragment;
import net.naonedbus.fragment.impl.BicloosFragment;
import net.naonedbus.manager.impl.FavoriBiclooManager;
import android.content.Context;

public class BicloosFragmentHeader implements FragmentHeader {

	private final Class<?>[] mFragments = new Class<?>[] { BicloosFragment.class, BicloosFavorisFragment.class };

	private final int[] mTitles = new int[] { R.string.title_fragment_bicloos, R.string.title_fragment_favoris };

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
		final FavoriBiclooManager favoriManager = FavoriBiclooManager.getInstance();
		final int count = favoriManager.getAll(context.getContentResolver()).size();
		return count > 0 ? 1 : 0;
	}

}
