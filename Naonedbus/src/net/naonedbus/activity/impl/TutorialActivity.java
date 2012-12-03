package net.naonedbus.activity.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.naonedbus.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

public class TutorialActivity extends Activity {

	private TutorialPagerAdapter mTutorialPagerAdapter;

	private Button mButtonOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		mButtonOk = (Button) findViewById(R.id.tutorialButton);
		mButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mTutorialPagerAdapter = new TutorialPagerAdapter(this);
		mTutorialPagerAdapter.addView(new TutorialView(R.layout.tutorial_view_welcome, R.string.tuto_0_title,
				R.string.tuto_0_summary, R.drawable.logo_large));
		mTutorialPagerAdapter.addView(new TutorialView(R.string.tuto_beta_title, R.string.tuto_beta_summary,
				R.drawable.ic_action_good));
		mTutorialPagerAdapter.addView(new TutorialView(R.string.tuto_1_title, R.string.tuto_1_summary,
				R.drawable.tuto_1));
		mTutorialPagerAdapter.addView(new TutorialView(R.string.tuto_2_title, R.string.tuto_2_summary,
				R.drawable.tuto_2));

		final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(mTutorialPagerAdapter);

		final CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.viewPagerIndicator);
		circlePageIndicator.setViewPager(viewPager);
	}

	private static class TutorialPagerAdapter extends PagerAdapter {

		private Context mContext;
		private List<TutorialView> mTutorialViews;
		private LayoutInflater mLayoutInflater;

		public TutorialPagerAdapter(Context context) {
			mContext = context;
			mTutorialViews = new ArrayList<TutorialActivity.TutorialView>();
			mLayoutInflater = LayoutInflater.from(context);
		}

		public void addView(TutorialView view) {
			mTutorialViews.add(view);
		}

		@Override
		public int getCount() {
			return mTutorialViews.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			final TutorialView tutorialView = mTutorialViews.get(position);
			final View layout = mLayoutInflater.inflate(tutorialView.layoutId, null);

			final TextView title = (TextView) layout.findViewById(android.R.id.title);
			title.setText(mContext.getString(tutorialView.titleResId).toUpperCase(Locale.getDefault()));

			final TextView summary = (TextView) layout.findViewById(android.R.id.summary);
			summary.setText(Html.fromHtml(mContext.getString(tutorialView.summaryResId).toString()));

			final ImageView icon = (ImageView) layout.findViewById(android.R.id.icon);
			icon.setImageResource(tutorialView.imageResId);

			((ViewPager) collection).addView(layout);

			return layout;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private static class TutorialView {
		private int layoutId = R.layout.tutorial_view;
		private int titleResId;
		private int summaryResId;
		private int imageResId;

		private TutorialView(int titleResId, int summaryResId, int imageResId) {
			this.titleResId = titleResId;
			this.summaryResId = summaryResId;
			this.imageResId = imageResId;
		}

		private TutorialView(int layoutId, int titleResId, int summaryResId, int imageResId) {
			this.layoutId = layoutId;
			this.titleResId = titleResId;
			this.summaryResId = summaryResId;
			this.imageResId = imageResId;
		}
	}
}
