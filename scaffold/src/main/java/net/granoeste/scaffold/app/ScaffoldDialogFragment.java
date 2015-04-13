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

import static net.granoeste.commons.util.LogUtils.makeLogTag;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uphyca.lifecyclecallbacks.LifecycleCallbacksSupportDialogFragment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ScaffoldDialogFragment extends LifecycleCallbacksSupportDialogFragment {
    private static final String TAG = makeLogTag(ScaffoldDialogFragment.class);
    public static final String ARG_URI = ScaffoldConstants.ARG_URI;
    public static final String ARG_ACTION = ScaffoldConstants.ARG_ACTION;

    private final Set<ScaffoldLifecycleListener> mScaffoldLifecycleListeners = Collections
            .synchronizedSet(new HashSet<ScaffoldLifecycleListener>());

    protected void addLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.add(listener);
    }

    protected void removeLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.remove(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onCreated(getActivity());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((ScaffoldApplication) getActivity().getApplication()).getTracker()
                .trackFragmentStart(this);

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

    /**
     * If you want to include custom view in the dialog, You Should be override onCreateView and return custom view.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return custom view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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

}
