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

import java.text.MessageFormat;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.commons.util.UIUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * 水平自動オーバースクロールビュー
 */
public class HorizontalAutoOverScrollView extends HorizontalScrollView {
	private static final String TAG = LogUtils.makeLogTag(HorizontalAutoOverScrollView.class);
	static final boolean DEBUG_EXTRACT = false;

	static final long DEFAULT_DELAY_MILLIS = 1 * 1000L;
	static final long DEFAULT_SCROLL_DELAY_MILLIS = 200L;
	static final int DEFAULT_SCROLL_INCREMENT = 10;

	Handler mHandler = new Handler();
	TextView mChildTextView;

	public HorizontalAutoOverScrollView(Context context) {
		super(context);
		configurate();
	}

	public HorizontalAutoOverScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		configurate();
	}

	public HorizontalAutoOverScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		configurate();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void configurate() {
		if (UIUtils.hasGingerbread()) {
			setOverScrollMode(OVER_SCROLL_ALWAYS);
		}
		setVerticalScrollBarEnabled(false);
		setVerticalFadingEdgeEnabled(false);
		setHorizontalScrollBarEnabled(false);
		setHorizontalFadingEdgeEnabled(false);
		setFillViewport(true);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		if (DEBUG_EXTRACT) {
			Log.v(TAG,
					MessageFormat
							.format("overScrollBy({0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8})",
									deltaX, deltaY, scrollX, scrollY,
									scrollRangeX, scrollRangeY, maxOverScrollX,
									maxOverScrollY, isTouchEvent));
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	@Override
	protected void onAttachedToWindow() {
		// スクロールスレッドを実行
		mHandler.postDelayed(mScrollThread, DEFAULT_DELAY_MILLIS);
		// 子供のTextViewのテキスト変更のリスナーを設定
		View child = getChildAt(0);
		if (child != null && child.getClass() == TextView.class) {
			mChildTextView = (TextView) child;
			mChildTextView.addTextChangedListener(mTextWatcher);
		}
	};

	@Override
	protected void onDetachedFromWindow() {
		// スクロールスレッドを削除
		mHandler.removeCallbacks(mScrollThread);
		// 子供のTextViewのテキスト変更のリスナーを削除
		if (mChildTextView != null) {
			mChildTextView.removeTextChangedListener(mTextWatcher);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 手動でスクロールできないようにする
		return true;
	}

	/* TextView テキスト変更のリスナー */
	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (DEBUG_EXTRACT) {
				Log.v(TAG,
						MessageFormat.format("afterTextChanged({0})",
								s.toString()));
			}
			// スクロールスレッドを登録
			mHandler.removeCallbacks(mScrollThread);
			mHandler.postDelayed(mScrollThread, DEFAULT_DELAY_MILLIS);
		}
	};

	/* 自動スクロール用スレッド */
	Thread mScrollThread = new Thread() {

		int deltaX = 0;
		int scrollX = 0;

		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void run() {
			// デフォルトのディレイ値を設定
			long delayMillis = DEFAULT_SCROLL_DELAY_MILLIS;
			// 各Viewの値を取得
			int childWidth = getChildAt(0).getWidth();
			int width = getWidth();
			int curScrollX = getScrollX();
			if (DEBUG_EXTRACT) {
				Log.v(TAG, MessageFormat.format(
						"Width:{0} ScrollX:{1} ChildWidth:{2}", width,
						curScrollX, childWidth));
			}

			// 子のTextViewがScrollViewより幅が広い場合
			if (childWidth > width) {
				// スクロール値インクリメント
				deltaX += DEFAULT_SCROLL_INCREMENT;
				scrollX += DEFAULT_SCROLL_INCREMENT;

				// GB未満の場合は overScrollBy を使用しない
				if (UIUtils.hasGingerbread()) {
					// テキストを画面外までスクロールしたか？
					if (curScrollX > childWidth) {
						// 初期位置に戻す
						deltaX = 0;
						scrollX = 0;
						// 初期値からスクロールの遅延を延長
						delayMillis = DEFAULT_DELAY_MILLIS;
					}
					overScrollBy(deltaX, 0, scrollX, 0, 10, 0, childWidth, 0,
							false);

				} else {
					// テキストを末尾までスクロールしたか？
					if (width + curScrollX >= childWidth) {
						// 一時待機してから初期位置に戻す
						deltaX = 0;
						scrollX = 0;
						delayMillis = DEFAULT_DELAY_MILLIS;
						try {
							sleep(DEFAULT_DELAY_MILLIS);
						} catch (InterruptedException e) {
						}
						scrollTo(0, 0);
					} else {
						scrollBy(scrollX, 0);
					}
				}

				// 次のスクロールスレッドを遅延実行で登録
				mHandler.removeCallbacks(this);
				mHandler.postDelayed(this, delayMillis);
			} else {
				// スクロールが必要のでスレッドを削除
				mHandler.removeCallbacks(this);
			}
		}
	};
}
