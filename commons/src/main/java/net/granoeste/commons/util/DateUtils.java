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

package net.granoeste.commons.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {
	/** ISO8601 formatter for date-time without time zone. */
	public static String ISO_DATETIME_FORMAT = DateFormatUtils.ISO_DATETIME_FORMAT.getPattern();

	/**
	 * get the ISO8601 formatted date string now.
	 *
	 * @return ISO8601 formatted for date-time without time zone.
	 */
	public static String getNowDateISO() {
		return getFormatedDate(System.currentTimeMillis(), ISO_DATETIME_FORMAT);
	}

	/**
	 * get the ISO8601 formatted date string.
	 *
	 * @param millis Millis
	 * @return ISO8601 formatted for date-time without time zone.
	 */
	public static String getDateISO(final long millis) {
		return getFormatedDate(millis, ISO_DATETIME_FORMAT);
	}

	/**
	 * get the Timestamp now.
	 *
	 * @return Timestamp
	 */
	public static Timestamp getNowTimestamp() {
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	/**
	 * Formats the specified millis using the rules of this format.
	 *
	 * @param millis Millis
	 * @param pattern the pattern.
	 * @return formatted date string
	 */
	public static String getFormatedDate(final long millis, final String pattern) {
		return new SimpleDateFormat(pattern).format(millis);
	}

	/**
	 * Parses a date from the specified string using the rules of the iso datetime format.
	 *
	 * @param string the string to parse.
	 * @return the Date resulting from the parsing.
	 * @throws java.text.ParseException
	 */
	public static Date parseIso(final String string) throws ParseException {
		return parse(string, ISO_DATETIME_FORMAT);
	}

	/**
	 * Parses a date from the specified string using the rules of this date format.
	 *
	 * @param string the string to parse.
	 * @param pattern the pattern.
	 * @return the Date resulting from the parsing.
	 * @throws java.text.ParseException
	 */
	public static Date parse(final String string, final String pattern) throws ParseException {
		return new SimpleDateFormat(pattern).parse(string);
	}

	/**
	 * Parses a date from the specified string using the rules of this date format.
	 *
	 * @param string the string to parse.
	 * @param pattern the pattern.
	 * @return the Date resulting from the parsing.
	 * @throws java.text.ParseException
	 */
	public static Date getDate(final String string, final String pattern) throws ParseException {
		return new SimpleDateFormat(pattern).parse(string);
	}

	public static Date today() {
		return truncateTime(new Date());
	}

	/**
	 * 月初の週の月曜日の日付
	 *
	 * @param date
	 * @param amount ex) last month : -1. the current month : 0. the next month : 1
	 * @return The date on Monday of a week at the beginning of the month.
	 */
	public static Date getDateOnMondayOfAWeekAtTheBeginningOfTheMonth(
			final Date date, final int amount) {

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		shiftDateOnMondayOfAWeekAtTheBeginningOfTheMonth(cal, amount);

		return cal.getTime();
	}

	/**
	 * カレンダーを月初の週の月曜日の日付に移動
	 *
	 * @param cal
	 * @param amount ex) last month : -1. the current month : 0. the next month : 1
	 */
	public static void shiftDateOnMondayOfAWeekAtTheBeginningOfTheMonth(
			final Calendar cal, final int amount) {

		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, amount);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DATE, -6);
		} else {
			cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK) + 2);
		}
	}

	/**
	 * 月末の週の日曜日の日付
	 *
	 * @param date
	 * @param amount ex) last month : -1. the current month : 0. the next month : 1
	 * @return The date on Sunday of the week of the end of the month.
	 */
	public static Date getDateOnSundayOfTheWeekOfTheEndOfTheMonth(
			final Date date, final int amount) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		shiftDateOnSundayOfTheWeekOfTheEndOfTheMonth(cal, amount);

		return cal.getTime();
	}

	/**
	 * カレンダーを月末の週の日曜日の日付に移動
	 *
	 * @param cal
	 * @param amount ex) last month : -1. the current month : 0. the next month : 1
	 */
	public static void shiftDateOnSundayOfTheWeekOfTheEndOfTheMonth(
			final Calendar cal, final int amount) {

		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, amount);
		cal.add(Calendar.DATE, -1);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			cal.add(Calendar.DATE, 1);
		} else {
			cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) + 1);
		}
	}

	/**
	 * 月初の日付 (1日)
	 *
	 * @param date
	 * @param amount ex) last month : -1. the current month : 0. the next month : 1
	 * @return A date at the beginning of the month.
	 */
	public static Date getDateAtTheBeginningOfTheMonth(
			final Date date, final int amount) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, amount);
		return cal.getTime();
	}

	/**
	 * 月末の日付
	 *
	 * @param date
	 * @param amount ex) last month : -1. the current month : 0. the next month : 1
	 * @return The date of the end of the month.
	 */
	public static Date getDateOfTheEndOfTheMonth(
			final Date date, final int amount) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, amount + 1);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * カレンダーを週の月曜日の日付に移動
	 *
	 * @param date
	 * @param amount ex) last week : -1. the current week : 0. the next week : 1
	 * @return The date of Monday of the week.
	 */
	public static Date getDateOnMondayOfTheWeek(
			final Date date, final int amount) {

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		shiftDateOnMondayOfTheWeek(cal, amount);

		return cal.getTime();
	}

	/**
	 * カレンダーを週の月曜日の日付に移動
	 *
	 * @param cal
	 * @param amount ex) last week : -1. the current week : 0. the next week : 1
	 */
	public static void shiftDateOnMondayOfTheWeek(
			final Calendar cal, final int amount) {

		cal.add(Calendar.WEEK_OF_YEAR, amount);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DATE, -6);
		} else {
			cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK) + 2);
		}
	}

	/**
	 * 時間を00:00:00.000に設定
	 *
	 * @param date
	 * @return 時間が00:00:00.000に設定された日付
	 */
	public static Date truncateTime(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 時と分を設定 秒とミリ秒は0で設定
	 *
	 * @param date
	 * @param hours
	 * @param minutes
	 * @return
	 */
	public static Date setHoursMinutes(final Date date, final int hours, final int minutes) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 時刻を5分間隔に調整
	 *
	 * @param date
	 * @return
	 */
	public static Date adjustedToFiveMinuteIntervals(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 秒, ミリ秒は切り捨て
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// 5分間隔に調整
		int min = cal.get(Calendar.MINUTE);
		min = Math.round(min / 5) * 5;
		cal.set(Calendar.MINUTE, min);

		return cal.getTime();
	}

	/**
	 * Dateの日付を比較
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDay(final Date date1, final Date date2) {
		return truncateTime(date1).compareTo(truncateTime(date2));
	}

	/**
	 * 2つの日付の月数の差を求める。
	 *
	 * @param date1 日付1 java.util.Date
	 * @param date2 日付2 java.util.Date
	 * @return 2つの日付の月数の差
	 */
	public static int diffMonth(final Date date1, final Date date2) {
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		cal1.set(Calendar.DATE, 1);
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		cal2.set(Calendar.DATE, 1);
		int count = 0;
		if (cal1.before(cal2)) {
			while (cal1.before(cal2)) {
				cal1.add(Calendar.MONTH, 1);
				count--;
			}
		} else {
			count--;
			while (!cal1.before(cal2)) {
				cal1.add(Calendar.MONTH, -1);
				count++;
			}
		}
		return count;
	}

	public static int diffDays(final Date date1, final Date date2) {
		final long time1 = truncateTime(date1).getTime();
		final long time2 = truncateTime(date2).getTime();
		final long oneDateTime = 1000 * 60 * 60 * 24;
		final long diffDays = (time1 - time2) / oneDateTime;
		return (int) diffDays;
	}

	// --
	// org.apache.commons.lang.time.DateUtils wrapper
	// --
	public static Date addYears(final Date date, final int amount) {
		return org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
	}

	public static Date addMonths(final Date date, final int amount) {
		return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
	}

	public static Date addDays(final Date date, final int amount) {
		return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
	}

	public static Date addWeeks(final Date date, final int amount) {
		return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
	}

	public static Date addHours(final Date date, final int amount) {
		return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
	}

	public static Date addMinutes(final Date date, final int amount) {
		return org.apache.commons.lang3.time.DateUtils.addMinutes(date, amount);
	}

	public static boolean isSameDay(final Date date1, final Date date2) {
		return org.apache.commons.lang3.time.DateUtils.isSameDay(date1, date2);
	}

	public static Date truncate(final Date date, final int field) {
		return org.apache.commons.lang3.time.DateUtils.truncate(date, field);
	}

	public static Calendar truncate(final Calendar cal, final int field) {
		return org.apache.commons.lang3.time.DateUtils.truncate(cal, field);
	}

}
