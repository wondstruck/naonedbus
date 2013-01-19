package net.naonedbus.fragment.impl;

import java.util.List;

import net.naonedbus.R;
import net.naonedbus.activity.impl.CommentaireDetailActivity;
import net.naonedbus.bean.Commentaire;
import net.naonedbus.bean.async.AsyncResult;
import net.naonedbus.formatter.CommentaireFomatter;
import net.naonedbus.fragment.CustomListFragment;
import net.naonedbus.intent.ParamIntent;
import net.naonedbus.manager.impl.CommentaireManager;
import net.naonedbus.widget.adapter.impl.CommentaireArrayAdapter;
import net.naonedbus.widget.indexer.impl.CommentaireIndexer;

import org.joda.time.DateTime;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class CommentairesFragment extends CustomListFragment {

	public CommentairesFragment() {
		super(R.string.title_fragment_en_direct, R.layout.fragment_listview_box);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		loadContent();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			refreshContent();
			break;
		}
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final Commentaire commentaire = (Commentaire) l.getItemAtPosition(position);
		final ParamIntent intent = new ParamIntent(getActivity(), CommentaireDetailActivity.class);
		intent.putExtraSerializable(CommentaireDetailActivity.Param.commentaire, commentaire);
		startActivity(intent);
	}

	@Override
	protected AsyncResult<ListAdapter> loadContent(final Context context) {
		final AsyncResult<ListAdapter> result = new AsyncResult<ListAdapter>();

		try {
			final CommentaireManager manager = CommentaireManager.getInstance();
			final List<Commentaire> infoTraficLignes = manager.getFromWeb(context, null, null, null, new DateTime(0));

			final CommentaireFomatter fomatter = new CommentaireFomatter(context);
			final CommentaireArrayAdapter adapter = new CommentaireArrayAdapter(context);
			if (infoTraficLignes != null) {
				fomatter.appendToAdapter(adapter, infoTraficLignes);
			}
			adapter.setIndexer(new CommentaireIndexer());
			result.setResult(adapter);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;
	}

}
