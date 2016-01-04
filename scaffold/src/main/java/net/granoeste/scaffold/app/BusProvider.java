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

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;

import net.granoeste.commons.util.ThreadUtils;
import net.granoeste.scaffold.BuildConfig;

import static net.granoeste.commons.util.LogUtils.LOGE;
import static net.granoeste.commons.util.LogUtils.LOGI;
import static net.granoeste.commons.util.LogUtils.makeLogTag;

/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more
 * efficient means
 * such as through injection directly into interested classes.
 */
public final class BusProvider {
    private static final String TAG = makeLogTag(BusProvider.class);
    private static final Object LOCK = new Object();
    private static MyBus sInstance;

    public static MyBus getInstance() {

        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new MyBus(new Handler(Looper.getMainLooper()));
                LOGI(TAG, "The bus was ready.");
            }
            return sInstance;
        }
    }

    private BusProvider() {
        // No instances.
    }

    public static class MyBus extends Bus {
        private Handler mHandler;

        public MyBus(Handler handler) {
            super();
            mHandler = handler;
        }

        @Override
        public void post(final Object event) {
            if (event instanceof DeadEvent) {
                LOGE(TAG, "An event could not be delivered", new RuntimeException(
                        "An event was posted, but which had no subscribers and thus could not be delivered. It's... "
                                + ((DeadEvent) event).event));
            }

            // Strict Examination !!
            if (BuildConfig.DEBUG) {
                boolean accept = false;
                Class<?> cls = ThreadUtils.calledFromClass();
                Class<?> sprCls = cls.getSuperclass();
                while (sprCls != null) {
                    if (sprCls == android.app.Activity.class
                            || sprCls == android.support.v4.app.Fragment.class
                            || sprCls == android.app.Service.class
                            || sprCls == net.granoeste.scaffold.app.ScaffoldLifecycleListener.class
                            ) {
                        accept = true;
                        break;
                    }
                    sprCls = sprCls.getSuperclass();
                }
                if (!accept) {
                    LOGE(TAG, "Post method is not allowed.", new RuntimeException(
                            "Should be post an event with on Activity,Fragment,Service and ScaffoldLifecycleListener."));
                }
            }

            if (Looper.myLooper() != mHandler.getLooper()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyBus.super.post(event);
                    }
                });
            } else {
                super.post(event);
            }
        }
    }

    public static void postEvent(Object event) {
        BusProvider.getInstance().post(event);
    }

    public static void register(Object obj) {
        BusProvider.getInstance().register(obj);
    }

    public static void unregister(Object obj) {
        BusProvider.getInstance().unregister(obj);
    }
}
