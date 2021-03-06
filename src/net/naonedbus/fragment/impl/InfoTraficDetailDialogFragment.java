package net.naonedbus.fragment.impl;

import net.naonedbus.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class InfoTraficDetailDialogFragment extends SherlockDialogFragment {

	public InfoTraficDetailDialogFragment() {
		setStyle(STYLE_NO_FRAME, R.style.ItineraryDialog);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialog_itinerary_detail, null);

		final Fragment fragment = new InfoTraficDetailFragment();
		fragment.setArguments(getArguments());

		getChildFragmentManager().beginTransaction()
				.replace(android.R.id.content, fragment, "InfoTraficDetailFragment").commit();

		return view;
	}
}
