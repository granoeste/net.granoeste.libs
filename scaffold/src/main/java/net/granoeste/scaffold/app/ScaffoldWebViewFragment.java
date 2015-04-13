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

package net.granoeste.scaffold.app;

import static net.granoeste.commons.util.LogUtils.LOGD;
import static net.granoeste.commons.util.LogUtils.LOGE;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.granoeste.commons.util.UIUtils;
import net.granoeste.scaffold.R;

public abstract class ScaffoldWebViewFragment extends ScaffoldFragment {
    private static final String TAG = makeLogTag(ScaffoldWebViewFragment.class);

    // フレーム用ID
    static final int INTERNAL_EMPTY_ID = R.id.webview_empty;
    static final int INTERNAL_PROGRESS_CONTAINER_ID = R.id.webview_progress_container;
    static final int INTERNAL_CONTENT_CONTAINER_ID = R.id.webview_content_container;

    // Parameters
    public static final String USER_AGENT = "user_agent";
    public static final String IS_RESTORE_STATE = "is_restore_state";
    public static final String LOAD_URL = "load_url";

    // ------------------------------------------------------------------------
    // Callback Definition
    // ------------------------------------------------------------------------
    public interface Callbacks {
        public boolean shouldOverrideUrlLoading(final WebView view, final String url);

        public void onPageFinished(final WebView view, final String url);

        public void onReceivedError(final WebView view, final int errorCode,
                                    final String description, final String url);
    }

    private final Callbacks mDummyCallbacks = new Callbacks() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String url) {
        }

    };

    private Callbacks mCallbacks = mDummyCallbacks;
    // ------------------------------------------------------------------------

    private LinearLayout mPFrame;
    private FrameLayout mCFrame;
    private InternalWebView mWebView;

    public ScaffoldWebViewFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callbacks) {
            mCallbacks = (Callbacks) activity;
        } else {
            Fragment frag = getTargetFragment();
            if (frag != null && frag instanceof Callbacks) {
                mCallbacks = (Callbacks) frag;
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = mDummyCallbacks;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        FrameLayout root = new FrameLayout(getActivity());

        // ------------------
        // Progress content
        // ------------------
        mPFrame = new LinearLayout(getActivity());
        mPFrame.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        mPFrame.setOrientation(LinearLayout.VERTICAL);
        mPFrame.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(getActivity(), null,
                android.R.attr.progressBarStyleLarge);
        mPFrame.addView(progress, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // ----------------
        // WebView content
        // ----------------
        mCFrame = new FrameLayout(getActivity());
        mCFrame.setId(INTERNAL_CONTENT_CONTAINER_ID);

        mWebView = new InternalWebView(getActivity());
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebView.getSettings().setJavaScriptEnabled(true);

        // Flash Support
        if (UIUtils.hasKitkat()) {
            // Flash対応(4.4以降は動かない)
        } else if (UIUtils.hasJellyBeanMr2()) {
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else if (UIUtils.hasFroyo()) {
            // Deprecated Since API level 9, and removed in API level 18 (JellyBeanMr2)
            // mWebView.getSettings().setPluginEnabled(true);
        }

        // mWebView.getSettings().setVerticalScrollbarOverlay(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // ダウンロード処理
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType(mimetype);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        mWebView.setWebViewClient(new InternalWebViewClient(root, mCallbacks));

        mCFrame.addView(mWebView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // ------------------------------------------------------------------

        root.addView(mCFrame, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(mPFrame, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle args = getArguments();
        if (args != null) {
            // カスタムUser-Agentの設定
            String userAgent = args.getString(USER_AGENT);
            if (!TextUtils.isEmpty(userAgent)) {
                mWebView.getSettings().setUserAgentString(userAgent);
            }

            // 前回表示していたURLをリストアするか？
            if (args.getBoolean(IS_RESTORE_STATE, false)) {
                mWebView.restoreState(savedInstanceState);
            } else {
                String url = args.getString(LOAD_URL);
                if (!TextUtils.isEmpty(url)) {
                    LOGD(TAG, "get url... " + url);
                    mWebView.loadUrl(url);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // フラグメントが破棄された後に復帰できるようにWebView履歴などを保存する
        mWebView.saveState(outState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPause() {
        super.onPause();
        if (UIUtils.hasHoneycomb()) {
            mWebView.onPause();
        }
        // Pauses all layout, parsing, and JavaScript timers for all WebViews.
        mWebView.pauseTimers();

// TPS: Cookieの永続化は、パフォーマンスを考慮して常に行うのでは無く、onPause時に強制的に行う。
//      // Requests sync manager to stop sync.
//        CookieSyncManager.getInstance().stopSync();
        // Cookieの永続化
        CookieSyncManager.getInstance().sync();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onResume() {
        super.onResume();
        if (UIUtils.hasHoneycomb()) {
            mWebView.onResume();
        }
        // Resumes all layout, parsing, and JavaScript timers for all WebViews.
        mWebView.resumeTimers();

// TPS: Cookieの永続化は、パフォーマンスを考慮して常に行うのでは無く、onPause時に強制的に行う。
//      // Requests sync manager to start sync
//        CookieSyncManager.getInstance().startSync();
        // Cookieの永続化の停止
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            unregisterForContextMenu(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    // ------------------------------------------------------------------------
    // Internal Classes
    // ------------------------------------------------------------------------
    public static class InternalWebView extends WebView {

        public InternalWebView(Context context) {
            super(context);
        }

        public InternalWebView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InternalWebView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void setWebChromeClient(WebChromeClient client) {
            super.setWebChromeClient(client);
        }

        @Override
        public void setWebViewClient(WebViewClient client) {
            if (client != null && !(client instanceof InternalWebViewClient)) {
                LOGE(TAG, "Why don't you use the " + ScaffoldWebViewFragment.InternalWebView.class.getName() + "? Should use it!");
            }
            super.setWebViewClient(client);
        }
    }

    public static class InternalWebViewClient extends WebViewClient {
        View frame;
        Callbacks callbacks;

        public InternalWebViewClient(View frame, Callbacks callbacks) {
            super();
            this.frame = frame;
            this.callbacks = callbacks;
        }

        //外部ブラウザを利用しない
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            if (!callbacks.shouldOverrideUrlLoading(view, url)) {
                view.loadUrl(url);
            }
            return true;
        }

        //ページ読み込み完了時に呼ばれる
        @Override
        public void onPageFinished(final WebView view, final String url) {
            // close progress
            frame.findViewById(INTERNAL_PROGRESS_CONTAINER_ID).setVisibility(View.GONE);
            callbacks.onPageFinished(view, url);
        }

        //エラー時に呼ばれる
        @Override
        public void onReceivedError(final WebView view, final int errorCode,
                                    final String description, final String url) {
            callbacks.onReceivedError(view, errorCode, description, url);
        }
    }

    // ------------------------------------------------------------------------
    public void loadUrl(String url) {
        LOGD(TAG, "get url... " + url);
        mPFrame.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }

    /**
     * @param uri
     */
    public void loadUrl(Uri uri) {
        loadUrl(uri.toString());
    }

    /**
     * loadUrl
     * "android.resource://[package]/[res id]"
     *
     * @param context
     * @param resId
     */
    public void loadUrl(Context context, int resId) {
        // "android.resource://[package]/[res id]"
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
        loadUrl(uri.toString());
    }

    /**
     * loadUrl
     * "android.resource://[package]/[res type]/[res name]"
     *
     * @param context
     * @param resType
     * @param resName
     */
    public void loadUrl(Context context, String resType, String resName) {
        // "android.resource://[package]/[res type]/[res name]"
        Uri uri = Uri.parse("android.resource://" + context.getPackageName()
                + "/" + resType + "/" + resName);
        loadUrl(uri.toString());
    }

    /**
     * get data
     *
     * @param data
     * @param mimeType
     * @param encoding
     */
    public void loadData(String data, String mimeType, String encoding) {
        LOGD(TAG, "get data... ");
        mPFrame.setVisibility(View.VISIBLE);
        mWebView.loadData(data, mimeType, encoding);
    }

    /**
     * get data with base url
     *
     * @param baseUrl
     * @param data
     * @param mimeType
     * @param encoding
     * @param failUrl
     */
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding,
                                    String failUrl) {
        LOGD(TAG, "get data with base url... ");
        mPFrame.setVisibility(View.VISIBLE);
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
    }

    public InternalWebView getWebView() {
        return mWebView;
    }
}
