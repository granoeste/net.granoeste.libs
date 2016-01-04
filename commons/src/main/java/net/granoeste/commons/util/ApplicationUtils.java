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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class ApplicationUtils {
    private static final String TAG = LogUtils.makeLogTag(ApplicationUtils.class);

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

    /**
     * ターゲットアプリパッケージを検索
     *
     * @param context コンテキスト
     * @param targetApplication アプリケーションパッケージ
     * @param intent インテント
     * @return ActivityInfo
     */
    public static ActivityInfo findTargetAppPackage(Context context,
                                                    String targetApplication, Intent intent) {
        return findTargetAppPackage(context, Lists.newArrayList(targetApplication), intent);
    }

    /**
     * ターゲットアプリパッケージを検索
     *
     * @param context コンテキスト
     * @param targetApplications アプリケーションパッケージ
     * @param intent インテント
     * @return ActivityInfo
     */
    public static ActivityInfo findTargetAppPackage(Context context,
                                                    List<String> targetApplications,
                                                    Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> availableApps = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (availableApps != null) {
            for (ResolveInfo availableApp : availableApps) {
                String packageName = availableApp.activityInfo.packageName;
                String name = availableApp.activityInfo.name;
                Log.d(TAG, "Found... " + packageName + "/" + name);
                if (targetApplications.contains(packageName)) {
                    return availableApp.activityInfo;
                }
            }
        }
        return null;
    }

    /* 各ブラウザーのパッケージ */
    private static String ANDROID_BROWSER = "com.android.browser";
    private static String ANDROID_CHROME = "com.android.chrome";
    private static String MOZILLA_FIREFOX = "org.mozilla.firefox";

    private static List<String> BROWSERS = Lists.newArrayList(
            ANDROID_BROWSER, ANDROID_CHROME, MOZILLA_FIREFOX);

    /**
     * ブラウザを開く。 定義済みのブラウザが存在した場合は、それを明示的に開く。存在しなかった場合は暗黙的に呼び出す。
     *
     * @param context コンテキスト
     * @param url     URL
     */
    public static void openWebBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ActivityInfo activityInfo;
        if ((activityInfo = findTargetAppPackage(context, BROWSERS, intent)) != null) {
            intent.setClassName(activityInfo.packageName, activityInfo.name);
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Activityが存在しなかった。暗黙的INTENTでも発生する。
            Log.w(TAG, "Did not open url on browser.", e);
        }
    }
}
