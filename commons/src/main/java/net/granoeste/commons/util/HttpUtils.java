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

import java.util.HashMap;

import android.text.TextUtils;
import android.webkit.CookieManager;

public class HttpUtils {

	/**
	 * URLのCookieを取得
	 *
	 * @param url
	 * @return map of Cookies
	 */
	public static HashMap<String, String> getCookie(String url) {
		HashMap<String, String> map = new HashMap<String, String>();
		// 文字列でCookieを取得
		String cookie = CookieManager.getInstance().getCookie(url);
		if (!TextUtils.isEmpty(cookie)) {
			String[] oneCookie = cookie.split(";");
			for (String pair : oneCookie) {
				pair = pair.trim();
				String[] set = pair.split("=");
				String key = set[0].trim();
				String value = set[1].trim();
				map.put(key, value);
			}
		}
		return map;
	}
}
