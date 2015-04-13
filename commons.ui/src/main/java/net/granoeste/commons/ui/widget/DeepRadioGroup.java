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

package net.granoeste.commons.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class DeepRadioGroup extends LinearLayout {

	private int mCheckedId = -1;
	private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
	private boolean mProtectFromCheckedChange = false;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private PassThroughHierarchyChangeListener mPassThroughListener;

	public DeepRadioGroup(final Context context) {
		super(context);
		setOrientation(VERTICAL);
		init();
	}

	public DeepRadioGroup(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mChildOnCheckedChangeListener = new CheckedStateTracker();
		mPassThroughListener = new PassThroughHierarchyChangeListener();
		super.setOnHierarchyChangeListener(mPassThroughListener);
	}

	@Override
	public void setOnHierarchyChangeListener(final OnHierarchyChangeListener listener) {
		// the user listener is delegated to our pass-through listener
		mPassThroughListener.mOnHierarchyChangeListener = listener;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (mCheckedId != -1) {
			mProtectFromCheckedChange = true;
			setCheckedStateForView(mCheckedId, true);
			mProtectFromCheckedChange = false;
			setCheckedId(mCheckedId);
		}
	}

	@Override
	public void addView(final View child, final int index, final ViewGroup.LayoutParams params) {
		new TreeScanner(new OnRadioButtonFoundListener() {
			@Override
			public void onRadioButtonFound(final RadioButton radioButton) {
				if (radioButton.isChecked()) {
					mProtectFromCheckedChange = true;
					if (mCheckedId != -1) {
						setCheckedStateForView(mCheckedId, false);
					}
					mProtectFromCheckedChange = false;
					setCheckedId(radioButton.getId());
				}
			}
		}).scan(child);

		super.addView(child, index, params);
	}

	public void check(final int id) {
		if (id != -1 && id == mCheckedId) {
			return;
		}

		if (mCheckedId != -1) {
			setCheckedStateForView(mCheckedId, false);
		}

		if (id != -1) {
			setCheckedStateForView(id, true);
		}

		setCheckedId(id);
	}

	private void setCheckedId(final int id) {
		mCheckedId = id;
		if (mOnCheckedChangeListener != null) {
			mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
		}
	}

	private void setCheckedStateForView(final int viewId, final boolean checked) {
		final View checkedView = findViewById(viewId);
		if (checkedView != null && checkedView instanceof RadioButton) {
			((RadioButton) checkedView).setChecked(checked);
		}
	}

	public int getCheckedRadioButtonId() {
		return mCheckedId;
	}

	public void clearCheck() {
		check(-1);
	}

	public void setOnCheckedChangeListener(final OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	@Override
	public LayoutParams generateLayoutParams(final AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected boolean checkLayoutParams(final ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	public static class LayoutParams extends LinearLayout.LayoutParams {

		public LayoutParams(final Context c, final AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(final int w, final int h) {
			super(w, h);
		}

		public LayoutParams(final int w, final int h, final float initWeight) {
			super(w, h, initWeight);
		}

		public LayoutParams(final ViewGroup.LayoutParams p) {
			super(p);
		}

		public LayoutParams(final MarginLayoutParams source) {
			super(source);
		}

		@Override
		protected void setBaseAttributes(final TypedArray a,
				final int widthAttr, final int heightAttr) {

			if (a.hasValue(widthAttr)) {
				width = a.getLayoutDimension(widthAttr, "layout_width");
			} else {
				width = WRAP_CONTENT;
			}

			if (a.hasValue(heightAttr)) {
				height = a.getLayoutDimension(heightAttr, "layout_height");
			} else {
				height = WRAP_CONTENT;
			}
		}
	}

	public interface OnCheckedChangeListener {
		public void onCheckedChanged(DeepRadioGroup group, int checkedId);
	}

	private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
			// prevents from infinite recursion
			if (mProtectFromCheckedChange) {
				return;
			}

			mProtectFromCheckedChange = true;
			if (mCheckedId != -1) {
				setCheckedStateForView(mCheckedId, false);
			}
			mProtectFromCheckedChange = false;

			final int id = buttonView.getId();
			setCheckedId(id);
		}
	}

	private class PassThroughHierarchyChangeListener implements
			OnHierarchyChangeListener {
		private OnHierarchyChangeListener mOnHierarchyChangeListener;

		@Override
		public void onChildViewAdded(final View parent, final View child) {
			if (parent == DeepRadioGroup.this) {
				new TreeScanner(new OnRadioButtonFoundListener() {
					@Override
					public void onRadioButtonFound(final RadioButton radioButton) {
						int id = radioButton.getId();
						if (id == View.NO_ID) {
							id = radioButton.hashCode();
							radioButton.setId(id);
						}
						radioButton.setOnCheckedChangeListener(mChildOnCheckedChangeListener);

					}
				}).scan(child);
			}
			if (mOnHierarchyChangeListener != null) {
				mOnHierarchyChangeListener.onChildViewAdded(parent, child);
			}
		}

		@Override
		public void onChildViewRemoved(final View parent, final View child) {
			if (parent == DeepRadioGroup.this) {
				new TreeScanner(new OnRadioButtonFoundListener() {

					@Override
					public void onRadioButtonFound(final RadioButton radioButton) {
						radioButton.setOnCheckedChangeListener(null);

					}
				}).scan(child);
			}
		}
	}

	private class TreeScanner {
		OnRadioButtonFoundListener listener;

		public TreeScanner(final OnRadioButtonFoundListener listener) {
			this.listener = listener;
		}

		// Scan Recursively
		public void scan(final View child) {
			if (child instanceof RadioButton) {
				listener.onRadioButtonFound((RadioButton) child);
			}
			else if (child instanceof ViewGroup)
			{
				final ViewGroup viewGroup = (ViewGroup) child;
				for (int i = 0, l = viewGroup.getChildCount(); i < l; i++) {
					scan(viewGroup.getChildAt(i));
				}
			}

		}

	}

	private interface OnRadioButtonFoundListener {
		public void onRadioButtonFound(RadioButton radioButton);
	}
}
