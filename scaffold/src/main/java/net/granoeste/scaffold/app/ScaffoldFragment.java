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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.uphyca.lifecyclecallbacks.LifecycleCallbacksSupportFragment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

public abstract class ScaffoldFragment extends LifecycleCallbacksSupportFragment
        implements ScaffoldAlertDialogFragment.OnAlertDialogEventListener,
        ScaffoldOnWindowFocusChangedListener {
    private static final String TAG = makeLogTag(ScaffoldFragment.class);
    public static final String ARG_URI = ScaffoldConstants.ARG_URI;
    public static final String ARG_ACTION = ScaffoldConstants.ARG_ACTION;
    private ScaffoldAlertDialogFragment mAlertDialogFragment;

    private final Set<ScaffoldLifecycleListener> mScaffoldLifecycleListeners
            = Collections.synchronizedSet(new HashSet<ScaffoldLifecycleListener>());

    protected void addLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.add(listener);
    }

    protected void removeLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.remove(listener);
    }

    protected boolean mHasSavedInstanceState;
    protected Configuration mConfiguration;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mConfiguration = getResources().getConfiguration();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHasSavedInstanceState = (savedInstanceState != null);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onCreated(getActivity());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((ScaffoldApplication) getActivity().getApplication()).getTracker().trackFragmentStart(this);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onStarted(getActivity());
            }
        }
    }

    @Override
    public void onStop() {
        ((ScaffoldApplication) getActivity().getApplication()).getTracker().trackFragmentStop(this);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onStopped(getActivity());
            }
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onDestroyed(getActivity());
            }
            mScaffoldLifecycleListeners.clear();
        }
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {}

    protected ScaffoldActivity getScaffoldActivity() {
        return (ScaffoldActivity) getActivity();
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
        ScaffoldAlertDialogFragment.builder(getActivity(), getActivity().getSupportFragmentManager())
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
     *
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
        ScaffoldAlertDialogFragment.builder(getActivity(), getActivity().getSupportFragmentManager())
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
    public final void onDialogClick(final DialogInterface dialog, final int whichButton, final String tag) {
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
    protected void doPositiveClick(final DialogInterface dialog, final int whichButton,
                                   final String tag) {
    }

    /**
     * doNeutralClick
     *
     * @param dialog
     * @param whichButton
     * @param tag
     */
    protected void doNeutralClick(final DialogInterface dialog, final int whichButton,
                                  final String tag) {

    }

    /**
     * doNegativeClick
     *
     * @param dialog
     * @param whichButton
     * @param tag
     */
    protected void doNegativeClick(final DialogInterface dialog, final int whichButton,
                                   final String tag) {
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
    protected final void showProgressDialog(final int iconId,
                                            final String title, final String message) {
        final DialogFragment newFragment = ScaffoldProgressDialogFragment
                .newInstance(0, title, message);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getActivity().getSupportFragmentManager(), "progress_dialog");
    }

    // ------------------------------------------------------------------------
    // Toast Utility
    // ------------------------------------------------------------------------
    // Normal Toast
    public void showToast(final String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public void showToast(final String text, int duration) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, text, duration).show();
        }
    }

    public void showToast(final int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public void showToast(final int resId, int duration) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, resId, duration).show();
        }
    }

    // ------------------------------------------------------------------------
    // findView Utility
    // ------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    protected <T> T findView(View v, int id) {
        return (T) v.findViewById(id);
    }

    public static class FragmentBuilder
            extends net.granoeste.scaffold.app.builder.FragmentBuilder<ScaffoldFragment
            .FragmentBuilder, ScaffoldFragment> {

        @Override
        public ScaffoldFragment build() {
// WIP
//            ScaffoldFragment fragment = new ScaffoldFragment();
//            fragment.setArguments(args);
//            return fragment;
            return null;
        }
    }
}
