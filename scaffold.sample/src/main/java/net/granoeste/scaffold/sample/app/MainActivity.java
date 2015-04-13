
package net.granoeste.scaffold.sample.app;

import static net.granoeste.commons.util.LogUtils.LOGV;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

import net.granoeste.commons.util.BundleUtils;
import net.granoeste.commons.util.SimpleAsyncTask;
import net.granoeste.scaffold.app.ScaffoldAlertDialogFragment;
import net.granoeste.scaffold.sample.R;
import net.granoeste.scaffold.app.ScaffoldActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends ScaffoldActivity {
    private static final String TAG = makeLogTag(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addLifecycleListener(new DeviceSensorListener());

        super.onCreate(savedInstanceState);

//		setContentView(R.layout.activity_main);

//		Bundle extras = getIntent().getExtras();
//
//		if (BuildConfig.DEBUG) {
//			//デバッグ環境では開発用のモジュールを使う
//			if (extras != null) {
//				String moduleName = extras.getString("module");
//				if (moduleName != null) {
//					LOGD(TAG, "module=" + moduleName);
//					try {
//						@SuppressWarnings("unchecked")
//						Class<Module> cls = (Class<Module>) Class.forName(moduleName);
//						Module module = cls.newInstance();
//						((BaseApplication) getApplication()).setInjector(Guice
//								.createInjector(module));
//					} catch (ClassNotFoundException e) {
//						e.printStackTrace();
//					} catch (InstantiationException e) {
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}

//		buildView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @AfterViews
    public void buildView() {
        LOGV(TAG, "buildView()");

        MainFragment frag = new MainFragment_();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, frag, "main_frag")
                .commit();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        LOGV(TAG, String.format("onAttachFragment() %s", ((Object) fragment).getClass().getName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_webview:
            HybridActivity_.intent(this).start();
            return true;
        case R.id.menu_alert_dialog:
            showDialog(R.drawable.ic_launcher, "AlertDialogTitle", "AlertDialogMessage");
            return true;
        case R.id.menu_custom_view_alert_dialog:
//            new ScaffoldAlertDialogFragment() {
//                @Override
//                public View getCustomView() {
//                    LayoutInflater inflater = (LayoutInflater) getActivity()
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    return inflater.inflate(R.layout.dialog_custom, null);
//                }
//
//            }.builder()
//                    .iconId(R.drawable.ic_launcher)
//                    .title(getResources().getString(android.R.string.dialog_alert_title))
//                    .message(getResources().getString(android.R.string.emptyPhoneNumber))
//                    .hasNegative()
//                    .hasNeutral()
//                    .hasPositive()
//                    .cancelable()
//                    .canceledOnTouchOutside()
//                    .build()
//                    .show(getSupportFragmentManager(), "dialog");

            ScaffoldAlertDialogFragment dialogFrag = new ScaffoldAlertDialogFragment() {
                @Override
                public View getCustomView() {
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = inflater.inflate(R.layout.dialog_custom, null);
                    customView.findViewById(R.id.button1)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showToast("onClick:button1");
                                }
                            });
                    customView.findViewById(R.id.button2)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showToast("onClick:button2");
                                }
                            });

                    return customView;
                }
            };

            Bundle args = null;
//            args = new Bundle();
//            args.putInt(ScaffoldAlertDialogFragment.ICON_ID, R.drawable.ic_launcher);
//            args.putString(ScaffoldAlertDialogFragment.TITLE,
//                    getResources().getString(android.R.string.dialog_alert_title));
//            args.putString(ScaffoldAlertDialogFragment.MESSAGE,
//                    getResources().getString(android.R.string.emptyPhoneNumber));
//            args.putBoolean(ScaffoldAlertDialogFragment.HAS_POSITIVE, true);
//            args.putBoolean(ScaffoldAlertDialogFragment.HAS_NEUTRAL, true);
//            args.putBoolean(ScaffoldAlertDialogFragment.HAS_NEGATIVE, true);
//            args.putBoolean(ScaffoldAlertDialogFragment.CANCELABLE, true);
//            args.putBoolean(ScaffoldAlertDialogFragment.CANCELED_ON_TOUCH_OUTSIDE, true);

            // BundleUtils.builder作ったけどあんまりメリットは無いな...
            args = BundleUtils.builder()
                    .put(ScaffoldAlertDialogFragment.ICON_ID, R.drawable.ic_launcher)
                    .put(ScaffoldAlertDialogFragment.TITLE,
                            getResources().getString(android.R.string.dialog_alert_title))
                    .put(ScaffoldAlertDialogFragment.MESSAGE,
                            getResources().getString(android.R.string.emptyPhoneNumber))
                    .put(ScaffoldAlertDialogFragment.HAS_POSITIVE, true)
                    .put(ScaffoldAlertDialogFragment.HAS_NEUTRAL, true)
                    .put(ScaffoldAlertDialogFragment.HAS_NEGATIVE, true)
                    .put(ScaffoldAlertDialogFragment.CANCELABLE, true)
                    .put(ScaffoldAlertDialogFragment.CANCELED_ON_TOUCH_OUTSIDE, true)
                    .build();
//

            dialogFrag.setArguments(args);
            dialogFrag.show(getSupportFragmentManager(), "dialog");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void doCancel(DialogInterface dialog, String tag) {
        super.doCancel(dialog, tag);
        showToast("doCancel:" + tag);
    }

    @Override
    protected void doNegativeClick(DialogInterface dialog, int whichButton, String tag) {
        super.doNegativeClick(dialog, whichButton, tag);
        showToast("doNegativeClick:" + tag);
    }

    @Override
    protected void doNeutralClick(DialogInterface dialog, int whichButton, String tag) {
        super.doNeutralClick(dialog, whichButton, tag);
        showToast("doNeutralClick:" + tag);
    }

    @Override
    protected void doPositiveClick(DialogInterface dialog, int whichButton, String tag) {
        super.doPositiveClick(dialog, whichButton, tag);
        showToast("doPositiveClick:" + tag);
    }

}
