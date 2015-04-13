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

package net.granoeste.commons.util;

import android.os.AsyncTask;
import android.os.Looper;

/**
 * SimpleAsyncTask.
 * This provides simply doInBackground and onPostExecute that does not require argument and return value is void.
 * Use the original AsyncTask If you want to use the arguments and progress.
 *
 * @see android.os.AsyncTask
 */
public abstract class SimpleAsyncTask extends AsyncTask<Void, Void, Void> {

    public SimpleAsyncTask() {
        if (Thread.currentThread().getId() != Looper.myLooper().getThread().getId()) {
            throw new IllegalStateException("This thread is not main.");
        }
    }

    @Deprecated
    @Override
    protected final Void doInBackground(Void... params) {
        this.doInBackground();
        return null;
    }

    @Deprecated
    @Override
    protected final void onPostExecute(Void result) {
        super.onPostExecute(result);
        this.onPostExecute();
    }

    @Deprecated
    @Override
    protected final void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    abstract protected void doInBackground();

    protected void onPostExecute() {
        // No default action
    }

}