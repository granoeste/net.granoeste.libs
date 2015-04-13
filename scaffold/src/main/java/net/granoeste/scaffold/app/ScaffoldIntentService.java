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

import android.content.Context;
import android.content.Intent;

import com.uphyca.lifecyclecallbacks.LifecycleCallbacksSupportIntentService;

import net.granoeste.scaffold.app.builder.ServiceIntentBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ScaffoldIntentService extends LifecycleCallbacksSupportIntentService {
    public ScaffoldIntentService(String name) {
        super(name);
    }

    private final Set<ScaffoldLifecycleListener> mScaffoldLifecycleListeners
            = Collections.synchronizedSet(new HashSet<ScaffoldLifecycleListener>());

    protected void addLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.add(listener);
    }

    protected void removeLifecycleListener(ScaffoldLifecycleListener listener) {
        mScaffoldLifecycleListeners.remove(listener);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onCreated(this);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onStarted(this);
            }
        }
        return ret;
    }


    @Override
    public void onDestroy() {

        // ServiceのライフサイクルにonStopは無いのでonDestroyで実施する。
        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onStopped(this);
            }
        }

        synchronized (mScaffoldLifecycleListeners) {
            for (ScaffoldLifecycleListener listener : mScaffoldLifecycleListeners) {
                listener.onDestroyed(this);
            }
            mScaffoldLifecycleListeners.clear();
        }

        super.onDestroy();
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

    /**
     * IntentBuilder
     */
    public static class IntentBuilder
            extends ServiceIntentBuilder<ScaffoldService.IntentBuilder> {
        public IntentBuilder(Context context, Class<?> clazz) {
            super(context, clazz);
        }
    }
}
