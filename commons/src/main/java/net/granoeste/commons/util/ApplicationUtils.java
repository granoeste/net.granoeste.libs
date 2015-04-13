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

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationUtils {

	private static String sVersionName = null;
	private static String sAppName = null;

	/**
	 * アプリケーション・バージョンを取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーション・バージョン
	 */
	public static String getAppVersionName(final Context context) {
		if (sVersionName == null) {
			try {
				sVersionName = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 1).versionName;
			} catch (final NameNotFoundException e) {
                // NOP
			}
		}
		return sVersionName;
	}

	/**
	 * アプリケーション名を取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーション名
	 */
	public static String getAppName(final Context context) {
		if (sAppName == null) {
			try {
				final PackageManager pm = context.getPackageManager();
				sAppName = pm.getPackageInfo(context.getPackageName(), 1).applicationInfo
						.loadLabel(pm).toString();
			} catch (final Exception e) {
                // NOP
			}
		}
		return sAppName;
	}

	/**
	 * パッケージ名を取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーション名
	 */
	public static String getPackageName(final Context context) {
		return context.getPackageName();
	}

}
