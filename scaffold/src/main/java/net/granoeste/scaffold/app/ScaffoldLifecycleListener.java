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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Activity/Fragment/Serviceのライフサイクルリスナー
 * Scaffoldはライフサイクルに合わせてこのリスナーをコールする。
 */
public abstract class ScaffoldLifecycleListener {

    public interface Callback {
        void onEvent(ScaffoldLifecycleListener listener, Object e);
    }

    protected Callback mCallback = new Callback(){
        @Override
        public void onEvent(ScaffoldLifecycleListener listener, Object e) {
            // NOP
        }
    };

    public ScaffoldLifecycleListener() {
    }

    public ScaffoldLifecycleListener(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void onCreated(Context context) {
    }

    public void onStarted(Context context) {
    }

    public void onStopped(Context context) {
    }

    public void onDestroyed(Context context) {
    }

    protected void postEvent(Object e ) {
        mCallback.onEvent(this, e);
    }
}
