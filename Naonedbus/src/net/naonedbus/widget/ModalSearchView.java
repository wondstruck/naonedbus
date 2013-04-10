package net.naonedbus.widget;

import net.naonedbus.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ModalSearchView extends LinearLayout implements TextWatcher {

	/**
	 * Callbacks for changes to the query text.
	 */
	public static interface OnQueryTextListener {

		/**
		 * Called when the query text is changed by the user.
		 * 
		 * @param newText
		 *            the new content of the query text field.
		 * 
		 * @return false if the SearchView should perform the default action of
		 *         showing any suggestions if available, true if the action was
		 *         handled by the listener.
		 */
		void onQueryTextChange(String newText);
	}

	private EditText mQueryTextView;
	private ImageView mCloseButton;
	private OnQueryTextListener mOnQueryTextListener;

	public ModalSearchView(final Context context, final AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mQueryTextView = (EditText) findViewById(R.id.searchViewText);
		mQueryTextView.addTextChangedListener(this);
		if (mQueryTextView.getHint() != null) {
			mQueryTextView.setHint(getDecoratedHint(mQueryTextView.getHint()));
		}

		mCloseButton = (ImageView) findViewById(R.id.searchViewClose);
		mCloseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				mQueryTextView.setText(null);
			}
		});
	}

	public void setOnQueryTextListener(final OnQueryTextListener onQueryTextListener) {
		mOnQueryTextListener = onQueryTextListener;
	}

	private CharSequence getDecoratedHint(final CharSequence hintText) {
		final SpannableStringBuilder ssb = new SpannableStringBuilder("   "); // for
																				// the
																				// icon
		ssb.append(hintText);

		final Drawable searchIcon = getContext().getResources().getDrawable(R.drawable.ic_action_search);
		final int textSize = (int) (mQueryTextView.getTextSize() * 1.25);
		searchIcon.setBounds(0, 0, textSize, textSize);
		ssb.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	@Override
	public void afterTextChanged(final Editable s) {

	}

	@Override
	public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

	}

	@Override
	public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
		if (mOnQueryTextListener != null) {
			mOnQueryTextListener.onQueryTextChange(s.toString());
		}

		if (s.length() > 0) {
			if (mCloseButton.getVisibility() != View.VISIBLE) {
				final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
				mCloseButton.setVisibility(View.VISIBLE);
				mCloseButton.startAnimation(fadeIn);
			}
		} else {
			mCloseButton.setVisibility(View.GONE);
		}
	}

	public void setText(final CharSequence text) {
		mQueryTextView.setText(text);
	}

}
