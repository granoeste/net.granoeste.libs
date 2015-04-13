/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2013 CREADORE GRANOESTE
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

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

/**
 * A "bug fixed" simple dialog containing an {@link android.widget.DatePicker}.
 * <p>
 * See the <a href="{@docRoot}
 * resources/tutorials/views/hello-datepicker.html">Date Picker tutorial</a>.
 * </p>
 */
public class DatePickerDialog extends AlertDialog implements OnClickListener,
		OnDateChangedListener {

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private final DatePicker mDatePicker;
	private final OnDateSetListener mCallBack;

// Not include callback interface. use origin "android.app.DatePickerDialog.OnDateSetListener"
//	/**
//	 * The callback used to indicate the user is done filling in the date.
//	 */
//	public interface OnDateSetListener extends android.app.DatePickerDialog.OnDateSetListener {
//
//		/**
//		 * @param view The view associated with this listener.
//		 * @param year The year that was set.
//		 * @param monthOfYear The month that was set (0-11) for compatibility
//		 *            with {@link java.util.Calendar}.
//		 * @param dayOfMonth The day of the month that was set.
//		 */
//		@Override
//		void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
//	}

	/**
	 * @param context The context the dialog is to run in.
	 * @param callBack How the parent is notified that the date is set.
	 * @param year The initial year of the dialog.
	 * @param monthOfYear The initial month of the dialog.
	 * @param dayOfMonth The initial day of the dialog.
	 */
	public DatePickerDialog(final Context context,
			final OnDateSetListener callBack,
			final int year,
			final int monthOfYear,
			final int dayOfMonth) {
		this(context,
				Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? 0
						: R.style.Theme_Dialog_DatePicker,
				callBack, year, monthOfYear, dayOfMonth);
	}

	/**
	 * @param context The context the dialog is to run in.
	 * @param theme the theme to apply to this dialog
	 * @param callBack How the parent is notified that the date is set.
	 * @param year The initial year of the dialog.
	 * @param monthOfYear The initial month of the dialog.
	 * @param dayOfMonth The initial day of the dialog.
	 */
	public DatePickerDialog(final Context context,
			final int theme,
			final OnDateSetListener callBack,
			final int year,
			final int monthOfYear,
			final int dayOfMonth) {
		super(context, theme);

		mCallBack = callBack;

		final Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, themeContext.getText(R.string.date_time_set), this);
		setButton(BUTTON_NEGATIVE, themeContext.getText(android.R.string.cancel), (OnClickListener) null);
		setIcon(0);
		setTitle(R.string.date_picker_dialog_title);

		final LayoutInflater inflater =
				(LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setView(view);
		mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
	}

	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		if (mCallBack != null) {
			mDatePicker.clearFocus();
			mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
					mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
		}
	}

	@Override
	public void onDateChanged(final DatePicker view, final int year,
			final int month, final int day) {
		mDatePicker.init(year, month, day, null);
	}

	/**
	 * Gets the {@link android.widget.DatePicker} contained in this dialog.
	 *
	 * @return The calendar view.
	 */
	public DatePicker getDatePicker() {
		return mDatePicker;
	}

	/**
	 * Sets the current date.
	 *
	 * @param year The date year.
	 * @param monthOfYear The date month.
	 * @param dayOfMonth The date day of month.
	 */
	public void updateDate(final int year, final int monthOfYear, final int dayOfMonth) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	@Override
	public Bundle onSaveInstanceState() {
		final Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		final int year = savedInstanceState.getInt(YEAR);
		final int month = savedInstanceState.getInt(MONTH);
		final int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);
	}
}