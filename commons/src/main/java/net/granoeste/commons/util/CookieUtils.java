package net.granoeste.commons.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import static net.granoeste.commons.util.LogUtils.LOGV;

/**
 * Cookieを操作するためのユーティリティ
 */
@SuppressWarnings("deprecation")
public class CookieUtils {

    private static final String TAG = LogUtils.makeLogTag(CookieUtils.class);

    /**
     * Cookieを取得する
     *
     * @param context Context
     * @param url     url
     */
    public static String getCookie(Context context, String url) {
        // CookieSyncManagerの初期化
        initializeCookieSyncManager(context);

        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    /**
     * Cookieを保存する
     *
     * @param domain    Cookieのドメイン
     * @param cookieStr Cookie
     */
    public static void setCookieList(String domain, String cookieStr) {
        CookieManager cookieManager = CookieManager.getInstance();

        String[] cookieList = cookieStr.split(";");
        for (String cookie : cookieList) {
            cookieManager.setCookie(domain, cookie);
        }
    }

    /**
     * CookieSyncManagerの初期化を行う。
     *
     * @param context Contextインスタンス
     */
    private static void initializeCookieSyncManager(Context context) {
        // LOLLIPOP以降では不要
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            // CookieSyncManager.createInstance()を読んでいない状態でsetCookieするとエラーになる
            CookieSyncManager.createInstance(context);
        }
    }

    /**
     * Cookieを同期(永続化)する
     */
    @SuppressLint("NewApi")
    private static void synCookie() {
        // CookieSyncManager.getInstance().sync()はLOLLIPOP以降非推奨なのでflush()を使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LOGV(TAG, "synCookie with CookieManager.flush()");
            CookieManager.getInstance().flush();
        } else {
            LOGV(TAG, "synCookie with CookieSyncManager.sync()");
            //CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * Cookieを削除する
     *
     * @param url 削除するCookieのドメイン
     */
    public void clearAllCookie(Context context, String url) {
        LOGV(TAG, "clearAllCookie()");
        // CookieSyncManagerの初期化
        initializeCookieSyncManager(context);

        CookieManager cookieManager = CookieManager.getInstance();
        String cookieString = cookieManager.getCookie(url);
        String[] cookies = cookieString.split(";");
        for (String cookie : cookies) {
            String[] cookieParts = cookie.split("=");
            String key = cookieParts[0].trim();
            cookieManager.setCookie(url, key + "=; Expires=Wed, 31 Dec 2025 23:59:59 GMT");
        }

        // Cookieの永続化
        synCookie();
    }

}
