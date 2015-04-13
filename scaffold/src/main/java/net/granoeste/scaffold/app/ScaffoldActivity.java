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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.uphyca.lifecyclecallbacks.LifecycleCallbacksSupportActionBarActivity;

import net.granoeste.commons.ui.IconContextMenu;
import net.granoeste.commons.ui.IconContextMenuDialogFragment;
import net.granoeste.commons.ui.IconContextMenuItem;
import net.granoeste.commons.util.UIUtils;
import net.granoeste.scaffold.app.builder.ActivityIntentBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style.Builder;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

public abstract class ScaffoldActivity extends
        LifecycleCallbacksSupportActionBarActivity implements
        ScaffoldAlertDialogFragment.OnAlertDialogEventListener,
        IconContextMenuDialogFragment.Callbacks {
    private static final String TAG = makeLogTag(ScaffoldActivity.class);

    private Handler mInternalHandler;

    private final Set<ScaffoldLifecycleListener> mScaffoldLifecycleListeners = Collections
            .synchronizedSet(new HashSet<ScaffoldLifecycleListener>());
    private ScaffoldAlertDialogFragment mAlertDialogFragment;

    protected void addLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.add(listener);
    }

    protected void removeLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.remove(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle extras = getIntent().getExtras();
//
//        if (BuildConfig.DEBUG) {
//            // デバッグ環境では開発用のモジュールを使う
//            if (extras != null) {
//                String moduleName = extras.getString("module");
//                if (moduleName != null) {
//                    LOGD(TAG, "module=" + moduleName);
//                    try {
//                        @SuppressWarnings("unchecked")
//                        Class<Module> cls = (Class<Module>) Class
//                                .forName(moduleName);
//                        Module module = cls.newInstance();
//                        ((ScaffoldApplication) getApplication())
//                                .setInjector(Guice.createInjector(module));
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onCreated(this);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((ScaffoldApplication) getApplication()).getTracker()
                .trackActivityStart(this);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onStarted(this);
            }
        }
    }

    @Override
    protected void onStop() {
        ((ScaffoldApplication) getApplication()).getTracker()
                .trackActivityStop(this);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onStopped(this);
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        clearCroutons();
        Crouton.cancelAllCroutons();

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onDestroyed(this);
            }
            mScaffoldLifecycleListeners.clear();
        }
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null && !frags.isEmpty()) {
            for (Fragment f : frags) {
                if (f.isAdded()) {
                    if (f instanceof ScaffoldOnWindowFocusChangedListener) {
                        ((ScaffoldOnWindowFocusChangedListener) f).onWindowFocusChanged(hasFocus);
                    }
                }
            }
        }
    }

    // This snippet hides the system bars.
    @SuppressLint("InlinedApi")
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
//        if (UIUtils.hasHoneycombMR1()) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        }
        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();;
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (UIUtils.hasICS()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Status bar hiding: Backwards compatible to Jellybean
        if (UIUtils.hasJellyBean()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (UIUtils.hasKitkat()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    // This snippet hides the system bars.
    @SuppressLint("InlinedApi")
    private void hideSystemUISticky() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
//        if (UIUtils.hasHoneycombMR1()) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }

        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();;
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (UIUtils.hasICS()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Status bar hiding: Backwards compatible to Jellybean
        if (UIUtils.hasJellyBean()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (UIUtils.hasKitkat()) {
            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    @SuppressLint("InlinedApi")
    private void showSystemUI() {
//        if (UIUtils.hasHoneycombMR1()) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (UIUtils.hasICS()) {
            newUiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Status bar hiding: Backwards compatible to Jellybean
        if (UIUtils.hasJellyBean()) {
            newUiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (UIUtils.hasKitkat()) {
            newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    // ------------------------------------------------------------------------
    // InternalHandler Utility
    // ------------------------------------------------------------------------

    /**
     * Handled message
     *
     * @param msg Message
     */
    protected void handleMessage(final Message msg) {
    }

    /**
     * Get InternalHandler
     *
     * @return Handler
     */
    protected Handler getInternalHandler() {
        if (mInternalHandler == null) {
            mInternalHandler = new InternalHandler(this);
        }
        return mInternalHandler;
    }

    /**
     * InternalHandler
     */
    protected static class InternalHandler extends Handler {
        private final WeakReference<ScaffoldActivity> mActivity;

        /**
         * Constructor
         *
         * @param activity
         */
        public InternalHandler(ScaffoldActivity activity) {
            mActivity = new WeakReference<ScaffoldActivity>(activity);
        }

        @Override
        public void handleMessage(final Message msg) {
            ScaffoldActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Takes a given intent and either starts a new activity to handle it (the
     * default behavior), or creates/updates a fragment (in the case of a
     * multi-pane activity) that can handle the intent. Must be called from the
     * main (UI) thread.
     */
    public void openActivityOrFragment(final Intent intent) {
        // Default implementation simply calls startActivity
        startActivity(intent);
    }

    // ------------------------------------------------------------------------

    /**
     * Converts an intent into a {@link android.os.Bundle} suitable for use as
     * fragment arguments.
     */
    public static final Bundle intentToFragmentArguments(final Intent intent) {
        final Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable(ScaffoldConstants.ARG_URI, data);
        }

        final String action = intent.getAction();
        if (action != null) {
            arguments.putString(ScaffoldConstants.ARG_ACTION, action);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static final Intent fragmentArgumentsToIntent(final Bundle arguments) {
        final Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable(ScaffoldConstants.ARG_URI);
        if (data != null) {
            intent.setData(data);
        }

        final String action = arguments.getString(ScaffoldConstants.ARG_ACTION);
        if (action != null) {
            intent.setAction(action);
        }

        intent.putExtras(arguments);
        intent.removeExtra(ScaffoldConstants.ARG_URI);
        return intent;
    }

    // ------------------------------------------------------------------------
    // EventBus
    // ------------------------------------------------------------------------
    protected final void postEvent(Object e) {
        BusProvider.getInstance().post(e);
    }

    protected final void registerEventBus() {
        BusProvider.getInstance().register(this);
    }

    protected final void unregisterEventBus() {
        BusProvider.getInstance().unregister(this);
    }

    // ------------------------------------------------------------------------
    // AlertDialogFragment Utility
    // ------------------------------------------------------------------------

    /**
     * showDialog (Only one positive button)
     *
     * @param iconId
     * @param title
     * @param message
     * @param tag
     */
    protected final void showDialog(
            final int iconId, final String title, final String message, final String tag) {
        showDialog(iconId, title, message, true, false, false, false, tag);
    }

    /**
     * showDialogYesNo
     *
     * @param iconId
     * @param title
     * @param message
     * @param tag
     */
    protected final void showDialogYesNo(
            final int iconId, final String title, final String message, final String tag) {
        showDialog(iconId, title, message, true, false, true, false, tag);
    }

    /**
     * @param iconId
     * @param title
     * @param message
     * @param hasPositive
     * @param hasNeutral
     * @param hasNegative
     * @param cancelable
     * @param tag
     */
    protected final void showDialog(
            final int iconId, final String title, final String message,
            final boolean hasPositive, final boolean hasNeutral, final boolean hasNegative,
            final boolean cancelable, final String tag) {
        ScaffoldAlertDialogFragment.builder(this, getSupportFragmentManager())
                .iconId(iconId)
                .title(title)
                .message(message)
                .hasPositive(hasPositive)
                .hasNeutral(hasNeutral)
                .hasNegative(hasNegative)
                .cancelable(cancelable)
                .tag(tag)
                .show();
    }

    /**
     * @param iconId
     * @param title
     * @param message
     * @param hasPositive
     * @param hasNeutral
     * @param hasNegative
     * @param positiveText
     * @param neutralText
     * @param negativeText
     * @param cancelable
     * @param tag
     */
    protected final void showDialog(
            final int iconId, final String title, final String message,
            final boolean hasPositive, final boolean hasNeutral, final boolean hasNegative,
            final int positiveText, final int neutralText, final int negativeText,
            final boolean cancelable, final String tag) {
        ScaffoldAlertDialogFragment.builder(this, getSupportFragmentManager())
                .iconId(iconId)
                .title(title)
                .message(message)
                .hasPositive(hasPositive)
                .hasNeutral(hasNeutral)
                .hasNegative(hasNegative)
                .positiveText(positiveText)
                .neutralText(neutralText)
                .negativeText(negativeText)
                .cancelable(cancelable)
                .tag(tag)
                .show();
    }

    // CallBack Listener
    @Override
    public final void onDialogClick(final DialogInterface dialog,
                                    final int whichButton, final String tag) {
        switch (whichButton) {
        case DialogInterface.BUTTON_POSITIVE:
            doPositiveClick(dialog, whichButton, tag);
            break;
        case DialogInterface.BUTTON_NEUTRAL:
            doNeutralClick(dialog, whichButton, tag);
            break;
        case DialogInterface.BUTTON_NEGATIVE:
            doNegativeClick(dialog, whichButton, tag);
            break;
        default:
            break;
        }
    }

    @Override
    public final void onDialogCancel(final DialogInterface dialog, final String tag) {
        doCancel(dialog, tag);
    }

    /**
     * doPositiveClick
     *
     * @param dialog
     * @param whichButton
     * @param tag
     */
    protected void doPositiveClick(final DialogInterface dialog,
                                   final int whichButton, final String tag) {
    }

    /**
     * doNeutralClick
     *
     * @param dialog
     * @param whichButton
     * @param tag
     */
    protected void doNeutralClick(final DialogInterface dialog,
                                  final int whichButton, final String tag) {

    }

    /**
     * doNegativeClick
     *
     * @param dialog
     * @param whichButton
     * @param tag
     */
    protected void doNegativeClick(final DialogInterface dialog,
                                   final int whichButton, final String tag) {
    }

    /**
     * doCancel
     *
     * @param dialog
     * @param tag
     */
    protected void doCancel(final DialogInterface dialog, final String tag) {
    }

    /**
     * show progress dialog
     *
     * @param iconId
     * @param title
     * @param message
     */
    protected DialogFragment showProgressDialog(final int iconId,
                                                final String title, final String message) {
        final DialogFragment newFragment = ScaffoldProgressDialogFragment
                .newInstance(0, title, message);
        newFragment.show(getSupportFragmentManager(), "progress_dialog");
        return newFragment;
    }

    /*
     * This method was deprecated in API level 13.
     * Use the new DialogFragment class with FragmentManager instead; this is also available on older platforms through the Android compatibility package.
     * 
     * - onCreateDialog(int, Bundle)
     * - onPrepareDialog(int, Dialog, Bundle)
     * - showDialog(int)
     * - dismissDialog (int id)
     * - removeDialog(int)
     */

    /**
     * @deprecated Old no-arguments version of {@link #onCreateDialog(int, Bundle)}.
     */
    @Deprecated
    @Override
    protected final Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    /**
     * @deprecated Use the new {@link android.app.DialogFragment} class with
     * {@link FragmentManager} instead; this is also
     * available on older platforms through the Android compatibility package.
     */
    @Deprecated
    @Override
    protected final Dialog onCreateDialog(int id, Bundle args) {
        return super.onCreateDialog(id, args);
    }

    /**
     * @deprecated Old no-arguments version of
     * {@link #onPrepareDialog(int, Dialog, Bundle)}.
     */
    @Deprecated
    @Override
    protected final void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }

    /**
     * @deprecated Use the new {@link android.app.DialogFragment} class with
     * {@link FragmentManager} instead; this is also
     * available on older platforms through the Android compatibility package.
     */
    @Deprecated
    @Override
    protected final void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog, args);
    }

    // ------------------------------------------------------------------------
    // IconContextMenu DialogFragment Utility
    // ------------------------------------------------------------------------
    public void showIconContextMenu(String title,
                                    ArrayList<IconContextMenuItem> iconContextMenuItems) {

        IconContextMenu iconContextMenu = new IconContextMenu(iconContextMenuItems);
        // set Title
        iconContextMenu.setMenuTitle(title);
        // show
        iconContextMenu.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onIconContextMenuOnItemSelected(final IconContextMenuItem menuItem) {
        doIconContextMenuOnItemSelected(menuItem);
    }

    public void doIconContextMenuOnItemSelected(final IconContextMenuItem menuItem) {
    }

    // ------------------------------------------------------------------------
    // Toast Utility
    // ------------------------------------------------------------------------
    // Normal Toast
    public void showToast(final String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public void showToast(final String text, int duration) {
        Toast.makeText(this, text, duration).show();
    }

    public void showToast(final int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public void showToast(final int resId, int duration) {
        Toast.makeText(this, resId, duration).show();
    }

    // Crouton Toast
    public void showCroutonAlert(final CharSequence text) {
        showCrouton(text, de.keyboardsurfer.android.widget.crouton.Style.ALERT);
    }

    public void showCroutonConfirm(final CharSequence text) {
        showCrouton(text, de.keyboardsurfer.android.widget.crouton.Style.CONFIRM);
    }

    public void showCroutonInfo(final CharSequence text) {
        showCrouton(text, de.keyboardsurfer.android.widget.crouton.Style.INFO);
    }

    public void showCroutonAlert(final int textResourceId) {
        showCrouton(textResourceId, de.keyboardsurfer.android.widget.crouton.Style.ALERT);
    }

    public void showCroutonConfirm(final int textResourceId) {
        showCrouton(textResourceId, de.keyboardsurfer.android.widget.crouton.Style.CONFIRM);
    }

    public void showCroutonInfo(final int textResourceId) {
        showCrouton(textResourceId, de.keyboardsurfer.android.widget.crouton.Style.INFO);
    }

    public void showCrouton(final int textResourceId,
                            final de.keyboardsurfer.android.widget.crouton.Style croutonStyle) {
        showCrouton(getString(textResourceId), croutonStyle);
    }

    public void showCrouton(final CharSequence croutonText,
                            final de.keyboardsurfer.android.widget.crouton.Style croutonStyle) {
        Crouton.makeText(this, croutonText, croutonStyle).show();
    }

    public void showCrouton(final CharSequence croutonText,
                            final de.keyboardsurfer.android.widget.crouton.Style croutonStyle,
                            ViewGroup viewGroup) {
        Crouton.makeText(this, croutonText, croutonStyle, viewGroup).show();
    }

    private Crouton mCrouton;

    private static final de.keyboardsurfer.android.widget.crouton.Style INFINITE_ALERT =
            new Builder()
                    .setConfiguration(
                            new Configuration.Builder()
                                    .setDuration(
                                            Configuration.DURATION_INFINITE)
                                    .build())
                    .setBackgroundColorValue(
                            de.keyboardsurfer.android.widget.crouton.Style.holoRedLight)
                    .setHeight(LayoutParams.WRAP_CONTENT)
                    .build();

    protected Crouton showCroutonAlertInfinity(final CharSequence text, int viewGroupResId) {
        if (mCrouton != null) {
            mCrouton.cancel();
        }
        mCrouton = Crouton.makeText(this, text, INFINITE_ALERT, viewGroupResId);
        mCrouton.show();

        return mCrouton;
    }

    protected Crouton showCroutonAlertInfinity(final CharSequence text) {
        if (mCrouton != null) {
            mCrouton.cancel();
        }
        mCrouton = Crouton.makeText(this, text, INFINITE_ALERT);
        mCrouton.show();

        return mCrouton;
    }

    protected Crouton showCroutonAlertInfinity(final int textResourceId, int viewGroupResId) {
        if (mCrouton != null) {
            mCrouton.cancel();
        }
        mCrouton = Crouton.makeText(this, textResourceId, INFINITE_ALERT, viewGroupResId);
        mCrouton.show();

        return mCrouton;
    }

    protected Crouton showCroutonAlertInfinity(final int textResourceId) {
        if (mCrouton != null) {
            mCrouton.cancel();
        }
        mCrouton = Crouton.makeText(this, textResourceId, INFINITE_ALERT);
        mCrouton.show();

        return mCrouton;
    }

    public void clearCroutons() {
        if (mCrouton != null) {
            mCrouton.cancel();
        }
        Crouton.clearCroutonsForActivity(this);
    }

    // ------------------------------------------------------------------------
    // findView Utility
    // ------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    protected <T> T findView(View v, int id) {
        return (T) v.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T> T findView(int id) {
        return (T) findViewById(id);
    }

    // ------------------------------------------------------------------------
    // findFragment Utility
    // ------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    protected <T> T findFragment(int id) {
        return (T) getSupportFragmentManager().findFragmentById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T> T findFragment(String tag) {
        return (T) getSupportFragmentManager().findFragmentByTag(tag);
    }

//    /**
//     * IntentBuilder
//     */
//    public static class IntentBuilder
//            extends ActivityIntentBuilder<ScaffoldActivity.IntentBuilder> {
//
//        private Fragment fragmentSupport_;
//
//        public IntentBuilder(Context context, Class<?> clazz) {
//            super(context, clazz);
//        }
//
//        public IntentBuilder(Fragment fragment, Class<?> clazz) {
//            super(fragment.getActivity(), clazz);
//            fragmentSupport_ = fragment;
//        }
//
//        @Override
//        public void startForResult(int requestCode) {
//            if (fragmentSupport_!= null) {
//                fragmentSupport_.startActivityForResult(intent, requestCode);
//            }
//        }
//    }
}
