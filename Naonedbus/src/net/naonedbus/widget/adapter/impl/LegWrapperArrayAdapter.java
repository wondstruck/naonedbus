package net.naonedbus.widget.adapter.impl;

import java.util.List;

import net.naonedbus.R;
import net.naonedbus.bean.LegWrapper;
import net.naonedbus.bean.Ligne;
import net.naonedbus.utils.ColorUtils;
import net.naonedbus.utils.FormatUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.ybo.opentripplanner.client.modele.Leg;
import fr.ybo.opentripplanner.client.modele.Place;

public class LegWrapperArrayAdapter extends ArrayAdapter<LegWrapper> {

	private final LayoutInflater mLayoutInflater;

	public LegWrapperArrayAdapter(final Context context, final List<LegWrapper> objects) {
		super(context, 0, objects);
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder;
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.list_item_leg, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.itemMetroPointFrom = view.findViewById(R.id.itemMetroPointFrom);
			viewHolder.itemMetroPointTo = view.findViewById(R.id.itemMetroPointTo);
			viewHolder.itemIcon = (ImageView) view.findViewById(R.id.itemIcon);
			viewHolder.itemSymbole = (TextView) view.findViewById(R.id.itemSymbole);
			viewHolder.fromDirection = (TextView) view.findViewById(R.id.fromDirection);
			viewHolder.totalTime = (TextView) view.findViewById(R.id.totalTime);
			viewHolder.fromTime = (TextView) view.findViewById(R.id.fromTime);
			viewHolder.fromPlace = (TextView) view.findViewById(R.id.fromPlace);
			viewHolder.toTime = (TextView) view.findViewById(R.id.toTime);
			viewHolder.toPlace = (TextView) view.findViewById(R.id.toPlace);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final LegWrapper legWrapper = getItem(position);
		final Leg leg = legWrapper.getLeg();
		final Ligne ligne = legWrapper.getLigne();

		if ("WALK".equals(leg.mode)) {
			viewHolder.itemIcon.setVisibility(View.VISIBLE);
			viewHolder.itemSymbole.setVisibility(View.INVISIBLE);

			viewHolder.fromDirection.setText("Marche à pied");

		} else if (ligne != null) {
			viewHolder.itemIcon.setVisibility(View.GONE);
			viewHolder.itemSymbole.setVisibility(View.VISIBLE);

			viewHolder.fromDirection.setText(FormatUtils.formatSens(leg.headsign));
			viewHolder.itemSymbole.setText(ligne.getLettre());
			viewHolder.itemSymbole.setTextColor(ligne.getCouleurTexte());
			viewHolder.itemSymbole.setBackgroundDrawable(ColorUtils.getRoundedGradiant(ligne.getCouleur()));
		}

		final Place from = leg.from;
		viewHolder.fromPlace.setText(from.name);
		viewHolder.fromTime.setText(legWrapper.getFromTime());

		final Place to = leg.to;
		viewHolder.toPlace.setText(to.name);
		viewHolder.toTime.setText(legWrapper.getToTime());

		if (position == 0) {
			viewHolder.itemMetroPointFrom.setBackgroundResource(R.drawable.ic_arret_first);
			viewHolder.itemMetroPointTo.setBackgroundResource(R.drawable.ic_arret_step);
		} else if (position == getCount() - 1) {
			viewHolder.itemMetroPointFrom.setBackgroundResource(R.drawable.ic_arret_step);
			viewHolder.itemMetroPointTo.setBackgroundResource(R.drawable.ic_arret_last);
		} else {
			viewHolder.itemMetroPointFrom.setBackgroundResource(R.drawable.ic_arret_step);
			viewHolder.itemMetroPointTo.setBackgroundResource(R.drawable.ic_arret_step);
		}

		return view;
	}

	private static class ViewHolder {
		View itemMetroPointFrom;
		View itemMetroPointTo;
		ImageView itemIcon;
		TextView itemSymbole;
		TextView totalTime;
		TextView fromDirection;
		TextView fromTime;
		TextView fromPlace;
		TextView toTime;
		TextView toPlace;
	}

}
