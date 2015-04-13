/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.commons.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Dialog Fragment
 */
public class IconContextMenuDialogFragment extends DialogFragment {

	/** ICON CONTEXT MENU TITLE */
	public static final String ICON_CONTEXT_MENU_TITLE = "icon_context_menu_title";
	/** ICON CONTEXT MENU ITEMS */
	public static final String ICON_CONTEXT_MENU_ITEMS = "icon_context_menu_items";

	private String mTitle;
	private ArrayList<IconContextMenuItem> mMenuItems;
	private IconMenuAdapter mMenuAdapter;

	// ------------------------------------------------------------------------
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * Callbacks
	 */
	public interface Callbacks {
		/**
		 * On Icon Context Menu On Item Selected.
		 * 
		 * @param menuItem IconContextMenuItem
		 */
		public void onIconContextMenuOnItemSelected(IconContextMenuItem menuItem);
	}

	/**
	 * Dummy Callbacks
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onIconContextMenuOnItemSelected(final IconContextMenuItem menuItem) {
		}
	};

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
//			throw new IllegalStateException(
//					"Activity must implement fragment's callbacks.");
			mCallbacks = sDummyCallbacks;
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	// ------------------------------------------------------------------------

	public static IconContextMenuDialogFragment newInstance() {
		return new IconContextMenuDialogFragment();
	}

//	public IconContextMenuDialogFragment() {
//	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());

		final Bundle args = getArguments();
		if (mTitle == null && args != null) {
			mTitle = args.getString(ICON_CONTEXT_MENU_TITLE);
		}

		if (args != null) {
			mMenuItems = (ArrayList<IconContextMenuItem>) args
					.getSerializable(ICON_CONTEXT_MENU_ITEMS);
		}
		if (mMenuAdapter == null) {
			if (mMenuItems != null) {
				mMenuAdapter = new IconMenuAdapter(getActivity());
				for (final IconContextMenuItem iconContextMenuItem : mMenuItems) {
					mMenuAdapter.addItem(iconContextMenuItem);
				}
			}
		}

		if (!TextUtils.isEmpty(mTitle)) {
			builder.setTitle(mTitle);
		}

		builder.setAdapter(mMenuAdapter,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(
							final DialogInterface dialoginterface,
							final int i) {
						final IconContextMenuItem item = (IconContextMenuItem) mMenuAdapter
								.getItem(i);

						mCallbacks.onIconContextMenuOnItemSelected(item);

						dismiss();
					}
				});

		builder.setInverseBackgroundForced(true);

		final AlertDialog dialog = builder.create();
		dialog.setOnCancelListener(this);
		dialog.setOnDismissListener(this);
		return dialog;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null && mMenuAdapter == null) {
			final ArrayList<IconContextMenuItem> items = savedInstanceState
					.getParcelableArrayList(ICON_CONTEXT_MENU_ITEMS);
			mMenuAdapter = new IconMenuAdapter(getActivity());
			for (final IconContextMenuItem iconContextMenuItem : items) {
				mMenuAdapter.addItem(iconContextMenuItem);
			}
			mTitle = savedInstanceState.getString(ICON_CONTEXT_MENU_TITLE);
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(ICON_CONTEXT_MENU_ITEMS, mMenuItems);
		outState.putString(ICON_CONTEXT_MENU_TITLE, mTitle);
	}

	@Override
	public void onCancel(final DialogInterface dialog) {
		super.onCancel(dialog);
	}

	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	/**
	 * Menu-like list adapter with icon
	 */
	protected class IconMenuAdapter extends BaseAdapter {
		private static final int LIST_PREFERED_HEIGHT = 65;
		private Context mContext = null;

		private final ArrayList<IconContextMenuItem> mItems = new ArrayList<IconContextMenuItem>();

		public IconMenuAdapter(final Context context) {
			this.mContext = context;
		}

		/**
		 * add item to adapter
		 * 
		 * @param menuItem
		 */
		public void addItem(final IconContextMenuItem menuItem) {
			mItems.add(menuItem);
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(final int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(final int position) {
			final IconContextMenuItem item = (IconContextMenuItem) getItem(position);
			return item.getActionId();
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final IconContextMenuItem item = (IconContextMenuItem) getItem(position);

			final Resources res = mContext.getResources();

			if (convertView == null) {
				final TextView temp = new TextView(mContext);
				final AbsListView.LayoutParams param = new AbsListView.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				temp.setLayoutParams(param);
				temp.setPadding(
						(int) toPixel(res, 5),
						(int) toPixel(res, 10),
						(int) toPixel(res, 5),
						(int) toPixel(res, 10));
				temp.setGravity(android.view.Gravity.CENTER_VERTICAL);

				// テーマの設定は不要
				// final Theme th = context.getTheme();
				// final TypedValue tv = new TypedValue();
				//
				// if (th.resolveAttribute(
				// android.R.attr.textAppearanceLargeInverse, tv, true)) {
				// temp.setTextAppearance(context, tv.resourceId);
				// }

				temp.setMinHeight(LIST_PREFERED_HEIGHT);
				temp.setCompoundDrawablePadding((int) toPixel(res, 14));
				convertView = temp;
			}

			final TextView textView = (TextView) convertView;
			textView.setTag(item);
			textView.setText(item.getText());
			textView.setSingleLine();
			textView.setEllipsize(TruncateAt.END);
			textView.setCompoundDrawablesWithIntrinsicBounds(item.getImage(), null,
					null, null);

			return textView;
		}

		private float toPixel(final Resources res, final int dip) {
			final float px = TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
			return px;
		}
	}

}
