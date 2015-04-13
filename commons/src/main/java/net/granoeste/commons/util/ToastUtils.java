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

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
    private static final Object LOCK = new Object();
    private static Handler sHandler;

    public static void showToast(final Context context, final CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(final Context context, final CharSequence text,
                                 final int duration) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            synchronized (LOCK) {
                if (sHandler == null) {
                    sHandler = new Handler(Looper.getMainLooper());
                }
            }
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context.getApplicationContext(), text, duration).show();
                }
            });
        } else {
            Toast.makeText(context.getApplicationContext(), text, duration).show();
        }
    }

}
