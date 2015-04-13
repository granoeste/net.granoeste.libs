
package net.granoeste.scaffold.sample.app;

import static net.granoeste.commons.util.LogUtils.LOGV;
import static net.granoeste.commons.util.LogUtils.makeLogTag;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.granoeste.scaffold.app.ScaffoldWebViewFragment;
import net.granoeste.scaffold.sample.R;
import net.granoeste.scaffold.app.ScaffoldActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_hybrid)
public class HybridActivity extends ScaffoldActivity implements ScaffoldWebViewFragment.Callbacks {
	private static final String TAG = makeLogTag(HybridActivity.class);
	private HybridActivity self = this;

	ScaffoldWebViewFragment mFrag;
	WebView mWebView;
	Handler mHandler = new Handler();

    @AfterViews
    public void onAfterViews() {
        mFrag = findFragment(R.id.webview_frag);
        if (mFrag != null) {
            mWebView = mFrag.getWebView();
            mWebView.addJavascriptInterface(new Bridge(), "bridge");

            mFrag.loadUrl("file:///android_asset/form.html");
        }
    }


	@Override
	protected void onStart() {
		super.onStart();
	}

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

	final private class Bridge {
        @JavascriptInterface
		public void qrcode() {
			LOGV(TAG, "qrcode()");

			IntentIntegrator integrator = new IntentIntegrator(self);
			integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (result != null) {
			String contents = result.getContents();
			if (contents != null) {
				showCroutonInfo("Format: " + result.getFormatName() + ", Contents: " + contents);

				mWebView.loadUrl("javascript:setInputText(\"" + contents + "\");");
			}
		}
	}
}
